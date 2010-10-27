package org.jetbrains.plugins.xml.searchandreplace.ui;

import com.intellij.psi.xml.XmlTag;
import org.jetbrains.plugins.xml.searchandreplace.search.Pattern;
import org.jetbrains.plugins.xml.searchandreplace.search.predicates.Not;
import org.jetbrains.plugins.xml.searchandreplace.search.predicates.TagPredicate;
import org.jetbrains.plugins.xml.searchandreplace.search.predicates.XmlElementPredicate;

import javax.swing.*;

public class Inside implements PredicateType {

    protected enum Params {NOT}

    Params p = null;

    public Inside() {}

    protected Inside(Params p) {
        this.p = p;
    }

    @Override
    public String toString() {
        return p == Params.NOT ? "Not inside" : "Inside";
    }

    public PredicateTypeController createNewController() {

        return new PredicateTypeController() {
            private TagPredicatePanel view = new TagPredicatePanel();

            @Override
            public JPanel getView() {
                return view;
            }

            @Override
            public XmlElementPredicate buildPredicate() {
                final String tagName = view.getTagName();
                if (tagName.isEmpty()) {
                    return null;
                }
                XmlElementPredicate tagPredicate = new TagPredicate() {
                    @Override
                    public boolean applyToTag(XmlTag tag) {
                        return tag.getName().equals(tagName);
                    }

                    @Override
                    public String getDisplayName() {
                        return null;  //To change body of implemented methods use File | Settings | File Templates.
                    }
                };
                if (p == Params.NOT) {
                    tagPredicate = new Not(tagPredicate);
                }
                return tagPredicate;
            }
        };
    }

    public void addNodeToPattern(Pattern p, Pattern.Node node, Pattern.Node parent) {
        p.getAllNodes().add(node);
        node.getChildren().add(parent);
    }
}
