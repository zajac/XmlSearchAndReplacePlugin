package org.jetbrains.plugins.xml.searchandreplace.ui.constraintTypes;

import org.jetbrains.plugins.xml.searchandreplace.search.Node;
import org.jetbrains.plugins.xml.searchandreplace.search.Pattern;
import org.jetbrains.plugins.xml.searchandreplace.search.predicates.And;
import org.jetbrains.plugins.xml.searchandreplace.ui.ConstraintType;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.ConstraintTypeController;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.WithAttributeController;

public class WithAttribute extends ConstraintType {


  public String toString() {
    return "With Attribute";
  }

  public ConstraintTypeController createNewController() {
    return new WithAttributeController(this);
  }

  public Node addNodeToPattern(Pattern p, Node node, Node parent) {
    if (parent != null) {
      And predicate = new And(parent.getPredicate(), node.getPredicate());
      parent.setPredicate(predicate);
      return parent;
    } else {
      p.getUnmatchedNodes().add(node);
      return node;
    }
  }
}
