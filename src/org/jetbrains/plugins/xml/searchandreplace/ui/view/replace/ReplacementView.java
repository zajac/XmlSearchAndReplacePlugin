package org.jetbrains.plugins.xml.searchandreplace.ui.view.replace;

import com.intellij.ide.highlighter.XmlFileType;
import com.intellij.openapi.project.Project;
import com.intellij.ui.EditorTextField;

import javax.swing.*;

public class ReplacementView extends JPanel {
  EditorTextField textField;
  private JPanel centerPane;
  private Project project;

  public ReplacementView(Project project) {
    this.project = project;
    add(centerPane);
  }

  public String getText() {
    return textField.getText();
  }

  private void createUIComponents() {
    textField = new EditorTextField("", project, XmlFileType.INSTANCE);
  }
}
