package org.jetbrains.plugins.xml.searchandreplace.ui;


import org.jetbrains.plugins.xml.searchandreplace.search.Pattern;

import java.util.*;

public class PatternController implements PredicateControllerDelegate {

    private PatternView view = new PatternView();
    private Map<PredicateController, List<PredicateController>> predicatesTree = new HashMap<PredicateController, List<PredicateController>>();

    private void addPredicateController(PredicateController pc) {
        pc.setDelegate(this);
        predicatesTree.put(pc, new ArrayList<PredicateController>());
        PredicateController parent = pc.getParent();
        if (parent != null) {
            predicatesTree.get(parent).add(pc);
        }
        view.addPredicateView(pc.getView(), parent == null ? null : parent.getView());
    }

    public PatternView getView() {
        return view;
    }

    public PatternController() {
        PredicateController root = new PredicateController(true, null);
        addPredicateController(root);
    }

    public Pattern buildPattern() {
        Pattern pattern = new Pattern(new HashSet<Pattern.Node>());
        if (!predicatesTree.isEmpty()) {
            gatherNodes(pattern, predicatesTree.keySet().iterator().next());
        }
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
        addPredicateController(new PredicateController(false, predicateController));
    }

    public List<PredicateType> getAllowedPredicateTypes(PredicateController predicateController) {
        return PredicateTypeRegistry.getInstance().getPredicateTypes();
    }
}
