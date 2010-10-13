package org.jetbrains.plugins.xml.searchandreplace.search.predicates;

import com.intellij.psi.xml.XmlElement;

public abstract class XmlElementPredicate {
    public abstract boolean apply(XmlElement element);
    public abstract String getDisplayName();
}

