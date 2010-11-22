package org.jetbrains.plugins.xml.searchandreplace.ui.controller.replace;

import com.intellij.psi.xml.XmlElement;
import org.jetbrains.plugins.xml.searchandreplace.replace.CapturePresentation;
import org.jetbrains.plugins.xml.searchandreplace.search.predicates.XmlElementPredicate;

public abstract class Capture {
  private XmlElementPredicate predicate;

  public XmlElementPredicate getPredicate() {
    return predicate;
  }

  public void setPredicate(XmlElementPredicate predicate) {
    this.predicate = predicate;
  }

  public abstract CapturePresentation presentation();
  public abstract String value(XmlElement element);
}