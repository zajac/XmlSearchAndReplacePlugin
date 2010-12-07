package org.jetbrains.plugins.xml.searchandreplace.search.predicates;

import com.intellij.psi.xml.XmlTag;


public class TagNameWildcardMatches extends TagPredicate {
  private String tagName;

  public TagNameWildcardMatches(String tagName) {
    super();
    this.tagName = tagName;
  }

  @Override
  public boolean applyToTag(XmlTag tag) {
    return Utils.wildcardMatches(tag.getName(), tagName);
  }

  @Override
  public String toString() {
    return tagName;
  }

  public String getTagName() {
    return tagName;
  }
}
