package org.jetbrains.plugins.xml.searchandreplace.replace;

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
  public XmlElement getReplacementFor(XmlElement element, Map<Node, XmlElement> match) {
    if (element instanceof XmlTag) {
      XmlTag tag = (XmlTag)element;
      XmlElement replacement = replacementProvider.getReplacementFor(element, match);
      for (XmlTagChild child : tag.getValue().getChildren()) {
        child.delete();
      }
      Utils.insertElementIntoTag(replacement, tag, true);
      return tag;
    }
    return null;
  }
}
