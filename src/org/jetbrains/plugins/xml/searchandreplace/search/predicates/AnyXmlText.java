package org.jetbrains.plugins.xml.searchandreplace.search.predicates;

import com.intellij.psi.xml.XmlElement;
import com.intellij.psi.xml.XmlText;

/**
* Created by IntelliJ IDEA.
* User: zajac
* Date: 02.12.10
* Time: 20:36
* To change this template use File | Settings | File Templates.
*/
public class AnyXmlText extends XmlElementPredicate {
  @Override
  public boolean apply(XmlElement element) {
    return element instanceof XmlText;
  }
}
