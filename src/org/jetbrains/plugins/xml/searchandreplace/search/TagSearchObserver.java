package org.jetbrains.plugins.xml.searchandreplace.search;

import com.intellij.psi.xml.XmlElement;
import org.jetbrains.plugins.xml.searchandreplace.search.Search;

public interface TagSearchObserver {
    void elementFound(Search search, XmlElement tag);
}
