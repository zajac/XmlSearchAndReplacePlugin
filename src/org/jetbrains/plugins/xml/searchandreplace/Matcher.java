package org.jetbrains.plugins.xml.searchandreplace;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.usageView.UsageInfo;
import com.intellij.usages.Usage;
import com.intellij.usages.UsageInfo2UsageAdapter;
import com.intellij.util.Processor;
import org.jetbrains.plugins.xml.searchandreplace.search.Node;
import org.jetbrains.plugins.xml.searchandreplace.search.Pattern;
import org.jetbrains.plugins.xml.searchandreplace.search.TagSearchObserver;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class Matcher implements Processor<VirtualFile>, TagSearchObserver {
  protected final Project project;
  protected final Pattern pattern;
  protected final Set<PsiElement> foundTags;
  protected final Map<Usage, Map<Node, PsiElement>> searchResults;
  protected final Processor<Usage> usageProcessor;

  public Matcher(Map<Usage, Map<Node, PsiElement>> searchResults, Pattern pattern, Project project, Processor<Usage> usageProcessor) {
    this.searchResults = searchResults;
    this.pattern = pattern;
    this.project = project;
    foundTags = new HashSet<PsiElement>();
    this.usageProcessor = usageProcessor;
  }

  public boolean process(VirtualFile virtualFile) {
    PsiFile psiFile = PsiManager.getInstance(project).findFile(virtualFile);
    if (psiFile != null) {
      process(psiFile);
    }
    return true;
  }

  protected abstract void process(PsiFile psiFile);

  public void elementFound(Pattern pattern, PsiElement tag) {
    if (!foundTags.contains(tag) && tag != null) {

      System.out.println(pattern.getMatchedNodes());
      System.out.println();

      foundTags.add(tag);
      Usage usage = new UsageInfo2UsageAdapter(new UsageInfo(tag));
      searchResults.put(usage, pattern.getMatchedNodes());
      usageProcessor.process(usage);
    }
  }
}
