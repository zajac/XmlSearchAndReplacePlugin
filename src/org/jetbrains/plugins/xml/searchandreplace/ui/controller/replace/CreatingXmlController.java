package org.jetbrains.plugins.xml.searchandreplace.ui.controller.replace;

import com.intellij.lang.Language;
import com.intellij.openapi.project.Project;
import org.jetbrains.plugins.xml.searchandreplace.replace.ReplacementProvider;
import org.jetbrains.plugins.xml.searchandreplace.replace.ReplacementProviderForXml;
import org.jetbrains.plugins.xml.searchandreplace.ui.view.replace.ReplacementView;

import javax.swing.*;

public abstract class CreatingXmlController extends ReplacementController {

  private ReplacementView myView;
  private Project myProject;
  private Language myLanguage;

  public CreatingXmlController(Project project, Language language) {
    myProject = project;
    myLanguage = language;
    myView = new ReplacementView(project);
  }

  @Override
  public JPanel getView() {
    return myView;
  }

  protected ReplacementProvider createReplacementProviderWithMyXml() {
    return new ReplacementProviderForXml(myView.getText(), myProject, myLanguage);
  }
}
