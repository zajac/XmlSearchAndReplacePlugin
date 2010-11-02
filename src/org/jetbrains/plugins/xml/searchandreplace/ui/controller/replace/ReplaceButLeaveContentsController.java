package org.jetbrains.plugins.xml.searchandreplace.ui.controller.replace;


import com.intellij.lang.Language;
import com.intellij.openapi.project.Project;
import com.intellij.psi.xml.XmlElement;
import com.intellij.psi.xml.XmlTag;
import org.jetbrains.plugins.xml.searchandreplace.replace.ReplaceTagButLeaveContent;
import org.jetbrains.plugins.xml.searchandreplace.replace.ReplacementProvider;

public class ReplaceButLeaveContentsController extends CreatingXmlController {
  public ReplaceButLeaveContentsController(Project project, Language language) {
    super(project, language);
  }

  @Override
  protected ReplacementProvider getReplacementProvider() {
    XmlElement tag = getXml();
    if (tag instanceof XmlTag) {
      return new ReplaceTagButLeaveContent(createReplacementProviderWithMyXml());
    } else {
      return null;
    }
  }

  @Override
  public String toString() {
    return "Replace tag but leave contents";
  }
}
