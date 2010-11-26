package org.jetbrains.plugins.xml.searchandreplace.replace;


import com.intellij.psi.xml.XmlElement;
import org.jetbrains.plugins.xml.searchandreplace.search.Node;

import java.util.Map;

public interface SetAttributeHelper {
  String attributeName(Map<Node, XmlElement> match);
  String attributeValue(Map<Node, XmlElement> match);
}
