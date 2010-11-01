package org.jetbrains.plugins.xml.searchandreplace.ui;

import org.jetbrains.plugins.xml.searchandreplace.search.Pattern;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.PredicateTypeController;

public interface PredicateType {

    PredicateTypeController createNewController();

    void addNodeToPattern(Pattern p, Pattern.Node node, Pattern.Node parent);

}
