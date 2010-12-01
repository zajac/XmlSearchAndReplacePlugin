package org.jetbrains.plugins.xml.searchandreplace.search.predicates;

import com.intellij.psi.xml.XmlTag;

public class TagNameMatches extends TagPredicate {

  public String getTagPattern() {
    return tagPattern;
  }

  private final String tagPattern;

  public TagNameMatches(String tagPattern) {
    this.tagPattern = tagPattern;
  }

  @Override
  public String toString() {
    return tagPattern;
  }

  @Override
  public boolean applyToTag(XmlTag tag) {
    return tag.getName().matches(tagPattern);
  }

}
