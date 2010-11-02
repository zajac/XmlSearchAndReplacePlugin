package org.jetbrains.plugins.xml.searchandreplace.ui.controller.replace;

import org.jetbrains.plugins.xml.searchandreplace.replace.RemoveAttribute;
import org.jetbrains.plugins.xml.searchandreplace.replace.ReplacementProvider;
import org.jetbrains.plugins.xml.searchandreplace.ui.view.replace.RemoveAttributeView;

import javax.swing.*;

public class RemoveAttributeController extends ReplacementController {

  private RemoveAttributeView myView = new RemoveAttributeView();

  @Override
  public JPanel getView() {
    return myView;
  }

  @Override
  protected ReplacementProvider getReplacementProvider() {
    return new RemoveAttribute(myView.getName());
  }

  @Override
  public String toString() {
    return "Remove attribute";
  }
}
