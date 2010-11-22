package org.jetbrains.plugins.xml.searchandreplace.ui.predicatetypes;

import org.jetbrains.plugins.xml.searchandreplace.search.Node;
import org.jetbrains.plugins.xml.searchandreplace.search.Pattern;
import org.jetbrains.plugins.xml.searchandreplace.search.predicates.And;
import org.jetbrains.plugins.xml.searchandreplace.search.predicates.XmlElementPredicate;
import org.jetbrains.plugins.xml.searchandreplace.ui.PredicateType;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.DirectlyContainsController;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.PredicateTypeController;

public class DirectlyContains extends PredicateType {
  @Override
  public PredicateTypeController createNewController() {
    return new DirectlyContainsController();
  }

  @Override
  public String toString() {
    return "Directly contains";
  }

  @Override
  public Node addNodeToPattern(Pattern p, Node node, Node parent) {
    XmlElementPredicate pred = parent.getPredicate();
    parent.setPredicate(new And(pred, node.getPredicate()));
    return parent;
  }
}
