package org.jetbrains.plugins.xml.searchandreplace.ui.predicatetypes;

import org.jetbrains.plugins.xml.searchandreplace.search.Node;
import org.jetbrains.plugins.xml.searchandreplace.search.Pattern;
import org.jetbrains.plugins.xml.searchandreplace.search.predicates.And;
import org.jetbrains.plugins.xml.searchandreplace.ui.PredicateType;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.PredicateTypeController;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.WithAttributeController;

import java.util.ArrayList;
import java.util.List;

public class WithAttribute extends PredicateType {


  public String toString() {
    return "With Attribute";
  }

  public PredicateTypeController createNewController() {
    return new WithAttributeController();
  }

  public void addNodeToPattern(Pattern p, Node node, Node parent) {
    if (parent != null) {
      And predicate = new And(parent.getPredicate(), node.getPredicate());
      parent.setPredicate(predicate);
    } else {
      p.getUnmatchedNodes().add(node);
    }
  }

  @Override
  public List<PredicateType> getAllowedChildrenTypes() {
    return new ArrayList<PredicateType>();
  }
}
