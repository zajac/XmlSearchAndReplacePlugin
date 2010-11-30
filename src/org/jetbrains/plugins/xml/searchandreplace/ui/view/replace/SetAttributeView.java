package org.jetbrains.plugins.xml.searchandreplace.ui.view.replace;

import com.intellij.openapi.editor.impl.EditorImpl;
import com.intellij.openapi.fileTypes.PlainTextFileType;
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

  public MyEditorTextField.Delegate getDelegate() {
    return ((MyEditorTextField)nameField).getDelegate();
  }

  public void setDelegate(MyEditorTextField.Delegate delegate) {
    ((MyEditorTextField)nameField).setDelegate(delegate);
    
  }


  public EditorImpl getNameEditor() {
    return (EditorImpl) nameField.getEditor();
  }

  public EditorImpl getValueEditor() {
    return (EditorImpl) valueField.getEditor();
  }

  private void createUIComponents() {
    nameField = new MyEditorTextField("", null, PlainTextFileType.INSTANCE);
    valueField = new MyEditorTextField("", null, PlainTextFileType.INSTANCE);
  }

  public MyEditorTextField getNameField() {
    return (MyEditorTextField) nameField;
  }

  public MyEditorTextField getValueField() {
    return (MyEditorTextField) valueField;
  }
}
