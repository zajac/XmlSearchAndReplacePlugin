package org.jetbrains.plugins.xml.searchandreplace.ui.controller.replace;

import com.intellij.lang.Language;
import com.intellij.openapi.project.Project;
import org.jetbrains.plugins.xml.searchandreplace.replace.InsertIntoTag;
import org.jetbrains.plugins.xml.searchandreplace.replace.ReplacementProvider;

public class InsertIntoTagController extends CreatingXmlController {

  private InsertIntoTag.Anchor anchor;

  public InsertIntoTagController(Project project, Language language, InsertIntoTag.Anchor anchor) {
    super(project, language);
    this.anchor = anchor;
  }

  @Override
  public String toString() {
    return anchor == InsertIntoTag.Anchor.BEGIN ? "Insert after begin" : "Insert before end";
  }

  @Override
  protected ReplacementProvider getReplacementProvider() {
    return new InsertIntoTag(createReplacementProviderWithMyXml(), anchor);
  }
}
