package org.jetbrains.plugins.xml.searchandreplace.replace;

import com.intellij.psi.PsiElement;
import com.intellij.psi.xml.XmlElement;
import com.intellij.psi.xml.XmlTag;
import com.intellij.psi.xml.XmlTagChild;
import org.jetbrains.plugins.xml.searchandreplace.search.Node;

import java.util.Map;

public class ReplaceWithContents extends ReplacementProvider {

  ReplacementProvider replacementProvider;

  public ReplaceWithContents(ReplacementProvider replacementProvider) {
    this.replacementProvider = replacementProvider;
  }

  @Override
  public XmlTag getReplacementFor(XmlElement element, Map<Node, PsiElement> match) {
    if (isValid(element)) {
      XmlTag replacement = replacementProvider.getReplacementFor(element, match);
      XmlTag parent = (XmlTag) element.getParent();
      if (replacement != null) {
        XmlTagChild[] children = replacement.getValue().getChildren();
        if (children.length > 0) {
          parent.addRangeAfter(children[0], children[children.length-1], element);
        }
      }
      element.delete();
    }
    return null;
  }
}
