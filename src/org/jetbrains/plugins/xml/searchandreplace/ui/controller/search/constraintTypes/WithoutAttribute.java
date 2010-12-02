package org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.constraintTypes;

import com.intellij.openapi.project.Project;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.ConstraintTypeController;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.WithoutAttributeController;

public class WithoutAttribute extends WithAttribute {
  public WithoutAttribute(Project project) {
    super(project);
  }

  public ConstraintTypeController createNewController() {
    return new WithoutAttributeController(this, project);
  }

  public String toString() {
    return "Without attribute";
  }

}
