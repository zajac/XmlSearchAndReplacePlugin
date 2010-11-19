package org.jetbrains.plugins.xml.searchandreplace.replace;

import com.intellij.psi.xml.XmlElement;
import org.jetbrains.plugins.xml.searchandreplace.search.Node;

import java.util.Arrays;
import java.util.Map;

public abstract class ReplacementProvider {
  public abstract XmlElement getReplacementFor(XmlElement element, Map<Node, XmlElement> match);

  protected boolean isValid(XmlElement element) {
    return element.getParent() != null &&
                   element.getParent().getChildren() != null &&
                   Arrays.asList(element.getParent().getChildren()).indexOf(element) != -1;
  }

  public boolean alwaysReturnsTag() {
    return false;
  }
}
