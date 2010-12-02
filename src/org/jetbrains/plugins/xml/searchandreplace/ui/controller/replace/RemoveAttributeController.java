package org.jetbrains.plugins.xml.searchandreplace.ui.controller.replace;

import org.jetbrains.plugins.xml.searchandreplace.replace.RemoveAttribute;
import org.jetbrains.plugins.xml.searchandreplace.replace.ReplacementProvider;

public class RemoveAttributeController extends SetAttributeController {


  public RemoveAttributeController() {
    getView().getValueField().setVisible(false);
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
