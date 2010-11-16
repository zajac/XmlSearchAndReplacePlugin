package org.jetbrains.plugins.xml.searchandreplace.search;

import com.intellij.psi.xml.XmlElement;

public interface TagSearchObserver {
  void elementFound(Pattern pattern, XmlElement tag);
}
