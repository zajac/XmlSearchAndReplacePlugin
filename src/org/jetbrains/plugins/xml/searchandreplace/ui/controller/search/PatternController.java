package org.jetbrains.plugins.xml.searchandreplace.ui.controller.search;


import org.jetbrains.plugins.xml.searchandreplace.search.Node;
import org.jetbrains.plugins.xml.searchandreplace.search.Pattern;
import org.jetbrains.plugins.xml.searchandreplace.ui.CapturePresentationFactory;
import org.jetbrains.plugins.xml.searchandreplace.ui.ConstraintType;
import org.jetbrains.plugins.xml.searchandreplace.ui.PredicateTypeRegistry;
import org.jetbrains.plugins.xml.searchandreplace.ui.view.PatternView;

import java.util.*;

public class PatternController implements PredicateControllerDelegate {

  public interface Delegate {
    void pleaseAutoresizeWindow(PatternController c);
  }

  private Delegate delegate;

  public Delegate getDelegate() {
    return delegate;
  }

  public void setDelegate(Delegate delegate) {
    this.delegate = delegate;
  }

  private PatternView view = new PatternView();
  private Map<ConstraintController, ArrayList<ConstraintController>> predicatesTree = new HashMap<ConstraintController, ArrayList<ConstraintController>>();
  private ConstraintController root;

  private void addPredicateController(ConstraintController pc) {
    pc.setDelegate(this);
    predicatesTree.put(pc, new ArrayList<ConstraintController>());
    ConstraintController parent = pc.getParent();
    if (parent != null) {
      predicatesTree.get(parent).add(pc);
    }
    view.addPredicateView(pc.getView(), parent == null ? null : parent.getView());
    if (delegate != null) {
      delegate.pleaseAutoresizeWindow(this);
    }
  }

  private void removePredicateController(ConstraintController constraintController) {
    List<ConstraintController> pcList = new ArrayList<ConstraintController>();
    for (ConstraintController pc : predicatesTree.get(constraintController)) {
      pcList.add(pc);
    }
    for (ConstraintController pc : pcList) {
      removePredicateController(pc);
    }
    predicatesTree.get(constraintController.getParent()).remove(constraintController);
    predicatesTree.remove(constraintController);
    view.removePredicateView(constraintController.getView(), constraintController.getParent() != null ?
            constraintController.getParent().getView() : null);
    if (delegate != null) {
      delegate.pleaseAutoresizeWindow(this);
    }

  }

  public PatternView getView() {
    return view;
  }

  public PatternController() {
    root = new ConstraintController(true, null);
    addPredicateController(root);
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
    List<ConstraintController> children = predicatesTree.get(root);
    if (children != null) {
      for (ConstraintController child : children) {
        gatherNodes(pattern, child);
      }
    }
  }

  public void addChild(ConstraintController constraintController) {
    ConstraintType selectedConstraintType = constraintController.getSelectedConstraintType();
    if (selectedConstraintType == null) return;

    Collection<ConstraintType> allowedChildrenTypes = constraintController.getConstraintTypeController().getAllowedChildrenTypes();
    if (!allowedChildrenTypes.isEmpty()) {
      addPredicateController(new ConstraintController(false, constraintController));
    }
  }

  public List<ConstraintType> getAllowedPredicateTypes(ConstraintController constraintController) {
    ConstraintController parent = constraintController.getParent();
    if (parent == null) {
      return PredicateTypeRegistry.getInstance().getConstraintTypes();
    }

    if (parent.getConstraintTypeController() != null) {
      return parent.getConstraintTypeController().getAllowedChildrenTypes();
    } else {
      return new ArrayList<ConstraintType>();
    }
  }

  public void removeMe(ConstraintController constraintController) {
    removePredicateController(constraintController);
    CapturePresentationFactory.instance().paredicateControllerIsDead(constraintController);
  }

  public void validateMe(ConstraintController constraintController) {
    ConstraintType selectedConstraintType = constraintController.getSelectedConstraintType();

    ConstraintTypeController constraintTypeController = constraintController.getConstraintTypeController();
    Collection<ConstraintType> allowedChildrenTypes = constraintTypeController == null ? new ArrayList<ConstraintType>() :
            constraintTypeController.getAllowedChildrenTypes();
    List<ConstraintController> children = (List<ConstraintController>) predicatesTree.get(constraintController).clone();
    for (ConstraintController child : children) {
      if (!allowedChildrenTypes.contains(child.getSelectedConstraintType())) {
        removePredicateController(child);
      }
    }
    constraintController.setCanHaveChildren(!allowedChildrenTypes.isEmpty());

    if (delegate != null) {
      delegate.pleaseAutoresizeWindow(this);
    }
  }

}
