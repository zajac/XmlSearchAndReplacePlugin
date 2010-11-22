package org.jetbrains.plugins.xml.searchandreplace.search.predicates;

import com.intellij.psi.xml.XmlElement;
import com.intellij.psi.xml.XmlTag;

public class DirectlyContains extends TagPredicate {

  private XmlElementPredicate nestedPredicate;

  public DirectlyContains(XmlElementPredicate predicate) {
    this.nestedPredicate = predicate;
  }

  @Override
  public String toString() {
    return "Directly contains " + nestedPredicate.toString();
  }

  @Override
  public boolean applyToTag(XmlTag tag) {
    for (XmlElement e : tag.getValue().getChildren()) {
      if (nestedPredicate.apply(e)) {
        return true;
      }
    }
    return false;
  }

  public XmlElementPredicate getNested() {
    return nestedPredicate;
  }
}
