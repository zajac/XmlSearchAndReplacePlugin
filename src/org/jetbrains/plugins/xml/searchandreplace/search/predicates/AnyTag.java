package org.jetbrains.plugins.xml.searchandreplace.search.predicates;

import com.intellij.psi.xml.XmlTag;

/**
 * Created by IntelliJ IDEA.
 * User: zajac
 * Date: 01.12.10
 * Time: 22:27
 * To change this template use File | Settings | File Templates.
 */
public class AnyTag extends TagPredicate {
  @Override
  public boolean applyToTag(XmlTag element) {
    return true;
  }
}
