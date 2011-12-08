package org.jetbrains.plugins.xml.searchandreplace.ui;


import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.ScrollType;
import com.intellij.openapi.editor.markup.HighlighterLayer;
import com.intellij.openapi.editor.markup.HighlighterTargetArea;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.TextEditor;
import com.intellij.openapi.fileEditor.ex.FileEditorManagerEx;
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

  private MomentoUserActivityWatcher watcher = new MomentoUserActivityWatcher();
  private Alarm livePreviewAlarm;
  private Pattern pattern;
  private static final TextAttributes MAIN_TARGET_ATTRIBUTES = new TextAttributes(Color.BLACK, Color.RED, null, null, 0);
  private Project project;

  private Editor editor;


  public LivePreview(PatternController patternController,Editor editor, Project project) {
    this.project = project;
    this.editor = editor;
    this.patternController = patternController;
    livePreviewAlarm = new Alarm(Alarm.ThreadToUse.SHARED_THREAD);
    watcher.addUserActivityListener(this);
    injectActivityWatcher();
    stateChanged();
  }

  private void injectActivityWatcher() {
//    watcher = new MomentoUserActivityWatcher();
//    watcher.addUserActivityListener(this);
    patternController.accept(new PatternController.Visitor() {
      @Override
      public void visitConstraint(ConstraintController constraintController) {
        subscribeWatcher(constraintController);
      }
    });
    patternController.addConstraintsTreeListener(this);
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
    if (project == null) return null;
    FileEditorManagerEx instanceEx = FileEditorManagerEx.getInstanceEx(project);
    if (instanceEx != null) {
      FileEditor[] editors = instanceEx.getEditors(instanceEx.getCurrentFile());
      if (editors.length > 0) {
        FileEditor fileEditor = editors[0];
        if (fileEditor instanceof TextEditor) {
          Editor editor1 = ((TextEditor) fileEditor).getEditor();
          if (editor != null) {
            editor.getMarkupModel().removeAllHighlighters();
          }
          editor = editor1;
        }
      }
    }
    return editor;
  }

  public void stateChanged() {
    livePreviewAlarm.cancelAllRequests();
    final Editor editor1 = getEditor();
    if (editor1 == null) return;
    cleanUp();
    injectActivityWatcher();
    pattern = patternController.buildPattern();
    livePreviewAlarm.addRequest(new Runnable() {
      @Override
      public void run() {
        if (patternController != null) {
          update(editor1);
        }
      }
    }, USER_ACTIVITY_PAUSE);
  }

  private void update(final Editor e) {

    ApplicationManager.getApplication().runReadAction(new Runnable() {
      @Override
      public void run() {
        if (e != null) {
          Document document = e.getDocument();
          final Project project = e.getProject();
          if (project != null) {

            PsiFile psiFile = PsiDocumentManager.getInstance(project).getPsiFile(document);

            Processor<Usage> dummy = new Processor<Usage>() {
              @Override
              public boolean process(Usage usage) {
                return true;
              }
            };
            new FileMatcher(searchResults, pattern, project, dummy).process(psiFile);
            //new InjectionsMatcher(searchResults, pattern, project, dummy).process(psiFile); //TODO: make it hightlight injected elements right
          }
        }
      }
    });

    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        highlightUsages();
      }
    });
  }

  private void highlightUsages() {
    Editor e = getEditor();
    if (e == null) return;
    boolean foundVisibleElement = false;
    int offset = Integer.MAX_VALUE;
    for (SearchResult sr : searchResults.values()) {
      Map<Node,PsiElement> match = sr.getMatch();
      TextAttributes textAttributes = createUniqueTextAttrsFor(sr);
      PsiElement targetElement = match.get(pattern.getTheOne());

      if (!foundVisibleElement && insideVisibleArea(e, targetElement)) {
        foundVisibleElement = true;
      }
      if (!foundVisibleElement && targetElement.getTextOffset() < offset) {
        offset = targetElement.getTextOffset();
      }
      for (PsiElement element : match.values()) {
        TextAttributes attributes = textAttributes;
        if (element == targetElement) {
          attributes = MAIN_TARGET_ATTRIBUTES;
        }
        if (element instanceof XmlTag) {
          element = element.getFirstChild().getNextSibling();
        }
        TextRange textRange = element.getTextRange();

        e.getMarkupModel().addRangeHighlighter(textRange.getStartOffset(),
                textRange.getEndOffset(),
                HighlighterLayer.SELECTION + 1, attributes, HighlighterTargetArea.EXACT_RANGE);
      }
    }
    if (!foundVisibleElement && offset != Integer.MAX_VALUE) {
      e.getScrollingModel().scrollTo(e.offsetToLogicalPosition(offset), ScrollType.CENTER);
    }
  }

  private boolean insideVisibleArea(Editor e, PsiElement targetElement) {
    Rectangle visibleArea = e.getScrollingModel().getVisibleArea();
    Point point = e.logicalPositionToXY(e.offsetToLogicalPosition(targetElement.getTextOffset()));

    return visibleArea.contains(point);
  }

  private TextAttributes createUniqueTextAttrsFor(SearchResult sr) {
    return new TextAttributes(Color.BLACK, Color.GREEN, null, null, 0);
  }

  @Override
  public void constraintAdded(ConstraintController c, ConstraintController parent) {
    subscribeWatcher(c);
    stateChanged();
  }

  @Override
  public void constraintRemoved(ConstraintController c, ConstraintController parent) {
    stateChanged();
  }

  @Override
  public void constraintTypeSelected(ConstraintController c) {
    subscribeWatcherToConstraintTypeController(c);
    stateChanged();
  }

  public void cleanUp() {
    unsubscribe();
    searchResults.clear();
    if (editor != null) {
      editor.getMarkupModel().removeAllHighlighters();
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
  }
}
