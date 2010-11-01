package org.jetbrains.plugins.xml.searchandreplace.ui.controller;

import com.intellij.psi.xml.XmlTag;
import org.jetbrains.plugins.xml.searchandreplace.search.predicates.TagPredicate;
import org.jetbrains.plugins.xml.searchandreplace.search.predicates.XmlElementPredicate;
import org.jetbrains.plugins.xml.searchandreplace.ui.view.TagPredicatePanel;

import javax.swing.*;

public class TagPredicateController extends PredicateTypeController {
    private TagPredicatePanel myView = new TagPredicatePanel();

    @Override
    public JPanel getView() {
        return myView;
    }

    public TagPredicateController() {
    }

    public TagPredicateController(Params p) {
        super(p);
    }

    @Override
    public XmlElementPredicate buildPredicate() {
        final String tagName = myView.getTagName();
        if (tagName.isEmpty()) {
            return null;
        }
        return decorateWithNotIfNeccessary(new TagPredicate() {
            @Override
            public boolean applyToTag(XmlTag tag) {
                return tag.getName().equals(tagName);
            }

        });
    }
}
