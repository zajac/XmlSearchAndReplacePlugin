package org.jetbrains.plugins.xml.searchandreplace.search;

import com.intellij.psi.PsiElement;

public interface TagSearchObserver {
  void elementFound(Pattern pattern, PsiElement tag);
}
