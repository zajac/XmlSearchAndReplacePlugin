package org.jetbrains.plugins.xml.searchandreplace;

import com.intellij.ide.projectView.PresentationData;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.util.Factory;
import com.intellij.openapi.vcs.FileStatus;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.usages.*;
import org.intellij.plugins.xpathView.search.SearchScope;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.plugins.xml.searchandreplace.replace.ReplacementProvider;
import org.jetbrains.plugins.xml.searchandreplace.replace.Replacer;
import org.jetbrains.plugins.xml.searchandreplace.search.Node;
import org.jetbrains.plugins.xml.searchandreplace.search.Pattern;
import org.jetbrains.plugins.xml.searchandreplace.ui.MainDialog;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.persistence.PatternsStorage;

import java.util.HashMap;
import java.util.Map;

public class SearchAndReplaceMenuAction extends AnAction {

  @Override
  public void actionPerformed(AnActionEvent anActionEvent) {

    final Project project = PlatformDataKeys.PROJECT.getData(anActionEvent.getDataContext());

    if (project != null) {
      final Editor editor = PlatformDataKeys.EDITOR.getData(anActionEvent.getDataContext());
      Module module = null;
      if (editor != null) {
        PsiFile psiFile = PsiDocumentManager.getInstance(project).getPsiFile(editor.getDocument());
        if (psiFile != null) {
          VirtualFile virtualFile = psiFile.getVirtualFile();
          if (virtualFile != null) {
            module = ProjectRootManager.getInstance(project).getFileIndex().getModuleForFile(virtualFile);
          }
        }
      } else {
        Module[] modules = ModuleManager.getInstance(project).getModules();
        if (modules.length > 0) {
          module = modules[0];
        }
      }
      if (module != null) {
        MainDialog mainDialog = new MainDialog(project, module);
        mainDialog.setDelegate(new MainDialog.MainDialogDelegate() {
          public void performSearch(MainDialog dialog) {
            Pattern pattern = dialog.getPattern();
            SearchScope scope = dialog.getSelectedScope();
            PatternsStorage.getInstance(project).setRecentScope(scope);
            if (pattern != null && scope != null) {
              SearchAndReplaceMenuAction.performSearchAndReplace(project, pattern, scope, dialog.getReplacementProvider());
            }
          }
        });
        mainDialog.show();
      }
    }
  }

  private static void performSearchAndReplace(@NotNull final Project project, @NotNull final Pattern pattern, @NotNull final SearchScope scope, ReplacementProvider replacementProvider) {
    UsageViewPresentation presentation = new UsageViewPresentation();
    presentation.setUsagesString("");
    presentation.setTabText("XML Tag");
    presentation.setScopeText("");

    final Map<Usage,Map<Node, PsiElement>> searchResults = new HashMap<Usage, Map<Node, PsiElement>>();
    Replacer replacer = replacementProvider == null ? null : new Replacer(project, replacementProvider, searchResults);
    UsageView myUsageView = UsageViewManager.getInstance(project).searchAndShowUsages(
            new UsageTarget[]{new MyUsageTarget(pattern)}, new Factory<UsageSearcher>() {
              public UsageSearcher create() {
                return new PatternUsageSearcher(pattern, project, searchResults, scope);
              }
            }, true, true, presentation, replacer);
  }

  @Override
  public void update(AnActionEvent anActionEvent) {
    Project project = PlatformDataKeys.PROJECT.getData(anActionEvent.getDataContext());
    anActionEvent.getPresentation().setEnabled(project != null);
  }

  private static class MyUsageTarget implements UsageTarget {

    private final Pattern pattern;

    public MyUsageTarget(Pattern pattern) {
      this.pattern = pattern;
    }

    public void findUsages() {
      //To change body of implemented methods use File | Settings | File Templates.
    }

    public void findUsagesInEditor(@NotNull FileEditor editor) {
      //To change body of implemented methods use File | Settings | File Templates.
    }

    public void highlightUsages(PsiFile file, Editor editor, boolean clearHighlights) {
      //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean isValid() {
      return true;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean isReadOnly() {
      return true;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public VirtualFile[] getFiles() {
      return null;
    }

    public void update() {
      //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getName() {
      return "";
    }

    public ItemPresentation getPresentation() {
      return new PresentationData("XML tag: " + pattern.getTheOne().getPredicate(), null, null, null, null);
    }

    public FileStatus getFileStatus() {
      return FileStatus.NOT_CHANGED;
    }

    public void navigate(boolean requestFocus) {
      //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean canNavigate() {
      return true;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean canNavigateToSource() {
      return false;  //To change body of implemented methods use File | Settings | File Templates.
    }
  }
}
