package org.jetbrains.plugins.xml.searchandreplace.ui.view.replace;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.ui.EditorTextField;
import org.jetbrains.annotations.NotNull;

/**
* Created by IntelliJ IDEA.
* User: zajac
* Date: 30.11.10
* Time: 18:15
* To change this template use File | Settings | File Templates.
*/
public class MyEditorTextField extends EditorTextField {

  public interface Delegate {
    void viewDidAppear();
  }

  public Delegate getDelegate() {
    return delegate;
  }

  public void setDelegate(Delegate delegate) {
    this.delegate = delegate;
  }

  private Delegate delegate;

  MyEditorTextField() {
  }

  MyEditorTextField(@NotNull String text) {
    super(text);
  }

  MyEditorTextField(@NotNull String text, Project project, FileType fileType) {
    super(text, project, fileType);
  }

  MyEditorTextField(Document document, Project project, FileType fileType) {
    super(document, project, fileType);
  }

  MyEditorTextField(Document document, Project project, FileType fileType, boolean isViewer) {
    super(document, project, fileType, isViewer);
  }

  MyEditorTextField(Document document, Project project, FileType fileType, boolean isViewer, boolean oneLineMode) {
    super(document, project, fileType, isViewer, oneLineMode);
  }

  @Override
  public void addNotify() {
    super.addNotify();    //To change body of overridden methods use File | Settings | File Templates.
    if (delegate != null) {
      delegate.viewDidAppear();
    }
  }
}
