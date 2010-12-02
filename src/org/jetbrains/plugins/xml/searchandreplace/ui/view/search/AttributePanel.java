package org.jetbrains.plugins.xml.searchandreplace.ui.view.search;


import com.intellij.openapi.project.Project;
import com.intellij.ui.CollectionComboBoxModel;
import com.intellij.ui.EditorTextField;

import javax.swing.*;
import java.util.List;

public class AttributePanel extends JPanel {

  private JPanel centerPanel;
  private EditorTextField nameField;
  private JComboBox comparatorChooser;
  private EditorTextField valueField;
  private Project project;

  public AttributePanel(List comparators, Project project) {
    this.project = project;
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

  public void setAttrName(String attrName) {
    nameField.setText(attrName);
  }

  public void setValue(String value) {
    valueField.setText(value);
  }

  public void setSelectedComparator(Object comparator) {
    comparatorChooser.setSelectedItem(comparator);
  }

  private void createUIComponents() {
    nameField = Util.createRegexpEditor(project);
    valueField = Util.createRegexpEditor(project);
  }
}
