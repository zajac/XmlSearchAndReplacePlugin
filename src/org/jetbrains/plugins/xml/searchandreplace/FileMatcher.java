package org.jetbrains.plugins.xml.searchandreplace;

import com.intellij.lang.html.HTMLLanguage;
import com.intellij.lang.xml.XMLLanguage;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.xml.XmlFile;
import com.intellij.usageView.UsageInfo;
import com.intellij.usages.Usage;
import com.intellij.usages.UsageInfo2UsageAdapter;
import com.intellij.util.Processor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.plugins.xml.searchandreplace.search.Node;
import org.jetbrains.plugins.xml.searchandreplace.search.Pattern;
import org.jetbrains.plugins.xml.searchandreplace.search.TagSearchObserver;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


class FileMatcher implements Processor<VirtualFile>, TagSearchObserver {
  private final Project project;
  private final Pattern pattern;
  private final Set<PsiElement> foundTags;
  private final Map<Usage, Map<Node, PsiElement>> searchResults;
  private final Processor<Usage> usageProcessor;

  public FileMatcher(Project project, Pattern pattern, Map<Usage, Map<Node, PsiElement>> searchResults, Processor<Usage> usageProcessor) {
    this.project = project;
    this.pattern = pattern;
    foundTags = new HashSet<PsiElement>();
    this.searchResults = searchResults;
    this.usageProcessor = usageProcessor;
  }

  public boolean process(VirtualFile virtualFile) {
    PsiFile psiFile = PsiManager.getInstance(project).findFile(virtualFile);
    if (psiFile != null) {
      PsiFile xmlFile = psiFile.getViewProvider().getPsi(XMLLanguage.INSTANCE);
      if (xmlFile == null) {
        xmlFile = psiFile.getViewProvider().getPsi(HTMLLanguage.INSTANCE);
      }
      if (xmlFile != null) {
        pattern.match(xmlFile, this);
      } else {
        pattern.match(psiFile, this);
      }
      matchInjections(pattern, psiFile);
    }
    return true;
  }

  private void matchInjections(final Pattern pattern, PsiFile psiFile) {
    final FileMatcher fileMatcher = this;
    final Set<PsiFile> injections = new HashSet<PsiFile>();
    psiFile.accept(new PsiRecursiveElementVisitor() {
      @Override
      public void visitElement(final PsiElement element) {
        if (element instanceof PsiLanguageInjectionHost) {
          PsiLanguageInjectionHost host = (PsiLanguageInjectionHost) element;
          host.processInjectedPsi(new PsiLanguageInjectionHost.InjectedPsiVisitor() {
            @Override
            public void visit(@NotNull PsiFile psiFile, @NotNull List<PsiLanguageInjectionHost.Shred> shreds) {
              if (psiFile instanceof XmlFile) {
                pattern.match(psiFile, fileMatcher);
              }
            }
          });
        }
        super.visitElement(element);
      }
    });
  }

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
