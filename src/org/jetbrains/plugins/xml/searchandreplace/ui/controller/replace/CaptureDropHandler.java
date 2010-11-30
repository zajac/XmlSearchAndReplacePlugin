package org.jetbrains.plugins.xml.searchandreplace.ui.controller.replace;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.EditorDropHandler;
import com.intellij.openapi.editor.impl.EditorImpl;
import com.intellij.openapi.project.Project;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.captures.Capture;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

class CaptureDropHandler implements EditorDropHandler {

  public interface CaptureDropHandlerDelegate {
    void insertCapture(Capture capture, int offset);
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


            delegate.insertCapture(capture, offset);
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
