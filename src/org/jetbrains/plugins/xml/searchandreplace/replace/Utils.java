package org.jetbrains.plugins.xml.searchandreplace.replace;

import com.intellij.lang.Language;
import com.intellij.openapi.project.Project;
import com.intellij.psi.XmlElementFactory;
import com.intellij.psi.xml.XmlElement;
import com.intellij.psi.xml.XmlTag;
import com.intellij.psi.xml.XmlTagChild;
import com.intellij.util.ArrayUtil;
import com.intellij.util.IncorrectOperationException;

public class Utils {

  public static XmlElement insertElementIntoTag(XmlElement e, XmlTag tag, boolean asFirstOrLast) {
    XmlTagChild[] children = tag.getSubTags();
    if (children.length == 0) {
      children = tag.getValue().getChildren();
    }
    if (children.length == 0) {
      return (XmlElement) tag.add(e);
    } else if (asFirstOrLast) {
      return (XmlElement) tag.addBefore(e, children[0]);
    } else {
      return (XmlElement) tag.addAfter(e, ArrayUtil.getLastElement(children));
    }
  }

  public static XmlElement createXmlElement(Language myLanguage, Project myProject, String myText) {
    XmlElement result;
    XmlElementFactory factory = XmlElementFactory.getInstance(myProject);
    if (myText != null && factory != null) {
      try {
        result = factory.createTagFromText(myText, myLanguage);
        if (((XmlTag)result).getName().isEmpty()) {
          result = factory.createDisplayText(myText);
        }
      } catch (IncorrectOperationException e) {
        try {
          result = factory.createDisplayText(myText);
        } catch (IncorrectOperationException ex) {
          result = null;
        }
      }
      return result;
    } else {
      return null;
    }
  }
}
