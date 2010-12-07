package org.jetbrains.plugins.xml.searchandreplace.replace;


import com.intellij.psi.PsiElement;
import com.intellij.psi.xml.XmlElement;
import com.intellij.psi.xml.XmlTag;
import com.intellij.psi.xml.XmlTagChild;
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
  public XmlTag getReplacementFor(XmlElement element, Map<Node, PsiElement> match) {
    if (isValid(element)) {
      XmlTag parent = (XmlTag) element.getParent();
      XmlTag replacement = replacementProvider.getReplacementFor(element, match);
      XmlTagChild[] children = replacement.getValue().getChildren();
      XmlElement first = children[0], last = children[children.length-1];
      if (anchor == Anchor.AFTER) {
        parent.addRangeAfter(first, last, element);
      } else {
        parent.addRangeBefore(first, last, element);
      }
    }
    return null;
  }
}
