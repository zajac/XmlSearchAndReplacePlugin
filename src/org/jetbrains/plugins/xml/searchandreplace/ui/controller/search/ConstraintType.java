package org.jetbrains.plugins.xml.searchandreplace.ui.controller.search;

import com.intellij.openapi.project.Project;
import org.jetbrains.plugins.xml.searchandreplace.search.Node;
import org.jetbrains.plugins.xml.searchandreplace.search.Pattern;

public abstract class ConstraintType {

  protected Project project;

  protected ConstraintType(Project project) {
    this.project = project;
  }

  public abstract ConstraintTypeController createNewController();

  public abstract Node addNodeToPattern(Pattern p, Node node, Node parent);

}
