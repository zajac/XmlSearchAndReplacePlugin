package org.jetbrains.plugins.xml.searchandreplace;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ContentIterator;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.PsiSearchHelper;
import com.intellij.psi.search.SearchScope;
import com.intellij.usages.Usage;
import com.intellij.usages.UsageSearcher;
import com.intellij.util.Processor;
import org.jetbrains.plugins.xml.searchandreplace.search.Pattern;
import org.jetbrains.plugins.xml.searchandreplace.search.PatternUtil;

import java.util.*;

class PatternUsageSearcher implements UsageSearcher {
  private final Pattern pattern;
  private final Project project;
  private final Map<Usage, SearchResult> searchResults;
  private final SearchScope scope;

  public PatternUsageSearcher(Pattern pattern, Project project, Map<Usage, SearchResult> searchResults, SearchScope scope) {
    this.pattern = pattern;
    this.project = project;
    this.searchResults = searchResults;
    this.scope = scope;
  }

  public void generate(final Processor<Usage> usageProcessor) {
    List<String> searchHint = PatternUtil.getSearchHint(pattern);
    Set<VirtualFile> files = filesContainingAllWords(project, scope, searchHint);
    searchWithMatcher(new FileMatcher(searchResults, pattern, project, usageProcessor), files);
    searchWithMatcher(new InjectionsMatcher(searchResults, pattern, project, usageProcessor), files);
  }

  private void searchWithMatcher(final Matcher matcher, Set<VirtualFile> files) {
    for (final VirtualFile file : files) {
      ApplicationManager.getApplication().runReadAction(new Runnable() {
        public void run() {
          matcher.process(file);
        }
      });
    }
  }

  private static Set<VirtualFile> filesContainingAllWords(final Project project, final SearchScope scope, List<String> searchHint) {
    PsiSearchHelper searchHelper = PsiManager.getInstance(project).getSearchHelper();
    final ArrayList<VirtualFile> scopeFiles = new ArrayList<VirtualFile>();

    if (scope instanceof GlobalSearchScope) {
      GlobalSearchScope globalSearchScope = (GlobalSearchScope) scope;
      for (String hint : searchHint) {
        final Set<VirtualFile> filesWithWord = new HashSet<VirtualFile>();

        Processor<PsiFile> accumulate = new Processor<PsiFile>() {
          @Override
          public boolean process(PsiFile psiFile) {
            filesWithWord.add(psiFile.getVirtualFile());
            return true;
          }
        };
        searchHelper.processAllFilesWithWordInText(hint, globalSearchScope, accumulate, false);
        searchHelper.processAllFilesWithWord(hint, globalSearchScope, accumulate, false);
        searchHelper.processAllFilesWithWordInLiterals(hint, globalSearchScope, accumulate);
        searchHelper.processAllFilesWithWordInComments(hint, globalSearchScope, accumulate);

        GlobalSearchScope toIntersect = GlobalSearchScope.filesScope(project, filesWithWord);
        globalSearchScope = globalSearchScope.intersectWith(toIntersect);
      }
      final Set<VirtualFile> result = new HashSet<VirtualFile>();
      final GlobalSearchScope finalGlobalSearchScope = globalSearchScope;
      ProjectRootManager.getInstance(project).getFileIndex().iterateContent(new ContentIterator() {
        @Override
        public boolean processFile(VirtualFile fileOrDir) {
          if (!fileOrDir.isDirectory() && finalGlobalSearchScope.contains(fileOrDir)) {
            result.add(fileOrDir);
          }
          return true;
        }
      });
      return result;
    }
    return Collections.emptySet();
  }
}
