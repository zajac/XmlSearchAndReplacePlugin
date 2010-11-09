package org.jetbrains.plugins.xml.searchandreplace.replace;

import com.intellij.lang.Language;
import com.intellij.openapi.project.Project;
import com.intellij.psi.XmlElementFactory;
import com.intellij.psi.xml.XmlElement;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.IncorrectOperationException;

public class ReplacementProviderForXml extends ReplacementProvider {
  private String myText;
  private Project myProject;
  private Language myLanguage;
  boolean isTag;

  public ReplacementProviderForXml(String text, Project project, Language language) {
    myText = text;
    myProject = project;
    myLanguage = language;
    isTag = createMyXml() instanceof XmlTag;
  }

  @Override
  public XmlElement getReplacementFor(XmlElement element) {
    return createMyXml();
  }

  private XmlElement createMyXml() {
    XmlElement result;
    XmlElementFactory factory = XmlElementFactory.getInstance(myProject);
    if (myText != null && factory != null) {
      try {
        result = factory.createTagFromText(myText, myLanguage);
        if (((XmlTag)result).getName().isEmpty()) {
          result = factory.createDisplayText(myText);
        }
      } catch (IncorrectOperationException e) {
        try {
          result = factory.createDisplayText(myText);
        } catch (IncorrectOperationException ex) {
          result = null;
        }
      }
      return result;
    } else {
      return null;
    }
  }

  @Override
  public boolean alwaysReturnsTag() {
    return isTag;
  }
}