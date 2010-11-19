package org.jetbrains.plugins.xml.searchandreplace.ui.controller.replace;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.EditorDropHandler;
import com.intellij.openapi.editor.RangeMarker;
import com.intellij.openapi.editor.impl.EditorImpl;
import com.intellij.openapi.editor.markup.HighlighterTargetArea;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.project.Project;
import org.jetbrains.plugins.xml.searchandreplace.replace.CapturePresentation;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

class CaptureDropHandler implements EditorDropHandler {

  public interface CaptureDropHandlerDelegate {
    void newCaptureInserted(Capture capture, RangeMarker where);
  }

  private final EditorImpl editor;

  private CaptureDropHandlerDelegate delegate;

  public CaptureDropHandlerDelegate getDelegate() {
    return delegate;
  }

  public void setDelegate(CaptureDropHandlerDelegate delegate) {
    this.delegate = delegate;
  }

  public CaptureDropHandler(EditorImpl editor) {
    this.editor = editor;
  }

  @Override
  public boolean canHandleDrop(DataFlavor[] transferFlavors) {
    for (DataFlavor f : transferFlavors) {
      if (f.getRepresentationClass() == Capture.class) {
        return true;
      }
    }
    return false;
  }

  @Override
  public void handleDrop(final Transferable t, Project project) {
    ApplicationManager.getApplication().runWriteAction(new Runnable() {
      @Override
      public void run() {
        try {
          Object transferData = t.getTransferData(t.getTransferDataFlavors()[0]);
          if (transferData instanceof Capture) {
            Capture capture = (Capture) transferData;
            int offset = editor.getCaretModel().getOffset();
            CapturePresentation presentation = capture.presentation();
            editor.getDocument().insertString(offset, presentation.getName());
            RangeMarker rangeHighlighter = editor.getMarkupModel().addRangeHighlighter(offset, offset + presentation.getName().length(), 10,
                    new TextAttributes(presentation.getTextColor(), presentation.getBackgroundColor(), null, null, 0),
                    HighlighterTargetArea.EXACT_RANGE);

            delegate.newCaptureInserted(capture, rangeHighlighter);
          }
        } catch (UnsupportedFlavorException e) {
          e.printStackTrace();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    });
  }
}
