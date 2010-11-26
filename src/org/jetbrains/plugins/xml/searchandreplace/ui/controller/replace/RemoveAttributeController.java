package org.jetbrains.plugins.xml.searchandreplace.ui.controller.replace;

import org.jetbrains.plugins.xml.searchandreplace.replace.RemoveAttribute;
import org.jetbrains.plugins.xml.searchandreplace.replace.ReplacementProvider;
import org.jetbrains.plugins.xml.searchandreplace.ui.view.replace.RemoveAttributeView;

public class RemoveAttributeController extends SetAttributeController {

  private RemoveAttributeView myView = new RemoveAttributeView();

  @Override
  public RemoveAttributeView getView() {
    return myView;
  }

  @Override
  protected ReplacementProvider getReplacementProvider() {
    return new RemoveAttribute(new MySetAttributeHelper());
  }

  @Override
  public String toString() {
    return "Remove attribute";
  }
}
