package org.jetbrains.plugins.xml.searchandreplace.replace;

import com.intellij.lang.Language;
import com.intellij.openapi.project.Project;
import com.intellij.psi.XmlElementFactory;
import com.intellij.psi.xml.XmlElement;
import com.intellij.psi.xml.XmlTag;
import com.intellij.psi.xml.XmlTagChild;
import com.intellij.util.IncorrectOperationException;

public class Utils {

  private static final String STUB_ROOT = "s-t-u-b";

  public static void insertCoupleOfElementsIntoTag(XmlTag e, XmlTag tag, boolean asFirstOrLast) {
    XmlTagChild[] children = tag.getValue().getChildren();
    XmlTagChild[] eChildren = e.getValue().getChildren();
    if (eChildren.length == 0) return;
    XmlTagChild first = eChildren[0];
    XmlTagChild last = eChildren[eChildren.length - 1];
    if (children.length == 0) {
      tag.addRange(first, last);
    } else if(asFirstOrLast) {
      tag.addRangeBefore(first, last, children[0]);
    } else {
      tag.addRangeAfter(first, last, children[children.length-1]);
    }
  }

  public static XmlTag createXmlElement(Language myLanguage, Project myProject, String myText) {
    myText = "<" + STUB_ROOT + ">" + myText + "</" + STUB_ROOT + ">";
    XmlElement result = null;
    XmlElementFactory factory = XmlElementFactory.getInstance(myProject);
    if (myText != null && factory != null) {
      try {
        result = factory.createTagFromText(myText, myLanguage);
      } catch (IncorrectOperationException e) {
        e.printStackTrace();
      }
    }
    return (XmlTag) result;
  }
}
