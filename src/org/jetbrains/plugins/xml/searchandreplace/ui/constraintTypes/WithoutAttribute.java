package org.jetbrains.plugins.xml.searchandreplace.ui.constraintTypes;

import org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.ConstraintTypeController;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.WithoutAttributeController;

public class WithoutAttribute extends WithAttribute {
  public ConstraintTypeController createNewController() {



    return new WithoutAttributeController(this);
  }

  public String toString() {
    return "Without attribute";
  }

}
