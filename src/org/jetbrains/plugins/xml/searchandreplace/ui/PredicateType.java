package org.jetbrains.plugins.xml.searchandreplace.ui;

import org.jetbrains.plugins.xml.searchandreplace.search.Node;
import org.jetbrains.plugins.xml.searchandreplace.search.Pattern;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.PredicateTypeController;

public abstract class PredicateType {

  public abstract PredicateTypeController createNewController();

  public abstract Node addNodeToPattern(Pattern p, Node node, Node parent);

}
