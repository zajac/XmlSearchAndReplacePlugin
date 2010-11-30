package org.jetbrains.plugins.xml.searchandreplace.ui.controller.search;

import org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.constraintTypes.*;

import java.util.ArrayList;
import java.util.List;


public class ConstraintTypesRegistry {

  private static ConstraintTypesRegistry ourInstance = new ConstraintTypesRegistry();

  private List<ConstraintType> constraintTypes = new ArrayList<ConstraintType>();

  public List<ConstraintType> getConstraintTypes() {
    return constraintTypes;
  }

  public static ConstraintTypesRegistry getInstance() {
    return ourInstance;
  }

  private ConstraintTypesRegistry() {
    registerPredicateType(new Inside());
    registerPredicateType(new NotInside());
    registerPredicateType(new Contains());
    registerPredicateType(new NotContains());
    registerPredicateType(new WithAttribute());
    registerPredicateType(new WithoutAttribute());
    registerPredicateType(new DirectlyContains());
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
