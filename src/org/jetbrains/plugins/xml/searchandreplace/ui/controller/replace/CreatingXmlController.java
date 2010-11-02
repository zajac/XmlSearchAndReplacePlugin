package org.jetbrains.plugins.xml.searchandreplace.ui.controller.replace;

import com.intellij.lang.Language;
import com.intellij.openapi.project.Project;
import com.intellij.psi.XmlElementFactory;
import com.intellij.psi.xml.XmlElement;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.plugins.xml.searchandreplace.replace.ReplacementProvider;
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

  protected XmlElement getXml() {
    XmlElement result;
    XmlElementFactory factory = XmlElementFactory.getInstance(myProject);
    String text = myView.getText();
    if (text == null || factory == null) {
      return null;
    }
    try {
      result = factory.createTagFromText(text, myLanguage);
    } catch (IncorrectOperationException e) {
      try {
        result = factory.createDisplayText(text);
      } catch (IncorrectOperationException ex) {
        result = null;
      }
    }
    return result;
  }

  @Override
  public JPanel getView() {
    return myView;
  }

  protected ReplacementProvider createReplacementProviderWithMyXml() {
    return new ReplacementProvider() {
      @Override
      public XmlElement getReplacementFor(XmlElement element) {
        return getXml();
      }
    };
  }
}
