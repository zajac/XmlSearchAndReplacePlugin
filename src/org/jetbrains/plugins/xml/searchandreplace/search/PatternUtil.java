package org.jetbrains.plugins.xml.searchandreplace.search;


import org.jetbrains.plugins.xml.searchandreplace.search.predicates.TagNameMatches;
import org.jetbrains.plugins.xml.searchandreplace.search.predicates.TagNameWildcardMatches;
import org.jetbrains.plugins.xml.searchandreplace.search.predicates.XmlElementPredicate;

import java.util.ArrayList;
import java.util.List;

public class PatternUtil {
  public static List<String> getSearchHint(Pattern pattern) {
    ArrayList<String> hint = new ArrayList<String>();
    for (Node node : pattern.getUnmatchedNodes()) {
      for (XmlElementPredicate p : node.getPredicate().flatten()) {
        String tagPattern = null;
        if (p instanceof TagNameMatches) {
          tagPattern = ((TagNameMatches) p).getTagPattern();
        } else if (p instanceof TagNameWildcardMatches) {
          tagPattern = ((TagNameWildcardMatches) p).getTagName();
        }
        if (tagPattern != null) {
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
