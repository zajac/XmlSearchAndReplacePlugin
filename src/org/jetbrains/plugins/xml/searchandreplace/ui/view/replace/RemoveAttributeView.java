package org.jetbrains.plugins.xml.searchandreplace.ui.view.replace;

import javax.swing.*;


public class RemoveAttributeView extends JPanel {
  private JTextField nameField;
  private JPanel centerPane;

  public RemoveAttributeView() {
    add(centerPane);
  }

  public String getName() {
    return nameField.getText();
  }
}
