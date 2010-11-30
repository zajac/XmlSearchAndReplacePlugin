package org.jetbrains.plugins.xml.searchandreplace.ui.controller.replace;


import org.jetbrains.plugins.xml.searchandreplace.replace.ReplacementProvider;
import org.jetbrains.plugins.xml.searchandreplace.ui.CapturesManager;

import javax.swing.*;

public abstract class ReplacementController {

  private CapturesManager capturesManager = null;

  public CapturesManager getCapturesManager() {
    return capturesManager;
  }

  public void setCapturesManager(CapturesManager capturesManager) {
    this.capturesManager = capturesManager;
  }

  public abstract JPanel getView();

  protected abstract ReplacementProvider getReplacementProvider();

  public void viewDidAppear() {}
}
