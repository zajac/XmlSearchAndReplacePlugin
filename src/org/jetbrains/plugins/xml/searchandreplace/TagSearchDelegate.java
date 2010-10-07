package org.jetbrains.plugins.xml.searchandreplace;

import com.intellij.psi.xml.XmlTag;

public interface TagSearchDelegate {
    void foundTag(TagSearch search, XmlTag tag);
}
