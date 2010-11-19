package org.jetbrains.plugins.xml.searchandreplace.search.predicates;

import com.intellij.psi.xml.XmlElement;

import java.util.HashSet;
import java.util.Set;

public abstract class XmlElementPredicate {
  public abstract boolean apply(XmlElement element);

  public Set<XmlElementPredicate> flatten() {
    HashSet<XmlElementPredicate> xmlElementPredicates = new HashSet<XmlElementPredicate>();
    xmlElementPredicates.add(this);
    return xmlElementPredicates;
  }
}

