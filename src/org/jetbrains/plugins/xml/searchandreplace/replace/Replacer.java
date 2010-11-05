package org.jetbrains.plugins.xml.searchandreplace.replace;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.xml.XmlElement;
import com.intellij.usages.Usage;
import com.intellij.usages.UsageInfo2UsageAdapter;
import com.intellij.usages.UsageView;
import com.intellij.usages.UsageViewManager;

import javax.swing.*;
import java.util.Collection;

public class Replacer implements UsageViewManager.UsageViewStateListener {

  private ReplacementProvider replacementProvider;
  private UsageView usageView;

  public Replacer(Project project, ReplacementProvider replacementProvider) {
    this.replacementProvider = replacementProvider;
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

  private void performReplace(final Collection<Usage> usages) {
    ApplicationManager.getApplication().runWriteAction(new Runnable() {
      public void run() {
        for (Usage u : usages) {
          if (usageView.getExcludedUsages().contains(u)) continue;
          if (u instanceof UsageInfo2UsageAdapter) {
            PsiElement element = ((UsageInfo2UsageAdapter) u).getElement();
            if (element instanceof XmlElement) {
              XmlElement xmlElement = (XmlElement) element;
              XmlElement replacement = replacementProvider.getReplacementFor(xmlElement);
              if (replacement != xmlElement && replacement != null) {
                xmlElement.replace(replacement);
              }
            }
          }
        }
      }
    });
    usageView.excludeUsages(usages.toArray(new Usage[0]));    
  }
}
