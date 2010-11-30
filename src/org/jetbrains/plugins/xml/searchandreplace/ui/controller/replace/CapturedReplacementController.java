package org.jetbrains.plugins.xml.searchandreplace.ui.controller.replace;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.RangeMarker;
import com.intellij.openapi.editor.event.*;
import com.intellij.openapi.editor.impl.EditorImpl;
import com.intellij.openapi.editor.markup.EffectType;
import com.intellij.openapi.editor.markup.HighlighterTargetArea;
import com.intellij.openapi.editor.markup.RangeHighlighter;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.psi.xml.XmlElement;
import org.jetbrains.plugins.xml.searchandreplace.replace.CapturePresentation;
import org.jetbrains.plugins.xml.searchandreplace.search.Node;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.captures.CapturesListener;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.captures.CapturesManager;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.captures.Capture;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.ConstraintController;

import java.awt.*;
import java.util.*;
import java.util.List;

import static java.util.Collections.sort;

public class CapturedReplacementController implements CaptureDropHandler.CaptureDropHandlerDelegate, DocumentListener, CaretListener, CapturesListener {

  @Override
  public void beforeDocumentChange(DocumentEvent event) {}

  @Override
  public void documentChanged(DocumentEvent event) {
    String newFragment = (String) event.getNewFragment();
    searchForNewCaptures();
    cleanUpBadEntries();
  }

  private void cleanUpBadEntries() {
    String text = editor.getDocument().getText();
    boolean changed;
    do {
      changed = false;

      for (CaptureEntry ce : entries) {
        boolean found = false;
        for (int i = 0; i < text.length(); ++i) {
          if (text.charAt(i) == '$') {
            String captureId = parseCaptureId(text, i+1);
            if (captureId != null && captureId.equals(ce.capture.presentation().getIdentifier())) {
              found = true;
            }
          }
        }
        if (!found) {
          killEntry(ce);
          changed = true;
          break;
        }
      }
    } while(changed);
  }

  private void searchForNewCaptures() {
    String text = editor.getDocument().getText();
    for (int i = 0; i < text.length(); ++i) {
      if (text.charAt(i) == '$') {
        String captureId = parseCaptureId(text, i+1);
        if (captureId == null) continue;
        CaptureEntry foundEntry = null;
        for (CaptureEntry ce : entries) {
          if (ce.range.getStartOffset() == i && ce.range.isValid() && ce.range.getStartOffset() != ce.range.getEndOffset()) {
            foundEntry = ce;
            break;
          }
        }
        Capture capture = findCaptureWithId(captureId);
        if (capture == null) continue;
        if (foundEntry != null) {
          foundEntry.capture = capture;
        } else {
          insertCaptureEntry(capture, i);
        }
      }
    }
  }

  private Capture findCaptureWithId(String captureId) {
    return capturesManager.findById(captureId);
  }

  private String parseCaptureId(String text, int i) {
    String number = "";
    int j = i;
    while (j < text.length() && Character.isDigit(text.charAt(j))) {
      number += text.charAt(j++);
    }
    return number.isEmpty() ? null : number;
  }

  @Override
  public void caretPositionChanged(CaretEvent e) {
    int offset = editor.logicalPositionToOffset(e.getNewPosition());
    for (CaptureEntry ce : entries) {
      boolean inside = ce.range.getStartOffset() <= offset && offset <= ce.range.getEndOffset();
      updateEntry(ce, inside);
    }
  }

  @Override
  public void captureBecameInvalid(Capture c) {
    boolean found;
    do {
      found = false;
      for (CaptureEntry ce : entries) {
        if (ce.capture == c) {
          killEntry(ce);
          found = true;
          break;
        }
      }
    } while (found);
  }

  private void killEntry(CaptureEntry ce) {
    updateEntry(ce, false);
    editor.getMarkupModel().removeHighlighter((RangeHighlighter) ce.range);
    entries.remove(ce);
  }

  @Override
  public void captureAdded(Capture c) {
    searchForNewCaptures();
  }

  public interface Delegate {
    void newCaptureInserted(Capture capture, RangeMarker where);
  }

  private Delegate delegate;

  public Delegate getDelegate() {
    return delegate;
  }

  public void setDelegate(Delegate delegate) {
    this.delegate = delegate;
  }

