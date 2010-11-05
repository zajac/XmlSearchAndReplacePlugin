package org.jetbrains.plugins.xml.searchandreplace.replace;

import com.intellij.psi.xml.XmlElement;
import com.intellij.psi.xml.XmlTag;
import com.intellij.psi.xml.XmlTagChild;

public class ReplaceTagButLeaveContent extends ReplacementProvider {

  private ReplacementProvider replacementProvider;

  public ReplaceTagButLeaveContent(ReplacementProvider replacementProvider) {
    this.replacementProvider = replacementProvider;
  }

  @Override
  public XmlElement getReplacementFor(XmlElement element) {
    if (isValid(element) && element instanceof XmlTag) {
      XmlTag oldTag = (XmlTag) element;
      XmlElement newElement = replacementProvider.getReplacementFor(element);
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
