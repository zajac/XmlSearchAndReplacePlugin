package org.jetbrains.plugins.xml.searchandreplace.ui;

import org.jetbrains.plugins.xml.searchandreplace.search.Pattern;

public interface PredicateType {

    PredicateTypeController createNewController();

    void addNodeToPattern(Pattern p, Pattern.Node node, Pattern.Node parent);

}
