package org.jetbrains.plugins.xml.searchandreplace.ui.constraintTypes;

import org.jetbrains.plugins.xml.searchandreplace.search.Node;
import org.jetbrains.plugins.xml.searchandreplace.search.Pattern;
import org.jetbrains.plugins.xml.searchandreplace.ui.ConstraintType;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.ConstraintTypeController;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.TagPredicateController;

public class RootConstraintType extends ConstraintType {

  public ConstraintTypeController createNewController() {
    return new TagPredicateController(this, false);
  }

  public Node addNodeToPattern(Pattern p, Node node, Node parent) {
    p.getUnmatchedNodes().add(node);
    return node;
  }

}
