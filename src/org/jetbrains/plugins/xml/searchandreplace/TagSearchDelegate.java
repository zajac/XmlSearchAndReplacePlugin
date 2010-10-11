package org.jetbrains.plugins.xml.searchandreplace;

import com.intellij.psi.xml.XmlElement;
import com.intellij.psi.xml.XmlTag;

public interface TagSearchDelegate {
    void foundTag(Search search, XmlElement tag);
}
