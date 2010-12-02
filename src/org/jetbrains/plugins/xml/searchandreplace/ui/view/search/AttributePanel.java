package org.jetbrains.plugins.xml.searchandreplace.ui.view.search;


import com.intellij.openapi.project.Project;
import com.intellij.ui.CollectionComboBoxModel;
import com.intellij.ui.EditorTextField;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.WithAttributeController;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;

public class AttributePanel extends JPanel {

  private JPanel centerPanel;
  private EditorTextField nameField;
  private JComboBox comparatorChooser;
  private EditorTextField valueField;
  private Project project;

  public AttributePanel(List comparators, final Project project) {
    this.project = project;
    if (comparators == null) {
      valueField.setVisible(false);
      comparatorChooser.setVisible(false);
    } else {
      comparatorChooser.setModel(new CollectionComboBoxModel(comparators, null));
    }

    comparatorChooser.addItemListener(new ItemListener() {
      @Override
      public void itemStateChanged(ItemEvent e) {
        if (e.getItem() instanceof WithAttributeController.Comparator) {
          if (((WithAttributeController.Comparator) e.getItem()).name().equals("matches")) {
            Util.useRegexps(valueField, project, true);
          } else {
            Util.useRegexps(valueField, project, false);
          }
        }
      }
    });
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

  public void setSelectedComparator(WithAttributeController.Comparator comparator) {
    comparatorChooser.setSelectedItem(comparator);
    if (comparator.name().equals("matches")) {
      Util.useRegexps(valueField, project, true);
    } else {
      Util.useRegexps(valueField, project, false);
    }
  }

  private void createUIComponents() {
    nameField = Util.createRegexpEditor(project, false);
    valueField = Util.createRegexpEditor(project, false);
  }

  public void useRegexps(boolean b) {
    Util.useRegexps(nameField, project, b);
  }
}
