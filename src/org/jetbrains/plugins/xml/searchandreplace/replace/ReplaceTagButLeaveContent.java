package org.jetbrains.plugins.xml.searchandreplace.replace;

import com.intellij.psi.xml.XmlElement;
import com.intellij.psi.xml.XmlTag;
import com.intellij.psi.xml.XmlTagChild;
import org.jetbrains.plugins.xml.searchandreplace.search.Node;

import java.util.Map;

public class ReplaceTagButLeaveContent extends ReplacementProvider {

  private ReplacementProvider replacementProvider;

  public ReplaceTagButLeaveContent(ReplacementProvider replacementProvider) {
    this.replacementProvider = replacementProvider;
  }

  @Override
  public XmlElement getReplacementFor(XmlElement element, Map<Node, XmlElement> match) {
    if (isValid(element) && element instanceof XmlTag) {
      XmlTag oldTag = (XmlTag) element;
      XmlElement newElement = replacementProvider.getReplacementFor(element, match);
      if (newElement instanceof XmlTag) {
        XmlTag newTag = (XmlTag) newElement;
        for (XmlTagChild child : oldTag.getValue().getChildren()) {
          Utils.insertElementIntoTag(child, newTag, false);
        }
        return newTag;
      }
    }
    return null;
  }
}
