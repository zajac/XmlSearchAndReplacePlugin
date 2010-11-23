package org.jetbrains.plugins.xml.searchandreplace.ui.view;

import org.jetbrains.plugins.xml.searchandreplace.ui.ConstraintType;

import java.util.List;

public interface ConstraintPanelDelegate {
  void addChild(ConstraintPanel panel);

  List<ConstraintType> getChildrentConstraintTypes(ConstraintPanel panel);

  void constraintTypeSelected(ConstraintPanel panel, ConstraintType selection);

  void removeMe(ConstraintPanel panel);
}
