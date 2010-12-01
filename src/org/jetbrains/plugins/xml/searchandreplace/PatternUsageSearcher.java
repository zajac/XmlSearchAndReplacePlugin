package org.jetbrains.plugins.xml.searchandreplace;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.PsiSearchHelper;
import com.intellij.psi.xml.XmlElement;
import com.intellij.usages.Usage;
import com.intellij.usages.UsageSearcher;
import com.intellij.util.Processor;
import org.intellij.plugins.xpathView.search.SearchScope;
import org.jetbrains.plugins.xml.searchandreplace.search.Node;
import org.jetbrains.plugins.xml.searchandreplace.search.Pattern;
import org.jetbrains.plugins.xml.searchandreplace.search.PatternUtil;

import java.util.*;

class PatternUsageSearcher implements UsageSearcher {
  private final Pattern pattern;
  private final Project project;
  private final Map<Usage, Map<Node, XmlElement>> searchResults;
  private final SearchScope scope;

  public PatternUsageSearcher(Pattern pattern, Project project, Map<Usage, Map<Node, XmlElement>> searchResults, SearchScope scope) {
    this.pattern = pattern;
    this.project = project;
    this.searchResults = searchResults;
    this.scope = scope;
  }

  public void generate(final Processor<Usage> usageProcessor) {
    ApplicationManager.getApplication().runReadAction(new Runnable() {
      public void run() {
        List<String> searchHint = PatternUtil.getSearchHint(pattern);
        final FileMatcher fileMatcher = new FileMatcher(project, pattern, searchResults, usageProcessor);
        if (searchHint.isEmpty()) {
          scope.iterateContent(project, fileMatcher);
        } else {
          PsiSearchHelper searchHelper = PsiManager.getInstance(project).getSearchHelper();
          final ArrayList<VirtualFile> scopeFiles = new ArrayList<VirtualFile>();
          scope.iterateContent(project, new Processor<VirtualFile>() {
            @Override
            public boolean process(VirtualFile virtualFile) {
              scopeFiles.add(virtualFile);
              return true;
            }
          });
          Set<VirtualFile> filesForHint = new HashSet<VirtualFile>(scopeFiles);
          for (String hint : searchHint) {
            final Set<VirtualFile> filesWithWord = new HashSet<VirtualFile>();
            searchHelper.processAllFilesWithWordInText(hint, GlobalSearchScope.filesScope(project, filesForHint), new Processor<PsiFile>() {
              @Override
              public boolean process(PsiFile psiFile) {
                filesWithWord.add(psiFile.getVirtualFile());
                return true;
              }
            }, false);
            filesForHint.retainAll(filesWithWord);
          }
          for (VirtualFile file : filesForHint) {
            fileMatcher.process(file);
          }
        }
      }
    });
  }
}
