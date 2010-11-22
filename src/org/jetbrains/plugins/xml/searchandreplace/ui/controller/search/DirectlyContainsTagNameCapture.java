package org.jetbrains.plugins.xml.searchandreplace.ui.controller.search;

import com.intellij.psi.xml.XmlElement;
import com.intellij.psi.xml.XmlTag;
import org.jetbrains.plugins.xml.searchandreplace.search.predicates.DirectlyContains;

public class DirectlyContainsTagNameCapture extends TagNameCapture{
  public DirectlyContainsTagNameCapture(PredicateController predicateController) {
    super(predicateController);
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
