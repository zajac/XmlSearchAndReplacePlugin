package org.jetbrains.plugins.xml.searchandreplace.ui.predicatetypes;

import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlTag;
import org.jetbrains.plugins.xml.searchandreplace.search.predicates.TagPredicate;
import org.jetbrains.plugins.xml.searchandreplace.search.predicates.XmlElementPredicate;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.PredicateTypeController;

import javax.swing.*;

public class WithoutAttribute extends WithAttribute {
    public PredicateTypeController createNewController() {
        final JPanel view = new JPanel();
        final JTextField attributeField = new JTextField();
        view.add(attributeField);
        return new PredicateTypeController() {

            @Override
            public JPanel getView() {
                return view;
            }

            @Override
            public XmlElementPredicate buildPredicate() {
                return new TagPredicate() {
                    @Override
                    public boolean applyToTag(XmlTag tag) {
                        String attrName = attributeField.getText();
                        if (attrName == null) return true;
                        for (XmlAttribute attr : tag.getAttributes()) {
                            if (attr.getName().equals(attrName)) {
                                return false;
                            }
                        }
                        return true;
                    }
                };
            }
        };
    }

    public String toString() {
        return "Without attribute";
    }
}
