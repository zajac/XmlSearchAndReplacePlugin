package org.jetbrains.plugins.xml.searchandreplace.ui.controller.replace;

import com.intellij.lang.Language;
import com.intellij.openapi.project.Project;
import org.jetbrains.plugins.xml.searchandreplace.replace.InsertNearElement;
import org.jetbrains.plugins.xml.searchandreplace.replace.ReplacementProvider;

public class InsertNearElementController extends CreatingXmlController {

  private InsertNearElement.Anchor anchor;

  public InsertNearElementController(Project project, Language language, InsertNearElement.Anchor anchor) {
    super(project, language);
    this.anchor = anchor;
  }

  @Override
  public String toString() {
    return anchor == InsertNearElement.Anchor.AFTER ? "Insert after tag" : "Insert before tag";
  }

  @Override
  protected ReplacementProvider getReplacementProvider() {
    ReplacementProvider replacementProviderWithMyXml = createReplacementProviderWithMyXml();
    if (replacementProviderWithMyXml == null) return null;
    return new InsertNearElement(replacementProviderWithMyXml, anchor);
  }
}
