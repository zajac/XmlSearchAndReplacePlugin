package org.jetbrains.plugins.xml.searchandreplace.ui.controller.search;


import org.jetbrains.plugins.xml.searchandreplace.search.Node;
import org.jetbrains.plugins.xml.searchandreplace.search.Pattern;
import org.jetbrains.plugins.xml.searchandreplace.ui.PredicateType;
import org.jetbrains.plugins.xml.searchandreplace.ui.PredicateTypeRegistry;
import org.jetbrains.plugins.xml.searchandreplace.ui.view.PatternView;

import java.util.*;

public class PatternController implements PredicateControllerDelegate {

  private PatternView view = new PatternView();
  private Map<PredicateController, ArrayList<PredicateController>> predicatesTree = new HashMap<PredicateController, ArrayList<PredicateController>>();
  private PredicateController root;

  private void addPredicateController(PredicateController pc) {
    pc.setDelegate(this);
    predicatesTree.put(pc, new ArrayList<PredicateController>());
    PredicateController parent = pc.getParent();
    if (parent != null) {
      predicatesTree.get(parent).add(pc);
    }
    view.addPredicateView(pc.getView(), parent == null ? null : parent.getView());
  }

  private void removePredicateController(PredicateController predicateController) {
    List<PredicateController> pcList = new ArrayList<PredicateController>();
    for (PredicateController pc : predicatesTree.get(predicateController)) {
      pcList.add(pc);
    }
    for (PredicateController pc : pcList) {
      removePredicateController(pc);
    }
    predicatesTree.get(predicateController.getParent()).remove(predicateController);
    predicatesTree.remove(predicateController);
    view.removePredicateView(predicateController.getView());
  }

  public PatternView getView() {
    return view;
  }

  public PatternController() {
    root = new PredicateController(true, null);
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

  private void gatherNodes(Pattern pattern, PredicateController root) {
    root.buildNode(pattern);
    List<PredicateController> children = predicatesTree.get(root);
    if (children != null) {
      for (PredicateController child : children) {
        gatherNodes(pattern, child);
      }
    }
  }

  public void addChild(PredicateController predicateController) {
    PredicateType selectedPredicateType = predicateController.getSelectedPredicateType();
    if (selectedPredicateType == null) return;

    List<PredicateType> allowedChildrenTypes = selectedPredicateType.getAllowedChildrenTypes();
    if (!allowedChildrenTypes.isEmpty()) {
      addPredicateController(new PredicateController(false, predicateController));
    }
  }

  public List<PredicateType> getAllowedPredicateTypes(PredicateController predicateController) {
    PredicateController parent = predicateController.getParent();
    if (parent == null) {
      return PredicateTypeRegistry.getInstance().getPredicateTypes();
    }
    PredicateType selectedPredicateType = parent.getSelectedPredicateType();
    if (selectedPredicateType != null) {
      return selectedPredicateType.getAllowedChildrenTypes();
    } else {
      return new ArrayList<PredicateType>();
    }
  }

  public void removeMe(PredicateController predicateController) {
    removePredicateController(predicateController);
  }

  public void validateMe(PredicateController predicateController) {
    PredicateType selectedPredicateType = predicateController.getSelectedPredicateType();

    List<PredicateType> allowedChildrenTypes = selectedPredicateType == null ? new ArrayList<PredicateType>() : selectedPredicateType.getAllowedChildrenTypes();
    List<PredicateController> children = (List<PredicateController>) predicatesTree.get(predicateController).clone();
    for (PredicateController child : children) {
      if (!allowedChildrenTypes.contains(child.getSelectedPredicateType())) {
        removePredicateController(child);
      }
    }
    predicateController.setCanHaveChildren(!allowedChildrenTypes.isEmpty());
  }

}
