package org.jetbrains.plugins.xml.searchandreplace.ui;

import org.jetbrains.plugins.xml.searchandreplace.search.Pattern;

public class RootPredicateType implements PredicateType {

    public PredicateTypeController createNewController() {
        return new TagPredicateController();
    }

    public void addNodeToPattern(Pattern p, Pattern.Node node, Pattern.Node parent) {
        p.getAllNodes().add(node);
    }
}
