package org.jetbrains.plugins.xml.searchandreplace.search.predicates;

import com.intellij.psi.xml.XmlTag;

public class NoOneTag extends TagPredicate {
  @Override
  public boolean applyToTag(XmlTag tag) {
    return false;
  }
}
