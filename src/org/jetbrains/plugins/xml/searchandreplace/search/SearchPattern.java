package org.jetbrains.plugins.xml.searchandreplace.search;

import org.jetbrains.plugins.xml.searchandreplace.search.predicates.XmlElementPredicate;

import java.util.HashSet;
import java.util.Set;

public class SearchPattern implements Cloneable {

    public static SearchPattern createEmpty() {
        return new SearchPattern(null, null, null);
    }

    private HashSet<XmlElementPredicate> parentsPredicates = new HashSet<XmlElementPredicate>();
    private XmlElementPredicate thisElementPredicate;
    private HashSet<XmlElementPredicate> childrenPredicates = new HashSet<XmlElementPredicate>();

    public HashSet<XmlElementPredicate> getParentsPredicates() {
        return parentsPredicates;
    }

    public XmlElementPredicate getThisElementPredicate() {
        return thisElementPredicate;
    }

    public HashSet<XmlElementPredicate> getChildrenPredicates() {
        return childrenPredicates;
    }

    public void setParentsPredicates(HashSet<XmlElementPredicate> parentsPredicates) {
        this.parentsPredicates = parentsPredicates;
    }

    public void setThisElementPredicate(XmlElementPredicate thisElementPredicate) {
        this.thisElementPredicate = thisElementPredicate;
    }

    public void setChildrenPredicates(HashSet<XmlElementPredicate> childrenPredicates) {
        this.childrenPredicates = childrenPredicates;
    }

    public SearchPattern(HashSet<XmlElementPredicate> parentsPredicates, XmlElementPredicate thisElementPredicate, HashSet<XmlElementPredicate> childrenPredicates) {
        if (parentsPredicates != null) {
            this.parentsPredicates = parentsPredicates;
        }
        if (childrenPredicates != null) {
            this.childrenPredicates = childrenPredicates;
        }
        this.thisElementPredicate = thisElementPredicate;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        SearchPattern tsp = (SearchPattern)super.clone();
        tsp.thisElementPredicate = thisElementPredicate;
        tsp.childrenPredicates = (HashSet<XmlElementPredicate>) childrenPredicates.clone();
        tsp.parentsPredicates = (HashSet<XmlElementPredicate>) parentsPredicates.clone();
        return tsp;
    }
}
