package org.jetbrains.plugins.xml.searchandreplace.replace;

import com.intellij.psi.PsiElement;
import com.intellij.psi.xml.XmlElement;
import com.intellij.psi.xml.XmlTag;
import org.jetbrains.plugins.xml.searchandreplace.search.Node;

import java.util.Map;

public class ReplaceTagButLeaveContent extends ReplacementProvider {

  private ReplacementProvider replacementProvider;

  public ReplaceTagButLeaveContent(ReplacementProvider replacementProvider) {
    this.replacementProvider = replacementProvider;
  }

  @Override
  public XmlTag getReplacementFor(XmlElement element, Map<Node, PsiElement> match) {
    if (isValid(element) && element instanceof XmlTag) {
      XmlTag xmlTag = (XmlTag) element;
      XmlTag newElement = replacementProvider.getReplacementFor(element, match);
      if (newElement.getSubTags().length != 1) {
        return null;
      }
      newElement = newElement.getSubTags()[0];
      Utils.insertCoupleOfElementsIntoTag(xmlTag, newElement, false);
      return newElement;
    }
    return null;
  }
}
