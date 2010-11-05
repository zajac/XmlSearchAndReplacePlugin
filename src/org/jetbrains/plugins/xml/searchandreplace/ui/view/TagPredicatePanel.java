package org.jetbrains.plugins.xml.searchandreplace.ui.view;

import javax.swing.*;
import java.awt.*;

public class TagPredicatePanel extends JPanel {

  public JTextField tagNameField;
  private JPanel pane;

  public TagPredicatePanel() {
    setLayout(new FlowLayout());
    add(pane);
  }

  public String getTagName() {
    return tagNameField.getText();
  }
}
