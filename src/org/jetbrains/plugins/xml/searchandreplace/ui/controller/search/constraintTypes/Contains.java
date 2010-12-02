package org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.constraintTypes;

import com.intellij.openapi.project.Project;
import org.jetbrains.plugins.xml.searchandreplace.search.Node;
import org.jetbrains.plugins.xml.searchandreplace.search.Pattern;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.ConstraintType;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.ConstraintTypeController;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.TagOrTextConstraintController;

public class Contains extends ConstraintType {

  protected enum Params {NOT}

  Params p = null;

  public Contains(Project project) {
    super(project);
  }

  protected Contains(Params p, Project project) {
    super(project);
    this.p = p;
  }

  public ConstraintTypeController createNewController() {
    return new TagOrTextConstraintController(this, p == Params.NOT ? TagOrTextConstraintController.Params.NOT : null, false, project);
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
