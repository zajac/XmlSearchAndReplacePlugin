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
import com.intellij.usageView.UsageInfo;
import com.intellij.usages.*;
import com.intellij.util.Processor;
import org.intellij.plugins.xpathView.search.SearchScope;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.plugins.xml.searchandreplace.replace.ReplacementProvider;
import org.jetbrains.plugins.xml.searchandreplace.replace.Replacer;
import org.jetbrains.plugins.xml.searchandreplace.search.Node;
import org.jetbrains.plugins.xml.searchandreplace.search.Pattern;
import org.jetbrains.plugins.xml.searchandreplace.search.TagSearchObserver;
import org.jetbrains.plugins.xml.searchandreplace.ui.MainDialog;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
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
              SearchAndReplaceMenuAction.performSearchAndReplace(project, pattern, scope, dialog.getReplacementProvider());
            }
          }
        });
        mainDialog.show();
      }
    }

  }

  private static Factory<UsageSearcher> createUsageSearcherFactory(final Project project, final Pattern pattern, final SearchScope scope, final Map<Usage, Map<Node, XmlElement>> searchResults) {
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
                          public void elementFound(Pattern pattern, XmlElement tag) {                            
                            if (!foundTags.contains(tag) && tag != null) {

                              System.out.println(pattern.getMatchedNodes());
                              System.out.println();

                              foundTags.add(tag);
                              Usage usage = new UsageInfo2UsageAdapter(new UsageInfo(tag));
                              searchResults.put(usage, pattern.getMatchedNodes());
                              usageProcessor.process(usage);
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

  private static void performSearchAndReplace(@NotNull Project project, @NotNull final Pattern pattern, @NotNull SearchScope scope, ReplacementProvider replacementProvider) {
    UsageViewPresentation presentation = new UsageViewPresentation();
    presentation.setUsagesString("");
    presentation.setTabText("XML Tag");
    presentation.setScopeText("");

    Map<Usage,Map<Node, XmlElement>> searchResults = new HashMap<Usage, Map<Node, XmlElement>>();
    Replacer replacer = replacementProvider == null ? null : new Replacer(project, replacementProvider, searchResults);
    Factory<UsageSearcher> searcherFactory = createUsageSearcherFactory(project, pattern, scope, searchResults);
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
                clearHighlights = true;
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
            }}, searcherFactory, true, true, presentation, replacer);    
  }

  @Override
  public void update(AnActionEvent anActionEvent) {
    Project project = PlatformDataKeys.PROJECT.getData(anActionEvent.getDataContext());
    anActionEvent.getPresentation().setEnabled(project != null);
  }
}
