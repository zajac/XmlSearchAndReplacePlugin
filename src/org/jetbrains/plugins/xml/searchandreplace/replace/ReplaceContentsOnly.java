package org.jetbrains.plugins.xml.searchandreplace.replace;

import com.intellij.psi.xml.XmlElement;

/**
 * Created by IntelliJ IDEA.
 * User: zajac
 * Date: Nov 2, 2010
 * Time: 11:09:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class ReplaceContentsOnly extends ReplacementProvider {

  private ReplacementProvider replacementProvider;

  public ReplaceContentsOnly(ReplacementProvider replacementProvider) {
    this.replacementProvider = replacementProvider;
  }

  @Override
  public XmlElement getReplacementFor(XmlElement element) {
    return null;  //To change body of implemented methods use File | Settings | File Templates. //TODO
  }
}
