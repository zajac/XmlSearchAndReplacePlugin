package org.jetbrains.plugins.xml.searchandreplace.ui.controller.search;

import org.jetbrains.plugins.xml.searchandreplace.ui.ConstraintType;

import java.util.List;

public interface PredicateControllerDelegate {
  void addChild(ConstraintController constraintController);

  List<ConstraintType> getAllowedPredicateTypes(ConstraintController constraintController);

  void removeMe(ConstraintController constraintController);

  void validateMe(ConstraintController constraintController);
}
