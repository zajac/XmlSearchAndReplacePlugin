package org.jetbrains.plugins.xml.searchandreplace;

import org.jetbrains.plugins.xml.searchandreplace.predicates.TagPredicate;
import org.jetbrains.plugins.xml.searchandreplace.predicates.XmlElementPredicate;

import java.util.HashSet;
import java.util.Set;

public class SearchPattern implements Cloneable {

    public static SearchPattern createEmpty() {
        return new SearchPattern(null, null, null);
    }

    public enum KindOfPredicate {PARENT, THIS_ELEMENT, CHILD}

    private HashSet<XmlElementPredicate> parentsPredicates = new HashSet<XmlElementPredicate>();
    private XmlElementPredicate thisElementPredicate;
    private HashSet<XmlElementPredicate> childrenPredicates = new HashSet<XmlElementPredicate>();

    public Set<XmlElementPredicate> getParentsPredicates() {
        return parentsPredicates;
    }

    public XmlElementPredicate getThisElementPredicate() {
        return thisElementPredicate;
    }

    public Set<XmlElementPredicate> getChildrenPredicates() {
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

    public KindOfPredicate whatKind(XmlElementPredicate p) {
        if (thisElementPredicate == p) {
            return KindOfPredicate.THIS_ELEMENT;
        }
        if (parentsPredicates.contains(p)) {
            return KindOfPredicate.PARENT;
        }
        if (childrenPredicates.contains(p)) {
            return KindOfPredicate.CHILD;
        }
        return null;
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

    public void add(KindOfPredicate kind, XmlElementPredicate p) {
        switch(kind) {
            case PARENT:
                parentsPredicates.add(p);
                break;
            case CHILD:
                childrenPredicates.add(p);
                break;
            case THIS_ELEMENT:
                assert thisElementPredicate == null;
                thisElementPredicate = p;
        }
    }

    public void remove(XmlElementPredicate p) {
        if (thisElementPredicate == p) {
            thisElementPredicate = null;
        } else if (childrenPredicates.contains(p)) {
            childrenPredicates.remove(p);
        } else if (parentsPredicates.contains(p)) {
            parentsPredicates.remove(p);
        }
    }

    public Set<XmlElementPredicate> selectByKind(KindOfPredicate kind) {
        switch(kind) {
            case PARENT:
                return parentsPredicates;
            case CHILD:
                return childrenPredicates;
            default:
                Set<XmlElementPredicate> r = new HashSet<XmlElementPredicate>();
                r.add(thisElementPredicate);
                return r;
        }
    }
}
