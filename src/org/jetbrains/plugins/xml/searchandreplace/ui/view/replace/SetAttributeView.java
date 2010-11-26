package org.jetbrains.plugins.xml.searchandreplace.ui.view.replace;

import com.intellij.openapi.editor.impl.EditorImpl;
import com.intellij.ui.EditorTextField;

import javax.swing.*;


public class SetAttributeView extends JPanel {
  private JPanel centerPane;
  private EditorTextField nameField;
  private EditorTextField valueField;

  public SetAttributeView() {
    add(centerPane);
  }

  public SetAttributeView(boolean hideValue) {
    add(centerPane);
    valueField.setVisible(!hideValue);
    centerPane.updateUI();
  }

  public EditorImpl getNameEditor() {
    return (EditorImpl) nameField.getEditor();
  }

  public EditorImpl getValueEditor() {
    return (EditorImpl) valueField.getEditor();
  }
}
