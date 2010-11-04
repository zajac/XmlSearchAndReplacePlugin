package org.jetbrains.plugins.xml.searchandreplace;

import com.intellij.ide.projectView.PresentationData;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.application.ApplicationManager;
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
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.xml.XmlElement;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import com.intellij.usageView.UsageInfo;
import com.intellij.usages.*;
import com.intellij.util.Processor;
import org.intellij.plugins.xpathView.search.SearchScope;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.plugins.xml.searchandreplace.replace.ReplacementProvider;
import org.jetbrains.plugins.xml.searchandreplace.replace.Replacer;
import org.jetbrains.plugins.xml.searchandreplace.search.Pattern;
import org.jetbrains.plugins.xml.searchandreplace.search.TagSearchObserver;
import org.jetbrains.plugins.xml.searchandreplace.search.predicates.Not;
import org.jetbrains.plugins.xml.searchandreplace.search.predicates.TagPredicate;
import org.jetbrains.plugins.xml.searchandreplace.search.predicates.XmlElementPredicate;
import org.jetbrains.plugins.xml.searchandreplace.ui.MainDialog;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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
            if (pattern != null && scope != null) {
              Factory<UsageSearcher> factory = createUsageSearcherFactory(project, pattern, scope);
              SearchAndReplaceMenuAction.performSearchAndReplace(project, pattern, scope, dialog.getReplacementProvider());
            }
          }
        });
        mainDialog.show();
      }
    }

  }

  private static Factory<UsageSearcher> createUsageSearcherFactory(final Project project, final Pattern pattern, final SearchScope scope) {
    return new Factory<UsageSearcher>() {
      public UsageSearcher create() {
        return new UsageSearcher() {
          public void generate(final Processor<Usage> usageProcessor) {
            ApplicationManager.getApplication().runReadAction(new Runnable() {
              public void run() {
                final Set<XmlElement> foundTags = new HashSet<XmlElement>();
                scope.iterateContent(project, new Processor<VirtualFile>() {
                  public boolean process(VirtualFile virtualFile) {
                    PsiFile psiFile = PsiManager.getInstance(project).findFile(virtualFile);
                    if (psiFile != null) {
                      if (psiFile instanceof XmlFile) {
                        XmlElement root = ((XmlFile) psiFile).getRootTag();
                        pattern.match(root, new TagSearchObserver() {
                          public void elementFound(XmlElement tag) {
                            if (!foundTags.contains(tag)) {
                              foundTags.add(tag);
                              usageProcessor.process(new UsageInfo2UsageAdapter(new UsageInfo(tag)));
                            }
                          }
                        });
                      }
                    }
                    return true;
                  }
                });
              }
            });
          }
        };
      }
    };
  }

  private static void performSearchAndReplace(@NotNull Project project, @NotNull Pattern pattern, @NotNull SearchScope scope, ReplacementProvider replacementProvider) {
    UsageViewPresentation presentation = new UsageViewPresentation();
    presentation.setUsagesString("Usages String");
    presentation.setTabText("Tab text");
    presentation.setScopeText("My Scope text");
    Factory<UsageSearcher> searcherFactory = createUsageSearcherFactory(project, pattern, scope);
    Replacer replacer = replacementProvider == null ? null : new Replacer(project, replacementProvider);
    UsageView myUsageView = UsageViewManager.getInstance(project).searchAndShowUsages(
            new UsageTarget[]{new UsageTarget() {

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
                return "SHIT";
              }

              public ItemPresentation getPresentation() {
                return new PresentationData("SHIT2", null, null, null, null);
              }

              public FileStatus getFileStatus() {
                return FileStatus.NOT_CHANGED;
              }

              public void navigate(boolean requestFocus) {
                //To change body of implemented methods use File | Settings | File Templates.
              }

              public boolean canNavigate() {
                return false;  //To change body of implemented methods use File | Settings | File Templates.
              }

              public boolean canNavigateToSource() {
                return false;  //To change body of implemented methods use File | Settings | File Templates.
              }
            }}, searcherFactory, true, true, presentation, replacer);
    
  }

  

  @Override
  public void update(AnActionEvent anActionEvent) {
    Project project = PlatformDataKeys.PROJECT.getData(anActionEvent.getDataContext());
    anActionEvent.getPresentation().setEnabled(project != null);
  }
}
