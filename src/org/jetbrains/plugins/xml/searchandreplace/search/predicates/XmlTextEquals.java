package org.jetbrains.plugins.xml.searchandreplace.search.predicates;

import com.intellij.psi.xml.XmlElement;
import com.intellij.psi.xml.XmlText;

public class XmlTextEquals extends XmlElementPredicate {
  private String text;

  public XmlTextEquals(String text) {
    super();
    this.text = text;
  }

  @Override
  public boolean apply(XmlElement element) {
    return element instanceof XmlText && element.getText().equals(text);
  }
}
