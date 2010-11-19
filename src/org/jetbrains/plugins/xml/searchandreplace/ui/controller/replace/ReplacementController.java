package org.jetbrains.plugins.xml.searchandreplace.ui.controller.replace;


import org.jetbrains.plugins.xml.searchandreplace.replace.ReplacementProvider;

import javax.swing.*;

public abstract class ReplacementController {

  public abstract JPanel getView();

  protected abstract ReplacementProvider getReplacementProvider();

  public void viewDidAppear() {}
}
