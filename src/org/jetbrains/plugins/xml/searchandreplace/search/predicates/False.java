package org.jetbrains.plugins.xml.searchandreplace.search.predicates;

import com.intellij.psi.xml.XmlElement;

public class False implements XmlElementPredicate {

  @Override
  public String toString() {
    return "FALSE";
  }

  public boolean apply(XmlElement element) {
    return false;
  }
}
