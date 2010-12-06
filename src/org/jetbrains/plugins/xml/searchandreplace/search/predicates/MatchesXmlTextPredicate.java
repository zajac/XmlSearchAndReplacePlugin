package org.jetbrains.plugins.xml.searchandreplace.search.predicates;

import com.intellij.psi.xml.XmlElement;
import com.intellij.psi.xml.XmlText;
import com.intellij.util.text.StringTokenizer;

import java.util.ArrayList;

public class MatchesXmlTextPredicate extends XmlElementPredicate {
  private String text;

  private ArrayList<String> words = new ArrayList<String>();

  public MatchesXmlTextPredicate(String text) {
    super();
    this.text = text;
    StringTokenizer tokenizer = new StringTokenizer(text);
    while (tokenizer.hasMoreElements()) {
      words.add(tokenizer.nextElement());
    }
  }

  @Override
  public boolean apply(XmlElement element) {
    return element instanceof XmlText && containsIgnoreWhitespace(element.getText());
  }

  private boolean containsIgnoreWhitespace(String s) {
    StringTokenizer tokenizer = new StringTokenizer(s);
    int wordIndex = 0;
    while (wordIndex < words.size() && tokenizer.hasMoreElements()) {
      String next = tokenizer.nextElement();
      String word = words.get(wordIndex);
      int indexOF = next.indexOf(word);
      int length = word.length();
      int nextsLen = next.length();
      if ((wordIndex == 0 && indexOF != -1 && indexOF + length == nextsLen) ||
              (wordIndex == words.size()-1 && indexOF == 0) ||
              (indexOF != -1 && nextsLen == length)) {
        wordIndex++;
      } else {
        wordIndex = 0;
      }
    }
    if (wordIndex == words.size()) {
      return true;
    }
    return false;
  }

  @Override
  public String toString() {
    return text;
  }
}
