package org.jetbrains.plugins.xml.searchandreplace.search;


import org.jetbrains.plugins.xml.searchandreplace.search.predicates.TagNameMatches;
import org.jetbrains.plugins.xml.searchandreplace.search.predicates.XmlElementPredicate;

import java.util.ArrayList;
import java.util.List;

public class PatternUtil {
  public static List<String> getSearchHint(Pattern pattern) {
    ArrayList<String> hint = new ArrayList<String>();
    for (Node node : pattern.getUnmatchedNodes()) {
      for (XmlElementPredicate p : node.getPredicate().flatten()) {
        if (p instanceof TagNameMatches) {
          String tagPattern = ((TagNameMatches) p).getTagPattern();
          boolean ok = true;
          for (int i = 0; i < tagPattern.length(); ++i) {
            char c = tagPattern.charAt(i);
            if (!Character.isLetterOrDigit(c) && c != '-' && c != '_') {
              ok = false;
            }
          }
          if (ok) {
            hint.add(tagPattern);
          }
        }
      }
    }
    return hint;
  }
}
