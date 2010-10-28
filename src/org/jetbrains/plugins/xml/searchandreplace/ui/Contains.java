package org.jetbrains.plugins.xml.searchandreplace.ui;

import org.jetbrains.plugins.xml.searchandreplace.search.Pattern;

public class Contains implements PredicateType {

    protected enum Params {NOT}

    Params p = null;

    public Contains() {}

    protected Contains(Params p) {
        this.p = p;
    }

    public PredicateTypeController createNewController() {
        return new TagPredicateController();
    }

    public String toString() {
        return p == Params.NOT ? "Not contains" : "Contains";
    }

    public void addNodeToPattern(Pattern p, Pattern.Node node, Pattern.Node parent) {
        parent.getChildren().add(node);
        p.getAllNodes().add(node);
    }
}
