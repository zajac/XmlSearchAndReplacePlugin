package org.jetbrains.plugins.xml.searchandreplace.ui.view;

import javax.swing.*;
import java.awt.*;

public class PatternView extends JPanel {

  public PatternView() {
    LayoutManager lm = new BoxLayout(this, BoxLayout.Y_AXIS);
    setLayout(lm);
  }

  public void addPredicateView(PredicatePanel panel, PredicatePanel parent) {
    add(panel, null);
    panel.updateUI();
  }
}
