package org.jetbrains.plugins.xml.searchandreplace.search.predicates;

import com.intellij.psi.xml.XmlElement;

public class And implements XmlElementPredicate {

  private XmlElementPredicate myP1;
  private XmlElementPredicate myP2;

  public And(XmlElementPredicate p1, XmlElementPredicate p2) {
    myP1 = p1;
    myP2 = p2;
  }

  public boolean apply(XmlElement element) {
    return (myP1 == null || myP1.apply(element)) && (myP2 == null || myP2.apply(element));
  }

}
