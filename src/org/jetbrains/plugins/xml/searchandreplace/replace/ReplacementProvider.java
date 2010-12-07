package org.jetbrains.plugins.xml.searchandreplace.replace;

import com.intellij.psi.PsiElement;
import com.intellij.psi.xml.XmlElement;
import com.intellij.psi.xml.XmlTag;
import org.jetbrains.plugins.xml.searchandreplace.search.Node;

import java.util.Arrays;
import java.util.Map;

public abstract class ReplacementProvider {
  public abstract XmlTag getReplacementFor(XmlElement element, Map<Node, PsiElement> match);

  protected boolean isValid(XmlElement element) {
    return element.getParent() != null &&
                   element.getParent().getChildren() != null &&
                   Arrays.asList(element.getParent().getChildren()).indexOf(element) != -1;
  }

  public boolean alwaysReturnsTag() {
    return false;
  }
}
