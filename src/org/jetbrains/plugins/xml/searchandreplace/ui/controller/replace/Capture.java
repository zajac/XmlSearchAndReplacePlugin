package org.jetbrains.plugins.xml.searchandreplace.ui.controller.replace;

import com.intellij.psi.xml.XmlElement;
import org.jetbrains.plugins.xml.searchandreplace.replace.CapturePresentation;
import org.jetbrains.plugins.xml.searchandreplace.search.predicates.XmlElementPredicate;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.ConstraintController;

public abstract class Capture {
  private XmlElementPredicate predicate;
  private CapturePresentation myPresentation;

  public CapturePresentation presentation() {
    return myPresentation;
  }

  public void setPresentation(CapturePresentation myPresentation) {
    this.myPresentation = myPresentation;
  }

  public ConstraintController getConstraintController() {
    return constraintController;
  }

  protected ConstraintController constraintController;

  public Capture(ConstraintController constraintController) {
    this.constraintController = constraintController;
  }

  public XmlElementPredicate getPredicate() {
    return predicate;
  }

  public void setPredicate(XmlElementPredicate predicate) {
    this.predicate = predicate;
  }

  public abstract String value(XmlElement element);
}
