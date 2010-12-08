package org.jetbrains.plugins.xml.searchandreplace;


import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.xml.XmlFile;
import com.intellij.usageView.UsageInfo;
import com.intellij.usages.Usage;
import com.intellij.usages.UsageInfo2UsageAdapter;
import com.intellij.util.Processor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.plugins.xml.searchandreplace.search.Pattern;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class InjectionsMatcher extends Matcher {

  public InjectionsMatcher(Map<Usage, SearchResult> searchResults, Pattern pattern, Project project, Processor<Usage> usageProcessor) {
    super(searchResults, pattern, project, usageProcessor);
  }

  @Override
  protected void process(PsiFile psiFile) {
    final InjectionsMatcher injectionsMatcher = this;
    final Set<PsiFile> injections = new HashSet<PsiFile>();
    psiFile.accept(new PsiRecursiveElementVisitor() {
      @Override
      public void visitElement(final PsiElement element) {
        final PsiRecursiveElementVisitor thisPsiVisitor = this;
        if (element instanceof PsiLanguageInjectionHost) {
          PsiLanguageInjectionHost host = (PsiLanguageInjectionHost) element;
          host.processInjectedPsi(new PsiLanguageInjectionHost.InjectedPsiVisitor() {
            @Override
            public void visit(@NotNull PsiFile psiFile11, @NotNull List<PsiLanguageInjectionHost.Shred> shreds) {
              if (psiFile11 instanceof XmlFile) {
                pattern.match(psiFile11, injectionsMatcher);
              }
              psiFile11.accept(thisPsiVisitor);
            }
          });
        }
        super.visitElement(element);
      }
    });
  }

  public void elementFound(Pattern pattern, PsiElement tag) {
    if (!foundTags.contains(tag) && tag != null) {

      foundTags.add(tag);
      Usage usage = new UsageInfo2UsageAdapter(new UsageInfo(tag));
      searchResults.put(usage, new SearchResult(pattern.getMatchedNodes(), true));
      usageProcessor.process(usage);
    }
  }
}