  private List<CaptureEntry> entries = new ArrayList<CaptureEntry>();

  void setEditor(final EditorImpl editor) {
    this.editor = editor;
  }

  private void updateEntry(CaptureEntry ce, boolean inside) {
    Capture active = null;
    if (inside) {
      active = ce.capture;
    }
    ce.capture.getConstraintController().highlightCaptures(active);
    if (ce.range instanceof RangeHighlighter && ce.range.isValid()) {
      RangeHighlighter range = (RangeHighlighter) ce.range;
      TextAttributes textAttributes = range.getTextAttributes();
      if (inside && range.getStartOffset() != range.getEndOffset()) {
        textAttributes.setEffectType(EffectType.BOXED);
        textAttributes.setEffectColor(Color.GREEN);
      } else {
        textAttributes.setEffectType(null);
      }
    }
  }

  public Editor getEditor() {
    return editor;
  }

  private Editor editor;
  private CapturesManager capturesManager;

  public CapturedReplacementController(final EditorImpl editor, CapturesManager capturesManager) {
    this.editor = editor;
    this.capturesManager = capturesManager;

    editor.addEditorMouseListener(new EditorMouseAdapter() {

      @Override
      public void mouseExited(EditorMouseEvent e) {
        for (CaptureEntry ce : entries) {
          updateEntry(ce, false);
        }
        editor.getComponent().repaint();
      }
    });
    editor.addEditorMouseMotionListener(new EditorMouseMotionListener() {
      @Override
      public void mouseMoved(EditorMouseEvent e) {
        Point point = e.getMouseEvent().getPoint();
        int offset = editor.logicalPositionToOffset(editor.xyToLogicalPosition(point));
        Set<ConstraintController> captures = new HashSet<ConstraintController>();
        for (CaptureEntry ce : entries) {
          boolean inside = offset >= ce.range.getStartOffset() && offset <= ce.range.getEndOffset();
          if (!captures.contains(ce.capture.getConstraintController())) {
            if(inside) {captures.add(ce.capture.getConstraintController()); }
            updateEntry(ce, inside);
          }
        }
        editor.getComponent().repaint();
      }

      @Override
      public void mouseDragged(EditorMouseEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
      }
    });

    CaptureDropHandler dropHandler = new CaptureDropHandler(editor);
    dropHandler.setDelegate(this);
    editor.setDropHandler(dropHandler);


    editor.getDocument().addDocumentListener(this);

    editor.getCaretModel().addCaretListener(this);

    capturesManager.addCapturesListener(this);
  }

  public void addCaptureEntry(Capture c, RangeMarker r) {
    entries.add(new CaptureEntry(r, c));
  }

  public String resolveCaptures(Map<Node, XmlElement> match) {
    String text = editor.getDocument().getText();
    StringBuilder result = new StringBuilder();
    sort(entries, new Comparator<CaptureEntry>() {
      @Override
      public int compare(CaptureEntry captureEntry, CaptureEntry captureEntry1) {
        return captureEntry.range.getStartOffset() - captureEntry1.range.getStartOffset();
      }
    });
    int start = 0;
    for (CaptureEntry entry : entries) {
      if (entry.range.isValid()) {
        result.append(text.substring(start, entry.range.getStartOffset()));
        Capture capture = entry.capture;
        Node key = null;
        for (Map.Entry<Node, XmlElement> e : match.entrySet()) {
          key = e.getKey();
          if (key.getPredicate().flatten().contains(capture.getPredicate())) {
            break;
          }
        }
        String captureResolution = capture.value(match.get(key));
        result.append(captureResolution);
        start = entry.range.getEndOffset();
      }
    }
    result.append(text.substring(start));
    return result.toString();
  }

  @Override
  public void insertCapture(Capture capture, int offset) {
    editor.getDocument().insertString(offset, "$" + capture.presentation().getIdentifier());
  }

  private void insertCaptureEntry(Capture capture, int offset) {
    CapturePresentation presentation = capture.presentation();


    RangeMarker marker = editor.getMarkupModel().addRangeHighlighter(offset, offset + presentation.getIdentifier().length()+1, 10,
            new TextAttributes(presentation.getTextColor(), presentation.getBackgroundColor(), null, null, 0),
            HighlighterTargetArea.EXACT_RANGE);
    addCaptureEntry(capture, marker);
  }
}
