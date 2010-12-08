package org.jetbrains.plugins.xml.searchandreplace;


import com.intellij.psi.PsiElement;
import org.jetbrains.plugins.xml.searchandreplace.search.Node;

import java.util.Map;

public class SearchResult {
  Map<Node, PsiElement> match;
  boolean isInjected = false;

  public SearchResult(Map<Node, PsiElement> match, boolean injected) {
    this.match = match;
    isInjected = injected;
  }

  public Map<Node, PsiElement> getMatch() {
    return match;
  }

  public boolean isInjected() {
    return isInjected;
  }
}
