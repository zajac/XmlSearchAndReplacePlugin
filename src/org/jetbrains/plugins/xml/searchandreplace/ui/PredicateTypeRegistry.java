package org.jetbrains.plugins.xml.searchandreplace.ui;

import org.jetbrains.plugins.xml.searchandreplace.ui.predicatetypes.*;

import java.util.ArrayList;
import java.util.List;


public class PredicateTypeRegistry {

  private static PredicateTypeRegistry ourInstance = new PredicateTypeRegistry();

  private List<PredicateType> predicateTypes = new ArrayList<PredicateType>();

  public List<PredicateType> getPredicateTypes() {
    return predicateTypes;
  }

  public static PredicateTypeRegistry getInstance() {
    return ourInstance;
  }

  private PredicateTypeRegistry() {
    registerPredicateType(new Inside());
    registerPredicateType(new NotInside());
    registerPredicateType(new Contains());
    registerPredicateType(new NotContains());
    registerPredicateType(new WithAttribute());
    registerPredicateType(new WithoutAttribute());
    registerPredicateType(new DirectlyContains());
  }

  public void registerPredicateType(PredicateType predicateType) {
    predicateTypes.add(predicateType);
  }
}
