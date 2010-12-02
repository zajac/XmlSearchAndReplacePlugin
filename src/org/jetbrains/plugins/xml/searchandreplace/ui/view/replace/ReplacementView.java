package org.jetbrains.plugins.xml.searchandreplace.ui.view.replace;

import com.intellij.openapi.editor.impl.EditorImpl;
import com.intellij.openapi.fileTypes.PlainTextFileType;
import com.intellij.openapi.project.Project;
import com.intellij.ui.EditorTextField;

import javax.swing.*;

public class ReplacementView extends JPanel {

  public EditorTextField getTextField() {
    return textField;
  }

  private EditorTextField textField;
  private JPanel centerPane;
  private Project project;

  public MyEditorTextField.Delegate getDelegate() {
    return ((MyEditorTextField)textField).getDelegate();
  }

  public void setDelegate(MyEditorTextField.Delegate delegate) {
    ((MyEditorTextField)textField).setDelegate(delegate);
  }

  public ReplacementView(Project project) {
    this.project = project;
    add(centerPane);
  }

  public String getText() {
    return textField.getText();
  }

  private void createUIComponents() {
    textField = new MyEditorTextField("", project, PlainTextFileType.INSTANCE);
  }

  public EditorImpl getEditor() {
    return (EditorImpl)textField.getEditor();
  }

  public void setText(String xml) {
    textField.setText(xml);
  }
}
