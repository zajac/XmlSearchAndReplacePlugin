package org.jetbrains.plugins.xml.searchandreplace.search.predicates;

import com.intellij.psi.xml.XmlTag;

/**
 * Created by IntelliJ IDEA.
 * User: zajac
 * Date: 02.12.10
 * Time: 20:33
 * To change this template use File | Settings | File Templates.
 */
public class TagNameEquals extends TagPredicate {
  private String tagName;

  public TagNameEquals(String tagName) {
    super();
    this.tagName = tagName;
  }

  @Override
  public boolean applyToTag(XmlTag tag) {
    return tag.getName().equals(tagName);
  }

  @Override
  public String toString() {
    return tagName;
  }
}
