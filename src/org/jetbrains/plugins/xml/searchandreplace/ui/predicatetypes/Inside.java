package org.jetbrains.plugins.xml.searchandreplace.ui.predicatetypes;

import org.jetbrains.plugins.xml.searchandreplace.search.Node;
import org.jetbrains.plugins.xml.searchandreplace.search.Pattern;
import org.jetbrains.plugins.xml.searchandreplace.ui.PredicateType;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.PredicateTypeController;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.TagPredicateController;

public class Inside extends PredicateType {

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

  public PredicateTypeController createNewController() {
    return new TagPredicateController(p == Params.NOT ? PredicateTypeController.Params.NOT : null);
  }

  public Node addNodeToPattern(Pattern p, Node node, Node parent) {
    p.getUnmatchedNodes().add(node);
    node.getChildren().add(parent);
    return node;
  }

}
