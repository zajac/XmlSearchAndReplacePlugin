package org.jetbrains.plugins.xml.searchandreplace.ui.view;

import org.jetbrains.plugins.xml.searchandreplace.ui.controller.replace.Capture;
import org.jetbrains.plugins.xml.searchandreplace.replace.CapturePresentation;

import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class CaptureView extends JLabel {

  private Capture capture;

  private JComponent thisAsComponent = this;

  private class MyTransferHandler extends TransferHandler {

    private DataFlavor dataFlavor = new DataFlavor(Capture.class, capture.presentation().getName());

    public MyTransferHandler() {
    }

    @Override
    protected Transferable createTransferable(JComponent jComponent) {
      return new Transferable() {
        @Override
        public DataFlavor[] getTransferDataFlavors() {
          return new DataFlavor[]{dataFlavor};
        }

        @Override
        public boolean isDataFlavorSupported(DataFlavor aDataFlavor) {
          return dataFlavor.equals(aDataFlavor);
        }

        @Override
        public Object getTransferData(DataFlavor aDataFlavor) throws UnsupportedFlavorException, IOException {
          return capture;
        }
      };
    }

    @Override
    public boolean importData(TransferSupport transferSupport) {
      return false;
    }

    @Override
    public boolean importData(JComponent jComponent, Transferable transferable) {
      return false;
    }

    @Override
    public boolean canImport(TransferSupport transferSupport) {
      return false;
    }

    @Override
    public boolean canImport(JComponent jComponent, DataFlavor[] dataFlavors) {
      return false;
    }

    @Override
    public int getSourceActions(JComponent jComponent) {
      return TransferHandler.COPY;
    }

    @Override
    protected void exportDone(JComponent jComponent, Transferable transferable, int i) {
      super.exportDone(jComponent, transferable, i);
    }
  }

  public CaptureView(Capture capture) {
    super(capture.presentation().getName());
    this.capture = capture;
    CapturePresentation cp = capture.presentation();
    setBackground(cp.getBackgroundColor());
    setForeground(cp.getTextColor());
    setOpaque(true);
    updateUI();    
    setTransferHandler(new MyTransferHandler());

    addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent e) {
        JComponent comp = (JComponent)e.getSource();
        TransferHandler th = comp.getTransferHandler();
        th.exportAsDrag(comp, e, TransferHandler.COPY);
      }
    });
  }


}
