package org.jetbrains.plugins.xml.searchandreplace.search.predicates;

import com.intellij.psi.xml.XmlElement;

public class Not extends XmlElementPredicate {

  private static final String NOT = "NOT";

  private XmlElementPredicate predicate;

  public Not(XmlElementPredicate predicate) {
    this.predicate = predicate;
  }
  
  public boolean apply(XmlElement element) {
    return !predicate.apply(element);
  }

  public XmlElementPredicate getPredicate() {
    return predicate;
  }
}
