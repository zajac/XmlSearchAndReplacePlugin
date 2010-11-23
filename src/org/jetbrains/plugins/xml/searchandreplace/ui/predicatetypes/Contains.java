package org.jetbrains.plugins.xml.searchandreplace.ui.predicatetypes;

import org.jetbrains.plugins.xml.searchandreplace.search.Node;
import org.jetbrains.plugins.xml.searchandreplace.search.Pattern;
import org.jetbrains.plugins.xml.searchandreplace.ui.ConstraintType;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.ConstraintTypeController;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.TagPredicateController;

public class Contains extends ConstraintType {

  protected enum Params {NOT}

  Params p = null;

  public Contains() {
  }

  protected Contains(Params p) {
    this.p = p;
  }

  public ConstraintTypeController createNewController() {
    return new TagPredicateController(this, p == Params.NOT ? TagPredicateController.Params.NOT : null, false);
  }

  public String toString() {
    return p == Params.NOT ? "Not contains" : "Contains";
  }

  public Node addNodeToPattern(Pattern p, Node node, Node parent) {
    parent.getChildren().add(node);
    p.getUnmatchedNodes().add(node);
    return node;
  }

}
