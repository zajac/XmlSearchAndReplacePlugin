package org.jetbrains.plugins.xml.searchandreplace.replace;

import com.intellij.psi.PsiElement;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlElement;
import com.intellij.psi.xml.XmlTag;
import org.jetbrains.plugins.xml.searchandreplace.search.Node;

import java.util.Map;

public class SetAttribute extends ReplacementProvider {
  private SetAttributeHelper helper;

  public SetAttribute(SetAttributeHelper helper) {
    this.helper = helper;
  }

  @Override
  public XmlTag getReplacementFor(XmlElement element, Map<Node, PsiElement> match) {
    if (isValid(element) && element instanceof XmlTag) {
      XmlTag tag = (XmlTag) element;
      String value = helper.attributeValue(match);
      String name = helper.attributeName(match);
      if (value == null) {
        XmlAttribute attribute = tag.getAttribute(name);
        if (attribute != null) {
          attribute.delete();
        }
      } else {
        tag.setAttribute(name, value);
      }
      return (XmlTag) element;
    }
    return null;
  }
}
