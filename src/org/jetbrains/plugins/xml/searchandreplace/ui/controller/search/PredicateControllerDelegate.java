package org.jetbrains.plugins.xml.searchandreplace.ui.controller.search;

import org.jetbrains.plugins.xml.searchandreplace.ui.PredicateType;

import java.util.List;

public interface PredicateControllerDelegate {
  void addChild(PredicateController predicateController);

  List<PredicateType> getAllowedPredicateTypes(PredicateController predicateController);

  void removeMe(PredicateController predicateController);

  void validateMe(PredicateController predicateController);
}
