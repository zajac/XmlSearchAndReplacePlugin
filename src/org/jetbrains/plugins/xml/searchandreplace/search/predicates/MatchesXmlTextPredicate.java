package org.jetbrains.plugins.xml.searchandreplace.search.predicates;

import com.intellij.psi.xml.XmlElement;
import com.intellij.psi.xml.XmlText;

public class MatchesXmlTextPredicate extends XmlElementPredicate {
  private String text;

  public MatchesXmlTextPredicate(String text) {
    super();
    this.text = text;
  }

  @Override
  public boolean apply(XmlElement element) {
    return element instanceof XmlText && containsIgnoreWhitespace(element.getText(), text);
  }

  private static boolean containsIgnoreWhitespace(String s1, String s2) {
    return s1.equals(s2); //TODO
  }
}
