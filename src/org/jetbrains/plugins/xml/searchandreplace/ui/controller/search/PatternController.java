package org.jetbrains.plugins.xml.searchandreplace.ui.controller.search;


import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.project.Project;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.persistence.ConstraintEntry;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.persistence.PatternStorageEntry;
import org.jetbrains.plugins.xml.searchandreplace.search.Node;
import org.jetbrains.plugins.xml.searchandreplace.search.Pattern;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.captures.CapturesManager;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.captures.Capture;
import org.jetbrains.plugins.xml.searchandreplace.ui.view.search.PatternView;

import java.util.*;

public class PatternController implements ConstraintControllerDelegate, PersistentStateComponent<PatternStorageEntry>,PatternView.Delegate {

  private Project project;
  private boolean previewMode;

  @Override
  public void useRegexps(boolean use) {
    useRegularExpressions = use;
    for (ConstraintController c : constraintsTree.keySet()) {
      c.useRegexps(use);
    }
  }

  @Override
  public boolean useRegexps() {
    return useRegularExpressions;
  }

  @Override
  public void badInput(ConstraintController constraintController) {
    getDelegate().badInput(this);
  }

  @Override
  public void killCapture(Capture c) {
    capturesManager.unregisterCapture(c);
  }

  public void setPreviewMode(boolean previewMode) {
    this.previewMode = previewMode;
    for (ConstraintController cc : constraintsTree.keySet()) {
      cc.setPreviewMode(previewMode);
    }
    view.setPreviewMode(previewMode);
  }

  public interface Delegate {

    void pleaseAutoresizeWindow(PatternController c);

    void badInput(PatternController patternController);
  }
  private Delegate delegate;
  private CapturesManager capturesManager = new CapturesManager();

  private PatternView view = new PatternView();

  private Map<ConstraintController, ArrayList<ConstraintController>> constraintsTree = new HashMap<ConstraintController, ArrayList<ConstraintController>>();

  private ConstraintController root;

  boolean useRegularExpressions = false;

  public Delegate getDelegate() {
    return delegate;
  }

  public void setDelegate(Delegate delegate) {
    this.delegate = delegate;
  }


  public CapturesManager getCapturesManager() {
    return capturesManager;
  }

  @Override
  public PatternStorageEntry getState() {
    HashMap<Integer, ArrayList<Integer>> tree = new HashMap<Integer, ArrayList<Integer>>();

    Map<ConstraintController, ConstraintEntry> entriesForControllers = new HashMap<ConstraintController, ConstraintEntry>();
    int id = 0;
    for (ConstraintController c : constraintsTree.keySet()) {
      ConstraintEntry state = c.getState();
      state.setId(id++);
      entriesForControllers.put(c, state);
    }

    for (ConstraintController c : constraintsTree.keySet()) {
      ArrayList<Integer> childrenIds = new ArrayList<Integer>();
      for (ConstraintController child : constraintsTree.get(c)) {
        childrenIds.add(entriesForControllers.get(child).getId());
      }
      tree.put(entriesForControllers.get(c).getId(), childrenIds);
    }
    PatternStorageEntry state = new PatternStorageEntry();
    state.setEntries(new ArrayList<ConstraintEntry>(entriesForControllers.values()));
    state.setTree(tree);
    state.setRoot(entriesForControllers.get(root).getId());
    state.setUseRegexps(useRegularExpressions);
    return state;
  }

  private void initController(PatternStorageEntry state, int id, ConstraintController parent, Map<Integer, ConstraintController> ready) {
    ConstraintController constraintController = ready.get(id);
    if (constraintController == null) {
      constraintController = new ConstraintController(true, parent, project);
      ready.put(id, constraintController);

      addConstraintController(constraintController);

      ArrayList<ConstraintEntry> constraintEntries = state.getEntries();
      ConstraintEntry constraintEntry = null;
      for (ConstraintEntry ce : constraintEntries) {
        if (ce.getId() == id) {
          constraintEntry = ce;
        }
      }
      if (constraintEntry != null) {
        constraintController.loadState(constraintEntry);
      }
    }
    if (id == state.getRoot()) {
      this.root = constraintController;
    }
    for (int childId : state.getTree().get(id)) {
      initController(state, childId, constraintController, ready);
    }
    validateMe(constraintController);
  }

  @Override
  public void loadState(PatternStorageEntry state) {
    if (state != null) {
      removeConstraintController(root);
      initController(state, state.getRoot(), null, new HashMap<Integer, ConstraintController>());
      useRegexps(state.getUseRegexps());
      view.setUseRegexps(useRegularExpressions);
    }
  }

