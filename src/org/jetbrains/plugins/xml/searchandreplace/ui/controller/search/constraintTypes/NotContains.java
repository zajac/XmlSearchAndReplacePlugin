package org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.constraintTypes;


import com.intellij.openapi.project.Project;
import org.jetbrains.plugins.xml.searchandreplace.search.Node;
import org.jetbrains.plugins.xml.searchandreplace.search.Pattern;

public class NotContains extends Contains {
  public NotContains(Project project) {
    super(Params.NOT, project);
  }


  @Override
  public Node addNodeToPattern(Pattern p, Node node, Node parent) {
    return super.addNodeToPattern(p, node, parent);
  }
}
