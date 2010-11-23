package org.jetbrains.plugins.xml.searchandreplace.ui.constraintTypes;

import org.jetbrains.plugins.xml.searchandreplace.search.Node;
import org.jetbrains.plugins.xml.searchandreplace.search.Pattern;
import org.jetbrains.plugins.xml.searchandreplace.search.predicates.And;
import org.jetbrains.plugins.xml.searchandreplace.search.predicates.XmlElementPredicate;
import org.jetbrains.plugins.xml.searchandreplace.ui.ConstraintType;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.ConstraintTypeController;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.DirectlyContainsController;

public class DirectlyContains extends ConstraintType {
  @Override
  public ConstraintTypeController createNewController() {
    return new DirectlyContainsController(this, false);
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
