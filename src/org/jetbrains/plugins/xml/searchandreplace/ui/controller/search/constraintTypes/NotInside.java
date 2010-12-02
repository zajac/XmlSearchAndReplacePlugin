package org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.constraintTypes;

import com.intellij.openapi.project.Project;

public class NotInside extends Inside {
  public NotInside(Project project) {
    super(Params.NOT, project);
  }

}
