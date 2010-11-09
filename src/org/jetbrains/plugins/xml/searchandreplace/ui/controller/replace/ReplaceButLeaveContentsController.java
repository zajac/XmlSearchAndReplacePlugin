package org.jetbrains.plugins.xml.searchandreplace.ui.controller.replace;


import com.intellij.lang.Language;
import com.intellij.openapi.project.Project;
import org.jetbrains.plugins.xml.searchandreplace.replace.ReplaceTagButLeaveContent;
import org.jetbrains.plugins.xml.searchandreplace.replace.ReplacementProvider;

public class ReplaceButLeaveContentsController extends CreatingXmlController {
  public ReplaceButLeaveContentsController(Project project, Language language) {
    super(project, language);
  }

  @Override
  protected ReplacementProvider getReplacementProvider() {
    ReplacementProvider withMyXml = createReplacementProviderWithMyXml();
    if (withMyXml.alwaysReturnsTag()) {
      return new ReplaceTagButLeaveContent(withMyXml);
    } else {
      return null;
    }
  }

  @Override
  public String toString() {
    return "Replace tag but leave contents";
  }
}
