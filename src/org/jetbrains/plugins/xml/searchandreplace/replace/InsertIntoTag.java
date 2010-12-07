package org.jetbrains.plugins.xml.searchandreplace.replace;

import com.intellij.psi.PsiElement;
import com.intellij.psi.xml.XmlElement;
import com.intellij.psi.xml.XmlTag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.plugins.xml.searchandreplace.search.Node;

import java.util.Map;

public class InsertIntoTag extends ReplacementProvider {

  private ReplacementProvider replacementProvider;
  private Anchor anchor;

  @Override
  public XmlTag getReplacementFor(XmlElement tag, Map<Node, PsiElement> match) {
    if (isValid(tag) && tag instanceof XmlTag) {
      XmlTag toInsert = replacementProvider.getReplacementFor(tag, match);
      XmlTag tagAsTag = (XmlTag)tag;
      if (toInsert != null) {
        Utils.insertCoupleOfElementsIntoTag(toInsert, tagAsTag, anchor == Anchor.BEGIN);
      }
      return tagAsTag;
    }
    return null;
  }

  public enum Anchor {BEGIN, END}

  public InsertIntoTag(@NotNull ReplacementProvider replacementProvider, @NotNull Anchor anchor) {
    this.replacementProvider = replacementProvider;
    this.anchor = anchor;
  }
}
