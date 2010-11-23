package org.jetbrains.plugins.xml.searchandreplace.ui;

import org.jetbrains.plugins.xml.searchandreplace.replace.CapturePresentation;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.ConstraintController;

import java.awt.*;


public class CapturePresentationFactory {
  private static CapturePresentationFactory shared = new CapturePresentationFactory();

  private CapturePresentationFactory(){}

  public static CapturePresentationFactory instance() {
    return shared;
  }

  public CapturePresentation createPresentation(ConstraintController constraintController, String name) {
    CapturePresentation cp = new CapturePresentation();
    cp.setBackgroundColor(Color.GRAY);
    cp.setTextColor(Color.BLACK);
    cp.setName(name);
    return cp;
  }

  public void paredicateControllerIsDead(ConstraintController pc) {
    //uniqueColors.remove(pc);
  }
}
