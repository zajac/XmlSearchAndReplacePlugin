package org.jetbrains.plugins.xml.searchandreplace.ui.controller.replace;


import org.jetbrains.plugins.xml.searchandreplace.replace.ReplacementProvider;
import org.jetbrains.plugins.xml.searchandreplace.replace.SetAttribute;
import org.jetbrains.plugins.xml.searchandreplace.ui.view.replace.SetAttributeView;

import javax.swing.*;

public class SetAttributeController extends ReplacementController {

  private SetAttributeView myView = new SetAttributeView();

  @Override
  public JPanel getView() {
    return myView;
  }

  @Override
  protected ReplacementProvider getReplacementProvider() {
    return new SetAttribute(myView.getName(), myView.getValue());
  }

  @Override
  public String toString() {
    return "Set attribute";
  }
}
