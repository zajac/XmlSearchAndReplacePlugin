package org.jetbrains.plugins.xml.searchandreplace.ui;

import org.jetbrains.plugins.xml.searchandreplace.search.Pattern;

public class Inside implements PredicateType {

    protected enum Params {NOT}

    Params p = null;

    public Inside() {}

    protected Inside(Params p) {
        this.p = p;
    }

    @Override
    public String toString() {
        return p == Params.NOT ? "Not inside" : "Inside";
    }

    public PredicateTypeController createNewController() {
        return new TagPredicateController(p == Params.NOT ? PredicateTypeController.Params.NOT : null);
    }

    public void addNodeToPattern(Pattern p, Pattern.Node node, Pattern.Node parent) {
        p.getAllNodes().add(node);
        node.getChildren().add(parent);
    }
}
