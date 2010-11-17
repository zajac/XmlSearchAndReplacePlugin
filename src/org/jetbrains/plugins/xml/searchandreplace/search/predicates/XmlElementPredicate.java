package org.jetbrains.plugins.xml.searchandreplace.search.predicates;

import com.intellij.psi.xml.XmlElement;

public interface XmlElementPredicate {
  boolean apply(XmlElement element);
}

