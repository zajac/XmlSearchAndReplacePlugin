package org.jetbrains.plugins.xml.searchandreplace.ui.controller.replace;

import com.intellij.lang.Language;
import com.intellij.openapi.project.Project;
import org.jetbrains.plugins.xml.searchandreplace.replace.ReplaceContentsOnly;
import org.jetbrains.plugins.xml.searchandreplace.replace.ReplacementProvider;

public class ReplaceContentsOnlyController extends CreatingXmlController {

  public ReplaceContentsOnlyController(Project project, Language language) {
    super(project, language);
  }

  @Override
  public String toString() {
    return "Replace contents only";
  }

  @Override
  protected ReplacementProvider getReplacementProvider() {
    ReplacementProvider replacementProviderWithMyXml = createReplacementProviderWithMyXml();
    if (replacementProviderWithMyXml == null) return null;
    return new ReplaceContentsOnly(replacementProviderWithMyXml);
  }
}
