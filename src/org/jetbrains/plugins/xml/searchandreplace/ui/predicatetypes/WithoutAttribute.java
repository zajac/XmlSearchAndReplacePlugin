package org.jetbrains.plugins.xml.searchandreplace.ui.predicatetypes;

import org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.PredicateTypeController;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.WithoutAttributeController;

public class WithoutAttribute extends WithAttribute {
  public PredicateTypeController createNewController() {



    return new WithoutAttributeController(this);
  }

  public String toString() {
    return "Without attribute";
  }

}
