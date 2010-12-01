package org.jetbrains.plugins.xml.searchandreplace.ui.controller.replace;


import com.intellij.openapi.components.PersistentStateComponent;
import org.jetbrains.plugins.xml.searchandreplace.replace.ReplacementProvider;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.captures.CapturesManager;

import javax.swing.*;

public abstract class ReplacementController implements PersistentStateComponent <ReplacementControllerState>{

  private CapturesManager capturesManager = null;

  public CapturesManager getCapturesManager() {
    return capturesManager;
  }

  public void setCapturesManager(CapturesManager capturesManager) {
    this.capturesManager = capturesManager;
  }

  public abstract JPanel getView();

  protected abstract ReplacementProvider getReplacementProvider();
}