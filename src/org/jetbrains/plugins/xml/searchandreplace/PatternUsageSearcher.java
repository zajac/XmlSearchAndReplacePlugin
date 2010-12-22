package org.jetbrains.plugins.xml.searchandreplace;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ContentIterator;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.impl.PsiManagerEx;
import com.intellij.psi.impl.cache.CacheManager;
import com.intellij.psi.search.*;
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
    FileMatcher fileMatcher = new FileMatcher(searchResults, pattern, project, usageProcessor);
    InjectionsMatcher injectionsMatcher = new InjectionsMatcher(searchResults, pattern, project, usageProcessor);
    if (scope instanceof GlobalSearchScope) {
      Set<VirtualFile> files = filesContainingAllWords(project, (GlobalSearchScope) scope, searchHint);
      searchWithMatcher(fileMatcher, files);
      searchWithMatcher(injectionsMatcher, files);
    } else {
      LocalSearchScope localSearchScope = (LocalSearchScope) scope;
      for (PsiElement e : localSearchScope.getScope()) {
        if (e instanceof PsiFile) {
          processInReadAction(fileMatcher, ((PsiFile) e).getVirtualFile());
          processInReadAction(injectionsMatcher, ((PsiFile) e).getVirtualFile());
        }
      }

    }
  }

  private void searchWithMatcher(final Matcher matcher, Set<VirtualFile> files) {
    for (final VirtualFile file : files) {
      processInReadAction(matcher, file);
    }
  }

  private void processInReadAction(final Matcher matcher, final VirtualFile file) {
    ApplicationManager.getApplication().runReadAction(new Runnable() {
      public void run() {
        matcher.process(file);
      }
    });
  }

  private static Set<VirtualFile> filesContainingAllWords(final Project project, GlobalSearchScope scope, List<String> searchHint) {
    PsiSearchHelper searchHelper = PsiManager.getInstance(project).getSearchHelper();
    final ArrayList<VirtualFile> scopeFiles = new ArrayList<VirtualFile>();

    CacheManager cacheManager = null;
    PsiManager psiManager = PsiManager.getInstance(project);
    if (psiManager instanceof PsiManagerEx) {
      cacheManager = ((PsiManagerEx) psiManager).getCacheManager();

    }
    if (scope != null && cacheManager != null) {
      final Set<VirtualFile> filesWithWord = new HashSet<VirtualFile>();

      Processor<PsiFile> accumulate = new Processor<PsiFile>() {
        @Override
        public boolean process(PsiFile psiFile) {
          filesWithWord.add(psiFile.getVirtualFile());
          return true;
        }
      };
      for (String hint : searchHint) {
        cacheManager.processFilesWithWord(accumulate, hint, UsageSearchContext.ANY, scope, false);

        GlobalSearchScope toIntersect = GlobalSearchScope.filesScope(project, new HashSet<VirtualFile>(filesWithWord));
        scope = scope.intersectWith(toIntersect);
        filesWithWord.clear();
      }
      final Set<VirtualFile> result = new HashSet<VirtualFile>();
      final GlobalSearchScope finalGlobalSearchScope = scope;
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
