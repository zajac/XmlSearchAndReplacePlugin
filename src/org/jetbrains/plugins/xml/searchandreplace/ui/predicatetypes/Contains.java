package org.jetbrains.plugins.xml.searchandreplace.ui.predicatetypes;

import org.jetbrains.plugins.xml.searchandreplace.search.Pattern;
import org.jetbrains.plugins.xml.searchandreplace.ui.PredicateType;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.PredicateTypeController;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.TagPredicateController;

public class Contains extends PredicateType {

  protected enum Params {NOT}

  Params p = null;

  public Contains() {
  }

  protected Contains(Params p) {
    this.p = p;
  }

  public PredicateTypeController createNewController() {
    return new TagPredicateController();
  }

  public String toString() {
    return p == Params.NOT ? "Not contains" : "Contains";
  }

  public void addNodeToPattern(Pattern p, Pattern.Node node, Pattern.Node parent) {
    parent.getChildren().add(node);
    p.getAllNodes().add(node);
  }

}
