package org.jetbrains.plugins.xml.searchandreplace.replace;

import com.intellij.psi.PsiElement;
import org.jetbrains.plugins.xml.searchandreplace.search.Node;

import java.util.Map;

public class RemoveAttribute extends SetAttribute {


  public RemoveAttribute(final SetAttributeHelper helper) {
    super(new SetAttributeHelper() {
      @Override
      public String attributeName(Map<Node, PsiElement> match) {
        return helper.attributeName(match);
      }

      @Override
      public String attributeValue(Map<Node, PsiElement> match) {
        return null;
      }
    });
  }
}
