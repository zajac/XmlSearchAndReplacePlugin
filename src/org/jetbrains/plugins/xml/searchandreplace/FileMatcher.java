package org.jetbrains.plugins.xml.searchandreplace;

import com.intellij.lang.html.HTMLLanguage;
import com.intellij.lang.xml.XMLLanguage;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.usageView.UsageInfo;
import com.intellij.usages.Usage;
import com.intellij.usages.UsageInfo2UsageAdapter;
import com.intellij.util.Processor;
import org.jetbrains.plugins.xml.searchandreplace.search.Pattern;

import java.util.Map;


class FileMatcher extends Matcher  {

  public FileMatcher(Map<Usage, SearchResult> searchResults, Pattern pattern, Project project, Processor<Usage> usageProcessor) {
    super(searchResults, pattern, project, usageProcessor);
  }

  @Override
  protected void process(PsiFile psiFile) {
    PsiFile xmlFile = psiFile.getViewProvider().getPsi(XMLLanguage.INSTANCE);
    if (xmlFile == null) {
      xmlFile = psiFile.getViewProvider().getPsi(HTMLLanguage.INSTANCE);
    }
    if (xmlFile != null) {
      pattern.match(xmlFile, this);
    } else {
      pattern.match(psiFile, this);
    }
  }

  public void elementFound(Pattern pattern, PsiElement tag) {
    if (!foundTags.contains(tag) && tag != null) {

      foundTags.add(tag);
      Usage usage = new UsageInfo2UsageAdapter(new UsageInfo(tag));
      searchResults.put(usage, new SearchResult(pattern.getMatchedNodes(), false));
      usageProcessor.process(usage);
    }
  }
}
