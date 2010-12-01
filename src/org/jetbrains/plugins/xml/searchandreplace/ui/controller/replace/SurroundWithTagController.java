package org.jetbrains.plugins.xml.searchandreplace.ui.controller.replace;

import com.intellij.lang.Language;
import com.intellij.openapi.project.Project;
import org.jetbrains.plugins.xml.searchandreplace.replace.ReplacementProvider;
import org.jetbrains.plugins.xml.searchandreplace.replace.SurroundWithTag;

public class SurroundWithTagController extends CreatingXmlController {

  public SurroundWithTagController(Project project, Language language) {
    super(project, language);
  }

  @Override
  public String toString() {
    return "Surround with tag";
  }

  @Override
  protected ReplacementProvider getReplacementProvider() {
    ReplacementProvider replacementProviderWithMyXml = createReplacementProviderWithMyXml();
    if (replacementProviderWithMyXml == null) return null;
    return new SurroundWithTag(replacementProviderWithMyXml);
  }
}
