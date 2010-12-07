package org.jetbrains.plugins.xml.searchandreplace.replace;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.xml.XmlElement;
import com.intellij.usages.Usage;
import com.intellij.usages.UsageInfo2UsageAdapter;
import com.intellij.usages.UsageView;
import com.intellij.usages.UsageViewManager;
import org.jetbrains.plugins.xml.searchandreplace.search.Node;

import javax.swing.*;
import java.util.Collection;
import java.util.Map;

public class Replacer implements UsageViewManager.UsageViewStateListener {

  private Project project;
  private ReplacementProvider replacementProvider;
  private Map<Usage, Map<Node, PsiElement>> searchResults;
  private UsageView usageView;

  public Replacer(Project project, ReplacementProvider replacementProvider, Map<Usage, Map<Node, PsiElement>> searchResults) {
    this.project = project;
    this.replacementProvider = replacementProvider;
    this.searchResults = searchResults;
  }

  public void usageViewCreated(UsageView usageView) {
    this.usageView = usageView;
  }

  public void findingUsagesFinished(final UsageView usageView) {
    if (usageView != null) {
      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          usageView.addPerformOperationAction(new Runnable() {
            public void run() {
              performReplace(usageView.getUsages());
            }
          }, "Replace All", null, "Replace All");
          usageView.addButtonToLowerPane(new Runnable() {
            public void run() {
              performReplace(usageView.getSelectedUsages());
            }
          }, "Replace selected");
        }
      });
    }
  }

  private void doActualReplace(Usage u, Map<Node, PsiElement> match) {
    if (u instanceof UsageInfo2UsageAdapter) {
      PsiElement element = ((UsageInfo2UsageAdapter) u).getElement();
      if (element instanceof XmlElement) {
        XmlElement xmlElement = (XmlElement) element;
        XmlElement replacement = replacementProvider.getReplacementFor(xmlElement, match);
        if (replacement != xmlElement && replacement != null) {
          xmlElement.replace(replacement);
        }
      }
    }
  }

  private void performReplace(final Collection<Usage> usages) {
    CommandProcessor.getInstance().executeCommand(project, new Runnable() {
      public void run() {
        ApplicationManager.getApplication().runWriteAction(new Runnable() {
          public void run() {
            for (Usage u : usages) {
              if (usageView.getExcludedUsages().contains(u)) continue;
              Map<Node, PsiElement> match = searchResults.get(u);
              doActualReplace(u, match);
            }
          }
        });
      }
    }, "XML tag replace", null);
    usageView.excludeUsages(usages.toArray(new Usage[usages.size()]));
  }
}
