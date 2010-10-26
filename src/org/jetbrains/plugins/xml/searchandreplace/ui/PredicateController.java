package org.jetbrains.plugins.xml.searchandreplace.ui;

import org.jetbrains.plugins.xml.searchandreplace.search.Pattern;
import org.jetbrains.plugins.xml.searchandreplace.search.predicates.XmlElementPredicate;

import javax.swing.*;
import java.util.List;

public class PredicateController implements PredicatePanelDelegate {

    private PredicatePanel myView;
    private PredicateTypeController predicateTypeController;
    private PredicateType selectedPredicateType;
    private PredicateController parent;
    private Pattern.Node builtNode;

    public PredicateController(boolean canBeRoot, PredicateController parent) {
        this.parent = parent;
        myView = new PredicatePanel(canBeRoot);
        myView.setDelegate(this);
    }

    public JPanel getView() {
        return myView;
    }

    public void addChild(PredicatePanel panel) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public List<PredicateType> getPredicateTypes(PredicatePanel panel) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void predicateTypeSelected(PredicatePanel panel, PredicateType selection) {
        selectedPredicateType = selection;
        predicateTypeController = selection.createNewController();
        myView.setPredicateTypeSpecificView(predicateTypeController.getView());
    }

    public Pattern.Node getBuiltNode() {
        return builtNode;
    }

    public void buildNode(Pattern p) {
        XmlElementPredicate predicate = predicateTypeController.buildPredicate();
        if (predicate != null) {
            builtNode = new Pattern.Node(predicate, parent == null);
            Pattern.Node parentNode = parent == null ? null : parent.getBuiltNode();
            if (parent == null || parentNode != null) {
                selectedPredicateType.addNodeInPattern(p, builtNode, parentNode);
            }
        }
    }
}
