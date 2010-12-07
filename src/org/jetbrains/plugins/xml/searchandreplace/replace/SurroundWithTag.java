package org.jetbrains.plugins.xml.searchandreplace.replace;

import com.intellij.psi.PsiElement;
import com.intellij.psi.xml.XmlElement;
import com.intellij.psi.xml.XmlTag;
import com.intellij.psi.xml.XmlTagChild;
import org.jetbrains.plugins.xml.searchandreplace.search.Node;

import java.util.Map;

public class SurroundWithTag extends ReplacementProvider {
  private ReplacementProvider replacementProvider;

  public SurroundWithTag(ReplacementProvider replacementProvider) {
    super();
    this.replacementProvider = replacementProvider;
  }

  @Override
  public XmlTag getReplacementFor(XmlElement element, Map<Node, PsiElement> match) {
    XmlTag replacement = replacementProvider.getReplacementFor(element, match);
    XmlTagChild[] children = replacement.getValue().getChildren();
    if (children.length != 0) {
      if (children[0] instanceof  XmlTag) {
        XmlTag tag = (XmlTag) children[0];
        tag.add(element);
        return tag;
      }
    }
    return null;
  }
}
