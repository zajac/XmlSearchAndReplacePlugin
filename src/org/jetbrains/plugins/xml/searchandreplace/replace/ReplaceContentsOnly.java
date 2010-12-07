package org.jetbrains.plugins.xml.searchandreplace.replace;

import com.intellij.psi.PsiElement;
import com.intellij.psi.xml.XmlElement;
import com.intellij.psi.xml.XmlTag;
import com.intellij.psi.xml.XmlTagChild;
import org.jetbrains.plugins.xml.searchandreplace.search.Node;

import java.util.Map;


public class ReplaceContentsOnly extends ReplacementProvider {

  private ReplacementProvider replacementProvider;

  public ReplaceContentsOnly(ReplacementProvider replacementProvider) {
    this.replacementProvider = replacementProvider;
  }

  @Override
  public XmlTag getReplacementFor(XmlElement t, Map<Node, PsiElement> match) {
    XmlTag replacement = replacementProvider.getReplacementFor(t, match);
    if (t instanceof XmlTag) {
      XmlTag tag = (XmlTag) t;
      for (XmlTagChild child : tag.getValue().getChildren()) {
        child.delete();
      }
      Utils.insertCoupleOfElementsIntoTag(replacement, tag, true);
      return tag;
    }
    return null;
  }
}
