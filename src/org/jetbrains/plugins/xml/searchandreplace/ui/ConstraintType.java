package org.jetbrains.plugins.xml.searchandreplace.ui;

import org.jetbrains.plugins.xml.searchandreplace.search.Node;
import org.jetbrains.plugins.xml.searchandreplace.search.Pattern;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.ConstraintTypeController;

public abstract class ConstraintType {

  public abstract ConstraintTypeController createNewController();

  public abstract Node addNodeToPattern(Pattern p, Node node, Node parent);

}
