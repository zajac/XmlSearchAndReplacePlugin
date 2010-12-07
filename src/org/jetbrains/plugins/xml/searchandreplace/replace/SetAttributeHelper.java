package org.jetbrains.plugins.xml.searchandreplace.replace;


import com.intellij.psi.PsiElement;
import org.jetbrains.plugins.xml.searchandreplace.search.Node;

import java.util.Map;

public interface SetAttributeHelper {
  String attributeName(Map<Node, PsiElement> match);
  String attributeValue(Map<Node, PsiElement> match);
}
