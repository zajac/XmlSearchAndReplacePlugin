package org.jetbrains.plugins.xml.searchandreplace.ui.view;

import javax.swing.*;
import java.awt.*;

public class PatternView extends JPanel {

  public PatternView() {
    LayoutManager lm = new BoxLayout(this, BoxLayout.Y_AXIS);
    setLayout(lm);
  }

  public void addPredicateView(ConstraintPanel panel, ConstraintPanel parent) {
    if (parent == null) {
      add(panel);
    } else {
      parent.addChildPredicatePanel(panel);
    }
    updateUI();
  }

  public void removePredicateView(ConstraintPanel view, ConstraintPanel parentPanel) {
    if (parentPanel != null) {
      parentPanel.removeChildPredicatePanel(view);
    } else {
      view.getParent().remove(view);
    }
    updateUI();
  }
}
