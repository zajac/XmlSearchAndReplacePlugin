package org.jetbrains.plugins.xml.searchandreplace.search.predicates;

import com.intellij.psi.xml.XmlElement;
import com.intellij.psi.xml.XmlTag;

public abstract class TagPredicate extends XmlElementPredicate implements Cloneable {
    @Override
    public boolean apply(XmlElement element) {
        return element instanceof XmlTag && applyToTag((XmlTag) element);
    }

    public abstract boolean applyToTag(XmlTag tag);
}
