package org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.captures;

import com.intellij.psi.xml.XmlElement;
import com.intellij.psi.xml.XmlTag;
import org.jetbrains.plugins.xml.searchandreplace.search.predicates.DirectlyContains;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.ConstraintController;

public class DirectlyContainsTagNameCapture extends TagNameCapture{
  public DirectlyContainsTagNameCapture(ConstraintController constraintController) {
    super(constraintController);
  }

  @Override
  public String value(XmlElement element) {
    if(element instanceof XmlTag) {
      for (XmlElement e : ((XmlTag) element).getValue().getChildren()) {
        if (((DirectlyContains)getPredicate()).getNested().apply(e)) {
          if (e instanceof XmlTag) {
            return ((XmlTag) e).getName();
          } else {
            return e.toString();
          }
        }
      }
    }
    return null;
  }
}
