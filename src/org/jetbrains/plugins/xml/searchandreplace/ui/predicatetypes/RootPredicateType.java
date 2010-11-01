package org.jetbrains.plugins.xml.searchandreplace.ui.predicatetypes;

import org.jetbrains.plugins.xml.searchandreplace.search.Pattern;
import org.jetbrains.plugins.xml.searchandreplace.ui.PredicateType;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.PredicateTypeController;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.TagPredicateController;

public class RootPredicateType implements PredicateType {

    public PredicateTypeController createNewController() {
        return new TagPredicateController();
    }

    public void addNodeToPattern(Pattern p, Pattern.Node node, Pattern.Node parent) {
        p.getAllNodes().add(node);
    }
}
