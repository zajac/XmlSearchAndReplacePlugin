package org.jetbrains.plugins.xml.searchandreplace.search.predicates;

import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlElement;

public abstract class AttributePredicate implements XmlElementPredicate {

  public boolean apply(XmlElement element) {
    return element instanceof XmlAttribute && applyToAttribute((XmlAttribute) element);
  }

  public abstract boolean applyToAttribute(XmlAttribute a);
}
