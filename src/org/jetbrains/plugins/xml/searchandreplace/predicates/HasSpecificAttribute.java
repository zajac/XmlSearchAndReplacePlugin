package org.jetbrains.plugins.xml.searchandreplace.predicates;

import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlTag;


public class HasSpecificAttribute extends TagPredicate {

    private static final String DISPLAY_NAME = "With Attribute";

    private AttributePredicate myAttributePredicate;

    public HasSpecificAttribute(AttributePredicate predicate) {
        this.myAttributePredicate = predicate;
    }

    public AttributePredicate getAttributePredicate() {
        return myAttributePredicate;
    }

    @Override
    public boolean applyToTag(XmlTag tag) {
        for (XmlAttribute attr : tag.getAttributes()) {
            if (myAttributePredicate.applyToAttribute(attr)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getDisplayName() {
        return DISPLAY_NAME;
    }
}
