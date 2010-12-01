package org.jetbrains.plugins.xml.searchandreplace;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.xml.XmlElement;
import com.intellij.psi.xml.XmlFile;
import com.intellij.usageView.UsageInfo;
import com.intellij.usages.Usage;
import com.intellij.usages.UsageInfo2UsageAdapter;
import com.intellij.util.Processor;
import org.jetbrains.plugins.xml.searchandreplace.search.Node;
import org.jetbrains.plugins.xml.searchandreplace.search.Pattern;
import org.jetbrains.plugins.xml.searchandreplace.search.TagSearchObserver;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;


class FileMatcher implements Processor<VirtualFile> {
  private final Project project;
  private final Pattern pattern;
  private final Set<XmlElement> foundTags;
  private final Map<Usage, Map<Node, XmlElement>> searchResults;
  private final Processor<Usage> usageProcessor;

  public FileMatcher(Project project, Pattern pattern, Map<Usage, Map<Node, XmlElement>> searchResults, Processor<Usage> usageProcessor) {
    this.project = project;
    this.pattern = pattern;
    foundTags = new HashSet<XmlElement>();
    this.searchResults = searchResults;
    this.usageProcessor = usageProcessor;
  }

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
}
