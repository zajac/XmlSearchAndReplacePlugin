package org.jetbrains.plugins.xml.searchandreplace.ui.controller.search;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.constraintTypes.*;

import java.util.ArrayList;
import java.util.List;


public class ConstraintTypesRegistry {

  private List<ConstraintType> constraintTypes = new ArrayList<ConstraintType>();
  private Project project;

  public List<ConstraintType> getConstraintTypes() {
    return constraintTypes;
  }

  public static ConstraintTypesRegistry getInstance(Project project) {
    return ServiceManager.getService(project, ConstraintTypesRegistry.class);
  }

  private ConstraintTypesRegistry(Project project) {
    this.project = project;
    registerPredicateType(new Inside(project));
    registerPredicateType(new NotInside(project));
    registerPredicateType(new Contains(project));
    registerPredicateType(new NotContains(project));
    registerPredicateType(new WithAttribute(project));
    registerPredicateType(new WithoutAttribute(project));
    registerPredicateType(new DirectlyContains(project));
    registerPredicateType(new RootConstraintType(project));
  }

  private ConstraintTypesRegistry() {

  }

  public void registerPredicateType(ConstraintType constraintType) {
    constraintTypes.add(constraintType);
  }

  public ConstraintType byClass(Class c) {
    for (ConstraintType pt : constraintTypes) {
      if (pt.getClass() == c) {
        return  pt;
      }
    }
    return null;
  }
}
