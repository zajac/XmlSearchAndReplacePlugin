package org.jetbrains.plugins.xml.searchandreplace.ui.controller.replace;

import com.intellij.lang.Language;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.RangeMarker;
import com.intellij.openapi.editor.event.EditorMouseEvent;
import com.intellij.openapi.editor.event.EditorMouseListener;
import com.intellij.openapi.editor.event.EditorMouseMotionListener;
import com.intellij.openapi.editor.markup.EffectType;
import com.intellij.openapi.editor.markup.RangeHighlighter;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.project.Project;
import com.intellij.psi.xml.XmlElement;
import org.jetbrains.plugins.xml.searchandreplace.replace.ReplacementProvider;
import org.jetbrains.plugins.xml.searchandreplace.replace.Utils;
import org.jetbrains.plugins.xml.searchandreplace.search.Node;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.ConstraintController;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

import static java.util.Collections.sort;

public class CapturedReplacementController extends ReplacementController {

  private List<CaptureEntry> entries = new ArrayList<CaptureEntry>();

  public void setEditor(final Editor editor) {
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
  }

  private void updateEntry(CaptureEntry ce, boolean inside) {
    ce.capture.getConstraintController().highlightCaptures(inside);
    if (ce.range instanceof RangeHighlighter) {
      RangeHighlighter range = (RangeHighlighter) ce.range;
      TextAttributes textAttributes = range.getTextAttributes();
      if (inside) {
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
        return Utils.createXmlElement(language, project, result.toString());
      }
    };
  }
}
