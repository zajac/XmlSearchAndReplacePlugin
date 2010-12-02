package org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.constraintTypes;

import com.intellij.openapi.project.Project;
import org.jetbrains.plugins.xml.searchandreplace.search.Node;
import org.jetbrains.plugins.xml.searchandreplace.search.Pattern;
import org.jetbrains.plugins.xml.searchandreplace.search.predicates.And;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.ConstraintType;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.ConstraintTypeController;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.DirectlyContainsController;

public class DirectlyContains extends ConstraintType {

  public DirectlyContains(Project project) {
    super(project);
  }

  @Override
  public ConstraintTypeController createNewController() {
    return new DirectlyContainsController(this, false, project);
  }

  @Override
  public String toString() {
    return "Directly contains";
  }

  @Override
  public Node addNodeToPattern(Pattern p, Node node, Node parent) {
    parent.setPredicate(new And(parent.getPredicate(), node.getPredicate()));
    node.setPredicate(((org.jetbrains.plugins.xml.searchandreplace.search.predicates.DirectlyContains)node.getPredicate()).getNested());
    parent.getChildren().add(node);
    p.getUnmatchedNodes().add(node);
    return node;
  }
}
