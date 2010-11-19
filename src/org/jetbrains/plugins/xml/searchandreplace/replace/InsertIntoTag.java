package org.jetbrains.plugins.xml.searchandreplace.replace;

import com.intellij.psi.xml.XmlElement;
import com.intellij.psi.xml.XmlTag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.plugins.xml.searchandreplace.search.Node;

import java.util.Map;

public class InsertIntoTag extends ReplacementProvider {

  private ReplacementProvider replacementProvider;
  private Anchor anchor;

  @Override
  public XmlElement getReplacementFor(XmlElement element, Map<Node, XmlElement> match) {
     if (isValid(element) && element instanceof XmlTag) {
      XmlTag tag = (XmlTag) element;
      XmlElement toInsert = replacementProvider.getReplacementFor(element, match);
      if (toInsert != null) {
        Utils.insertElementIntoTag(toInsert, tag, anchor == Anchor.BEGIN);
      }
    }
    return element;
  }

  public enum Anchor {BEGIN, END}

  public InsertIntoTag(@NotNull ReplacementProvider replacementProvider, @NotNull Anchor anchor) {
    this.replacementProvider = replacementProvider;
    this.anchor = anchor;
  }
}
