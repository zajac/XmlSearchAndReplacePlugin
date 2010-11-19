package org.jetbrains.plugins.xml.searchandreplace.ui.controller.replace;

import com.intellij.lang.Language;
import com.intellij.openapi.editor.RangeMarker;
import com.intellij.openapi.editor.impl.EditorImpl;
import com.intellij.openapi.project.Project;
import org.jetbrains.plugins.xml.searchandreplace.replace.ReplacementProvider;
import org.jetbrains.plugins.xml.searchandreplace.ui.view.replace.ReplacementView;

import javax.swing.*;

public abstract class CreatingXmlController extends ReplacementController implements CaptureDropHandler.CaptureDropHandlerDelegate {

  CapturedReplacementController nested;

  private ReplacementView myView;
  private Project myProject;
  private Language myLanguage;

  public CreatingXmlController(Project project, Language language) {
    nested = new CapturedReplacementController(language, project);
    myProject = project;
    myLanguage = language;
    myView = new ReplacementView(project);
  }

  @Override
  public JPanel getView() {
    return myView;
  }

  @Override
  public void viewDidAppear() {
    final EditorImpl editor = myView.getEditor();
    CaptureDropHandler dropHandler = new CaptureDropHandler(editor);
    dropHandler.setDelegate(this);
    editor.setDropHandler(dropHandler);
    nested.setEditor(editor);
  }

  protected ReplacementProvider createReplacementProviderWithMyXml() {
    return nested.getReplacementProvider();
  }

  @Override
  public void newCaptureInserted(Capture capture, RangeMarker where) {
    nested.addCaptureEntry(capture, where);
  }
}
