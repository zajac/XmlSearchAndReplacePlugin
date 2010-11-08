package org.jetbrains.plugins.xml.searchandreplace.ui.controller.search;


import org.jetbrains.plugins.xml.searchandreplace.search.Pattern;
import org.jetbrains.plugins.xml.searchandreplace.ui.PredicateType;
import org.jetbrains.plugins.xml.searchandreplace.ui.PredicateTypeRegistry;
import org.jetbrains.plugins.xml.searchandreplace.ui.view.PatternView;

import java.util.*;

public class PatternController implements PredicateControllerDelegate {

  private PatternView view = new PatternView();
  private Map<PredicateController, List<PredicateController>> predicatesTree = new HashMap<PredicateController, List<PredicateController>>();
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
    Pattern pattern = new Pattern(new HashSet<Pattern.Node>());
    if (root != null) {
      gatherNodes(pattern, root);
    }

    pattern.validateNodes();
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
    addPredicateController(new PredicateController(true, predicateController));
  }

  public List<PredicateType> getAllowedPredicateTypes(PredicateController predicateController) {
    return PredicateTypeRegistry.getInstance().getPredicateTypes();
  }

  public void removeMe(PredicateController predicateController) {
    removePredicateController(predicateController);
  }

}
