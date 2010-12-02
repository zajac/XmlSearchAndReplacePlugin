package org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.constraintTypes;

import com.intellij.openapi.project.Project;
import org.jetbrains.plugins.xml.searchandreplace.search.Node;
import org.jetbrains.plugins.xml.searchandreplace.search.Pattern;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.ConstraintType;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.ConstraintTypeController;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.TagOrTextConstraintController;

public class Inside extends ConstraintType {

  private Project project;

  protected enum Params {NOT}

  Params p = null;

  public Inside(Project project) {
    super(project);
  }

  protected Inside(Params p, Project project) {
    super(project);
    this.p = p;
  }

  @Override
  public String toString() {
    return p == Params.NOT ? "Not inside" : "Inside";
  }

  public ConstraintTypeController createNewController() {
    return new TagOrTextConstraintController(this, p == Params.NOT ? ConstraintTypeController.Params.NOT : null, true, project);
  }

  public Node addNodeToPattern(Pattern p, Node node, Node parent) {
    p.getUnmatchedNodes().add(node);
    node.getChildren().add(parent);
    return node;
  }

}
