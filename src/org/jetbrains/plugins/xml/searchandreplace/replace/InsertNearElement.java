package org.jetbrains.plugins.xml.searchandreplace.replace;


import com.intellij.psi.xml.XmlElement;
import org.jetbrains.plugins.xml.searchandreplace.search.Node;

import java.util.Map;

public class InsertNearElement extends ReplacementProvider {

  private ReplacementProvider replacementProvider;
  private Anchor anchor;

  public enum Anchor {BEFORE, AFTER}

  public InsertNearElement(ReplacementProvider replacementProvider, Anchor anchor) {
    this.replacementProvider = replacementProvider;
    this.anchor = anchor;
  }

  @Override
  public XmlElement getReplacementFor(XmlElement element, Map<Node, XmlElement> match) {
    if (isValid(element)) {
      XmlElement toInsert = replacementProvider.getReplacementFor(element, match);
      if (toInsert != null) {
        if (anchor == Anchor.AFTER) {
          element.getParent().addAfter(toInsert, element);
        } else {
          element.getParent().addBefore(toInsert, element);
        }
      }
    }
    return element;
  }
}
