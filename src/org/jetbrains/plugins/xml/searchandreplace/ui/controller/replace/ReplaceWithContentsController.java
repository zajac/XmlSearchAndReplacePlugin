package org.jetbrains.plugins.xml.searchandreplace.ui.controller.replace;

import com.intellij.lang.Language;
import com.intellij.openapi.project.Project;
import org.jetbrains.plugins.xml.searchandreplace.replace.ReplacementProvider;

/**
 * Created by IntelliJ IDEA.
 * User: zajac
 * Date: Nov 2, 2010
 * Time: 11:46:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class ReplaceWithContentsController extends CreatingXmlController {
  public ReplaceWithContentsController(Project project, Language language) {
    super(project, language);
  }

  @Override
  protected ReplacementProvider getReplacementProvider() {
    return createReplacementProviderWithMyXml();
  }

  @Override
  public String toString() {
    return "Replace with contents";
  }
}
