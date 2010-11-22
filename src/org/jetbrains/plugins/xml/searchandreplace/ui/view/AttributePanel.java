package org.jetbrains.plugins.xml.searchandreplace.ui.view;


import com.intellij.ui.CollectionComboBoxModel;

import javax.swing.*;
import java.util.List;

public class AttributePanel extends JPanel {

  private JPanel centerPanel;
  private JTextField nameField;
  private JComboBox comparatorChooser;
  private JTextField valueField;

  public AttributePanel(List comparators) {
    if (comparators == null) {
      valueField.setVisible(false);
      comparatorChooser.setVisible(false);
    } else {
      comparatorChooser.setModel(new CollectionComboBoxModel(comparators, null));
    }
    add(centerPanel);

  }

  public String getAttrName() {
    return nameField.getText();
  }

  public String getValue() {
    return valueField.getText();
  }

  public Object selectedComparator() {
    return comparatorChooser.getSelectedItem();
  }
}
