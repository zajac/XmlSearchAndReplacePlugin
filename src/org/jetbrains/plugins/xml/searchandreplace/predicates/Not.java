package org.jetbrains.plugins.xml.searchandreplace.predicates;

import com.intellij.psi.xml.XmlElement;

public class Not extends XmlElementPredicate {

    private static final String NOT = "NOT";

    private XmlElementPredicate predicate;

    public Not(XmlElementPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public boolean apply(XmlElement element) {
        return !predicate.apply(element);
    }

    @Override
    public String getDisplayName() {
        return NOT + " " + predicate.getDisplayName();
    }

    public XmlElementPredicate getPredicate() {
        return predicate;
    }
}
