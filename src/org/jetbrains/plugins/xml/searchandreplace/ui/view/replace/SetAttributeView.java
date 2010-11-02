package org.jetbrains.plugins.xml.searchandreplace.ui.view.replace;

import javax.swing.*;


public class SetAttributeView extends JPanel {
  private JPanel centerPane;
  private JTextField nameField;
  private JTextField valueField;

  public SetAttributeView() {
    add(centerPane);
  }

  public String getName() {
    return nameField.getText();
  }

  public String getValue() {
    return valueField.getText();
  }

}
