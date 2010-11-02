package org.jetbrains.plugins.xml.searchandreplace.ui.controller.replace;


import com.intellij.psi.xml.XmlElement;
import org.jetbrains.plugins.xml.searchandreplace.replace.ReplacementProvider;

import javax.swing.*;

public abstract class ReplacementController {

  public abstract JPanel getView();

  protected abstract ReplacementProvider getReplacementProvider();

  public final void doReplace(XmlElement what) {
    XmlElement replacement = getReplacementProvider().getReplacementFor(what);
    if (replacement != null) {
      what.replace(replacement);
    }
  }
}
