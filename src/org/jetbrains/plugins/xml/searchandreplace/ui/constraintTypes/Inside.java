package org.jetbrains.plugins.xml.searchandreplace.ui.constraintTypes;

import org.jetbrains.plugins.xml.searchandreplace.search.Node;
import org.jetbrains.plugins.xml.searchandreplace.search.Pattern;
import org.jetbrains.plugins.xml.searchandreplace.ui.ConstraintType;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.ConstraintTypeController;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.TagPredicateController;

public class Inside extends ConstraintType {

  protected enum Params {NOT}

  Params p = null;

  public Inside() {
  }

  protected Inside(Params p) {
    this.p = p;
  }

  @Override
  public String toString() {
    return p == Params.NOT ? "Not inside" : "Inside";
  }

  public ConstraintTypeController createNewController() {
    return new TagPredicateController(this, p == Params.NOT ? ConstraintTypeController.Params.NOT : null, true);
  }

  public Node addNodeToPattern(Pattern p, Node node, Node parent) {
    p.getUnmatchedNodes().add(node);
    node.getChildren().add(parent);
    return node;
  }

}
