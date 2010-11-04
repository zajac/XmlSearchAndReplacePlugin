package org.jetbrains.plugins.xml.searchandreplace.replace;

import com.intellij.psi.xml.XmlElement;
import com.intellij.psi.xml.XmlTag;
import com.intellij.psi.xml.XmlTagChild;
import com.intellij.util.ArrayUtil;

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
}