  private void addConstraintController(ConstraintController pc) {
    pc.setDelegate(this);
    constraintsTree.put(pc, new ArrayList<ConstraintController>());
    ConstraintController parent = pc.getParent();
    if (parent != null) {
      constraintsTree.get(parent).add(pc);
    }
    view.addConstraintView(pc.getView(), parent == null ? null : parent.getView());
    if (delegate != null) {
      delegate.pleaseAutoresizeWindow(this);
    }
  }

  private void removeConstraintController(ConstraintController constraintController) {
    capturesManager.paredicateControllerIsDead(constraintController);
    List<ConstraintController> pcList = new ArrayList<ConstraintController>();
    for (ConstraintController pc : constraintsTree.get(constraintController)) {
      pcList.add(pc);
    }
    for (ConstraintController pc : pcList) {
      removeConstraintController(pc);
    }
    ArrayList<ConstraintController> brothers = constraintsTree.get(constraintController.getParent());
    if (brothers != null) {
      brothers.remove(constraintController);
    }

    constraintsTree.remove(constraintController);
    view.removeConstraintView(constraintController.getView(), constraintController.getParent() != null ?
            constraintController.getParent().getView() : null);
    if (delegate != null) {
      delegate.pleaseAutoresizeWindow(this);
    }

  }

  public PatternView getView() {
    return view;
  }

  public PatternController(Project project) {
    this.project = project;
    view.setDelegate(this);
    root = new ConstraintController(true, null, project);
    addConstraintController(root);
    root.constraintTypeSelected(null, null);
  }

  public Pattern buildPattern() {
    Pattern pattern = new Pattern(new HashSet<Node>());
    if (root != null) {
      gatherNodes(pattern, root);
    }

    pattern.endBuild();
    return pattern;
  }

  private void gatherNodes(Pattern pattern, ConstraintController root) {
    root.buildNode(pattern);
    List<ConstraintController> children = constraintsTree.get(root);
    if (children != null) {
      for (ConstraintController child : children) {
        gatherNodes(pattern, child);
      }
    }
  }

  public void addChild(ConstraintController parent) {
    ConstraintType selectedConstraintType = parent.getSelectedConstraintType();
    if (selectedConstraintType == null) return;

    Collection<ConstraintType> allowedChildrenTypes = parent.getConstraintTypeController().getAllowedChildrenTypes();
    if (!allowedChildrenTypes.isEmpty()) {
      addConstraintController(new ConstraintController(false, parent, project));
    }
  }

  public List<ConstraintType> getAllowedPredicateTypes(ConstraintController constraintController) {
    ConstraintController parent = constraintController.getParent();
    if (parent == null) {
      return ConstraintTypesRegistry.getInstance(project).getConstraintTypes();
    }

    if (parent.getConstraintTypeController() != null) {
      return parent.getConstraintTypeController().getAllowedChildrenTypes();
    } else {
      return new ArrayList<ConstraintType>();
    }
  }

  public void removeMe(ConstraintController constraintController) {
    removeConstraintController(constraintController);
  }

  public void validateMe(ConstraintController constraintController) {
    ConstraintType selectedConstraintType = constraintController.getSelectedConstraintType();

    ConstraintTypeController constraintTypeController = constraintController.getConstraintTypeController();
    Collection<ConstraintType> allowedChildrenTypes = constraintTypeController == null ? new ArrayList<ConstraintType>() :
            constraintTypeController.getAllowedChildrenTypes();
    ArrayList<ConstraintController> constraintControllers = constraintsTree.get(constraintController);
    List<ConstraintController> children = new ArrayList<ConstraintController>(constraintControllers);
    for (ConstraintController child : children) {
      if (!allowedChildrenTypes.contains(child.getSelectedConstraintType())) {
        removeConstraintController(child);
      }
    }
    constraintController.setCanHaveChildren(!allowedChildrenTypes.isEmpty());
    registerCapturesIfNesessary(constraintController);
    if (delegate != null) {
      delegate.pleaseAutoresizeWindow(this);
    }
  }

  @Override
  public void loadCapturesFor(ConstraintController constraintController, ConstraintEntry state) {
    List<String> capturesIds = state.getCapturesIds();
    List<Capture> captures = constraintController.getCaptures();
    for (int i = 0; i < capturesIds.size(); ++i) {
      capturesManager.registerNewCapture(constraintController, captures.get(i), capturesIds.get(i));
    }
  }



  private void registerCapturesIfNesessary(ConstraintController constraintController) {
    boolean unregistered = false;
    for (Capture c : constraintController.getCaptures()) {
      if (!capturesManager.isCaptureRegistered(c)) {
        capturesManager.registerNewCapture(constraintController, c);
        unregistered = true;
      }
    }
    if (unregistered) {
      constraintController.getView().setCaptures(constraintController.getCaptures());
    }
  }

}
