package org.jetbrains.plugins.xml.searchandreplace.predicates;

import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlElement;

public abstract class AttributePredicate extends XmlElementPredicate {

    @Override
    public boolean apply(XmlElement element) {
        return element instanceof XmlAttribute && apply((XmlAttribute) element);
    }

    public abstract boolean apply(XmlAttribute a);
}
