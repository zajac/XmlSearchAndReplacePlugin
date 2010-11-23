package org.jetbrains.plugins.xml.searchandreplace.ui.view;

import org.jetbrains.plugins.xml.searchandreplace.ui.ConstraintType;

import java.util.List;

public interface PredicatePanelDelegate {
  void addChild(PredicatePanel panel);

  List<ConstraintType> getPredicateTypes(PredicatePanel panel);

  void predicateTypeSelected(PredicatePanel panel, ConstraintType selection);

  void removeMe(PredicatePanel panel);
}
