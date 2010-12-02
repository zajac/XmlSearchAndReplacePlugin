package org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.constraintTypes;

import com.intellij.openapi.project.Project;
import org.jetbrains.plugins.xml.searchandreplace.search.Node;
import org.jetbrains.plugins.xml.searchandreplace.search.Pattern;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.ConstraintType;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.ConstraintTypeController;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.TagOrTextConstraintController;

public class RootConstraintType extends ConstraintType {

  public RootConstraintType(Project project) {
    super(project);
  }

  public ConstraintTypeController createNewController() {
    return new TagOrTextConstraintController(this, false, project);
  }

  public Node addNodeToPattern(Pattern p, Node node, Node parent) {
    p.getUnmatchedNodes().add(node);
    return node;
  }

}
