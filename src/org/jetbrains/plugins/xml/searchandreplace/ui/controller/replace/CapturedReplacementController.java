package org.jetbrains.plugins.xml.searchandreplace.ui.controller.replace;

import com.intellij.lang.Language;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.RangeMarker;
import com.intellij.openapi.editor.event.*;
import com.intellij.openapi.editor.impl.EditorImpl;
import com.intellij.openapi.editor.markup.EffectType;
import com.intellij.openapi.editor.markup.HighlighterTargetArea;
import com.intellij.openapi.editor.markup.RangeHighlighter;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.project.Project;
import com.intellij.psi.xml.XmlElement;
import org.jetbrains.plugins.xml.searchandreplace.replace.CapturePresentation;
import org.jetbrains.plugins.xml.searchandreplace.replace.ReplacementProvider;
import org.jetbrains.plugins.xml.searchandreplace.replace.Utils;
import org.jetbrains.plugins.xml.searchandreplace.search.Node;
import org.jetbrains.plugins.xml.searchandreplace.ui.CapturePresentationFactory;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.ConstraintController;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

import static java.util.Collections.sort;

public class CapturedReplacementController extends ReplacementController implements CaptureDropHandler.CaptureDropHandlerDelegate, DocumentListener, CaretListener {

  @Override
  public void beforeDocumentChange(DocumentEvent event) {}

  @Override
  public void documentChanged(DocumentEvent event) {
    String newFragment = (String) event.getNewFragment();
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
    return CapturePresentationFactory.instance().findById(captureId);
  }

  private String parseCaptureId(String text, int i) {
    StringTokenizer stringTokenizer = new StringTokenizer(text.substring(i), " $");
    if (!stringTokenizer.hasMoreTokens()) {
      return null;
    } else {
      return stringTokenizer.nextToken();
    }
  }

  @Override
  public void caretPositionChanged(CaretEvent e) {
    int offset = editor.logicalPositionToOffset(e.getNewPosition());
    for (CaptureEntry ce : entries) {
      boolean inside = ce.range.getStartOffset() <= offset && offset <= ce.range.getEndOffset();
      updateEntry(ce, inside);
    }
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

  public void setEditor(final EditorImpl editor) {
    this.editor = editor;
    editor.addEditorMouseListener(new EditorMouseListener() {
      @Override
      public void mousePressed(EditorMouseEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
      }

      @Override
      public void mouseClicked(EditorMouseEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
      }

      @Override
      public void mouseReleased(EditorMouseEvent e) {

      }

      @Override
      public void mouseEntered(EditorMouseEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
      }

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
  }

  private void updateEntry(CaptureEntry ce, boolean inside) {
    ce.capture.getConstraintController().highlightCaptures(inside);
    if (ce.range instanceof RangeHighlighter) {
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

  private Language language;
  private Project project;

  public CapturedReplacementController(Language language, Project project) {
    this.language = language;
    this.project = project;
  }

  public void addCaptureEntry(Capture c, RangeMarker r) {
    entries.add(new CaptureEntry(r, c));
  }

  @Override
  public JPanel getView() {
    return null;
  }

  @Override
  public ReplacementProvider getReplacementProvider() {
    return new ReplacementProvider() {
      @Override
      public XmlElement getReplacementFor(XmlElement element, Map<Node, XmlElement> match) {
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
        return Utils.createXmlElement(language, project, result.toString());
      }
    };
  }

  @Override
  public void insertCapture(Capture capture, int offset) {
    editor.getDocument().insertString(offset, "$" + capture.presentation().getIdentifier());
    //insertCaptureEntry(capture, offset);
  }

  private void insertCaptureEntry(Capture capture, int offset) {
    CapturePresentation presentation = capture.presentation();


    RangeMarker marker = editor.getMarkupModel().addRangeHighlighter(offset, offset + presentation.getIdentifier().length()+1, 10,
            new TextAttributes(presentation.getTextColor(), presentation.getBackgroundColor(), null, null, 0),
            HighlighterTargetArea.EXACT_RANGE);

    if (delegate != null) {
      delegate.newCaptureInserted(capture, marker);
    }
  }
}
