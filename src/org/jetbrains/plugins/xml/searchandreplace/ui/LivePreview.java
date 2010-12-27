package org.jetbrains.plugins.xml.searchandreplace.ui;


import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.markup.HighlighterLayer;
import com.intellij.openapi.editor.markup.HighlighterTargetArea;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.TextEditor;
import com.intellij.openapi.fileEditor.ex.FileEditorManagerEx;
import com.intellij.openapi.fileEditor.impl.EditorWindow;
import com.intellij.openapi.fileEditor.impl.EditorWithProviderComposite;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.xml.XmlTag;
import com.intellij.ui.UserActivityListener;
import com.intellij.usages.Usage;
import com.intellij.util.Alarm;
import com.intellij.util.Processor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.plugins.xml.searchandreplace.FileMatcher;
import org.jetbrains.plugins.xml.searchandreplace.SearchResult;
import org.jetbrains.plugins.xml.searchandreplace.search.Node;
import org.jetbrains.plugins.xml.searchandreplace.search.Pattern;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.ConstraintController;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.ConstraintTypeController;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.PatternController;
import org.jetbrains.plugins.xml.searchandreplace.ui.view.search.ConstraintPanel;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class LivePreview implements UserActivityListener, PatternController.ConstraintsTreeListener, ConstraintController.ConstraintListener {

  private static final int USER_ACTIVITY_PAUSE = 1000;

  private PatternController patternController;
  private Map<Usage,SearchResult> searchResults = new HashMap<Usage, SearchResult>();

  private MomentoUserActivityWatcher watcher;
  private Alarm livePreviewAlarm;
  private Pattern pattern;
  private static final TextAttributes MAIN_TARGET_ATTRIBUTES = new TextAttributes(Color.BLACK, Color.RED, null, null, 0);
  private Project project;

  private Editor editor;


  public LivePreview(PatternController patternController,Editor editor) {
    this.project = editor.getProject();
    this.editor = editor;
    this.patternController = patternController;
    livePreviewAlarm = new Alarm(Alarm.ThreadToUse.SWING_THREAD);
    injectActivityWatcher();
    patternController.addConstraintsTreeListener(this);
    update(patternController.buildPattern());
  }

  private void injectActivityWatcher() {
    watcher = new MomentoUserActivityWatcher();
    watcher.addUserActivityListener(this);
    patternController.accept(new PatternController.Visitor() {
      @Override
      public void visitConstraint(ConstraintController constraintController) {
        subscribeWatcher(constraintController);
      }
    });
  }

  private void subscribeWatcher(ConstraintController constraintController) {
    ConstraintPanel view = constraintController.getView();
    if (!watcher.isWatched(view)) {
      watcher.register(view);
    }
    subscribeWatcherToConstraintTypeController(constraintController);
    constraintController.addConstraintListener(this);
  }

  private void subscribeWatcherToConstraintTypeController(ConstraintController constraintController) {
    ConstraintTypeController constraintTypeController = constraintController.getConstraintTypeController();
    if (constraintTypeController != null) {
      JPanel view = constraintTypeController.getView();
      if (!watcher.isWatched(view)) {
        watcher.register(view);
      }
    }
  }

  @Nullable
  public Editor getEditor() {
    EditorWindow currentWindow = FileEditorManagerEx.getInstanceEx(project).getCurrentWindow();
    if (currentWindow != null) {
      EditorWithProviderComposite selectedEditor = currentWindow.getSelectedEditor();
      if (selectedEditor != null) {
        FileEditor fileEditor = selectedEditor.getSelectedEditorWithProvider().first;
        if (fileEditor instanceof TextEditor) {
          Editor editor1 = ((TextEditor) fileEditor).getEditor();
          if (editor1 != editor) {
            editor.getMarkupModel().removeAllHighlighters();
            editor = editor1;
          }
        }
      }
    }
    return editor;
  }

  public void stateChanged() {
    livePreviewAlarm.cancelAllRequests();
    livePreviewAlarm.addRequest(new Runnable() {
      @Override
      public void run() {
        if (patternController != null) {
          update(patternController.buildPattern());
        }
      }
    }, USER_ACTIVITY_PAUSE);
  }

  public void update(@NotNull final Pattern pattern) {
    if (getEditor() == null) return;
    this.pattern = pattern;
    Document document = getEditor().getDocument();
    final Project project = getEditor().getProject();
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
          //new InjectionsMatcher(searchResults, pattern, project, dummy).process(psiFile); //TODO: make it hightlight injected elements right
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

  private void highlightUsages() {
    if (getEditor() == null) return;
    for (SearchResult sr : searchResults.values()) {
      Map<Node,PsiElement> match = sr.getMatch();
      TextAttributes textAttributes = createUniqueTextAttrsFor(sr);
      PsiElement mainTarget = match.get(pattern.getTheOne());
      for (PsiElement element : match.values()) {
        TextAttributes attributes = textAttributes;
        if (element == mainTarget) {
          attributes = MAIN_TARGET_ATTRIBUTES;
        }
        if (element instanceof XmlTag) {
          element = element.getFirstChild().getNextSibling();
        }
        TextRange textRange = element.getTextRange();

        getEditor().getMarkupModel().addRangeHighlighter(textRange.getStartOffset(),
                textRange.getEndOffset(),
                HighlighterLayer.SELECTION + 1, attributes, HighlighterTargetArea.EXACT_RANGE);
      }
    }
  }

  private TextAttributes createUniqueTextAttrsFor(SearchResult sr) {
    return new TextAttributes(Color.BLACK, Color.GREEN, null, null, 0);
  }

  @Override
  public void constraintAdded(ConstraintController c, ConstraintController parent) {
    subscribeWatcher(c);
    update(patternController.buildPattern());
  }

  @Override
  public void constraintRemoved(ConstraintController c, ConstraintController parent) {
    update(patternController.buildPattern());
  }

  @Override
  public void constraintTypeSelected(ConstraintController c) {
    subscribeWatcherToConstraintTypeController(c);
    update(patternController.buildPattern());
  }

  public void cleanUp() {
    unsubscribe();
    searchResults.clear();
    if (getEditor() != null) {
      getEditor().getMarkupModel().removeAllHighlighters();
    }
  }

  private void unsubscribe() {
    final LivePreview livePreview = this;
    patternController.accept(new PatternController.Visitor() {
      @Override
      public void visitConstraint(ConstraintController constraintController) {
        constraintController.removeConstraintListener(livePreview);
      }
    });
    patternController.removeConstraintsTreeListener(this);
    getEditor().getMarkupModel().removeAllHighlighters();
  }
}
