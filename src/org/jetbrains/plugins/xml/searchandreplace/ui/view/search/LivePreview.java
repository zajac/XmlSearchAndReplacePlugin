package org.jetbrains.plugins.xml.searchandreplace.ui.view.search;


import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.markup.HighlighterLayer;
import com.intellij.openapi.editor.markup.HighlighterTargetArea;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.xml.XmlTag;
import com.intellij.usages.Usage;
import com.intellij.util.Processor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.plugins.xml.searchandreplace.FileMatcher;
import org.jetbrains.plugins.xml.searchandreplace.InjectionsMatcher;
import org.jetbrains.plugins.xml.searchandreplace.SearchResult;
import org.jetbrains.plugins.xml.searchandreplace.search.Node;
import org.jetbrains.plugins.xml.searchandreplace.search.Pattern;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class LivePreview {
  private Editor editor;
  private Map<Usage,SearchResult> searchResults = new HashMap<Usage, SearchResult>();

  public LivePreview(@Nullable Editor e) {
    editor = e;
  }

  @Nullable
  public Editor getEditor() {
    return editor;
  }

  public void setEditor(Editor editor) {
    this.editor = editor;
  }

  public void update(@NotNull final Pattern pattern) {
    if (editor == null) return;
    Document document = editor.getDocument();
    final Project project = editor.getProject();
    if (project != null) {
      final PsiFile psiFile = PsiDocumentManager.getInstance(project).getPsiFile(document);
      cleanUp();
      ApplicationManager.getApplication().runReadAction(new Runnable() {
        @Override
        public void run() {
          Processor<Usage> dummy = new Processor<Usage>() {
            @Override
            public boolean process(Usage usage) {
              return true;
            }
          };
          new FileMatcher(searchResults, pattern, project, dummy).process(psiFile);
          new InjectionsMatcher(searchResults, pattern, project, dummy).process(psiFile);
        }
      });

      SwingUtilities.invokeLater(new Runnable() {
        @Override
        public void run() {
          highlightUsages();
        }
      });
    }
  }

  public void cleanUp() {
    searchResults.clear();
    if (editor != null) {
      editor.getMarkupModel().removeAllHighlighters();
    }
  }

  private void highlightUsages() {
    if (editor == null) return;
    for (SearchResult sr : searchResults.values()) {
      Map<Node,PsiElement> match = sr.getMatch();
      TextAttributes textAttributes = createUniqueTextAttrsFor(sr);
      for (PsiElement element : match.values()) {
        if (element instanceof XmlTag) {
          element = element.getFirstChild().getNextSibling();

        }
        TextRange textRange = element.getTextRange();

        editor.getMarkupModel().addRangeHighlighter(textRange.getStartOffset(),
                textRange.getEndOffset(),
                HighlighterLayer.SELECTION + 1, textAttributes, HighlighterTargetArea.EXACT_RANGE);
      }
    }
  }

  private TextAttributes createUniqueTextAttrsFor(SearchResult sr) {
    return new TextAttributes(Color.BLACK, Color.GREEN, null, null, 0);
  }
}
