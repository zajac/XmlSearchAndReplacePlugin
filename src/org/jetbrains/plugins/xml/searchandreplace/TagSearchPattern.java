package org.jetbrains.plugins.xml.searchandreplace;

import com.sun.tools.corba.se.idl.ParameterEntry;
import com.sun.tools.javac.util.Pair;
import org.jetbrains.plugins.xml.searchandreplace.predicates.TagPredicate;

import javax.tools.JavaFileObject;
import java.util.HashSet;
import java.util.Set;

public class TagSearchPattern implements Cloneable {

    public enum KindOfPredicate {PARENT, THIS_ELEMENT, CHILD}

    private HashSet<TagPredicate> parentsPredicates = new HashSet<TagPredicate>();
    private TagPredicate thisElementPredicate;
    private HashSet<TagPredicate> childrenPredicates = new HashSet<TagPredicate>();

    public Set<TagPredicate> getParentsPredicates() {
        return parentsPredicates;
    }

    public TagPredicate getThisElementPredicate() {
        return thisElementPredicate;
    }

    public Set<TagPredicate> getChildrenPredicates() {
        return childrenPredicates;
    }

    public void setParentsPredicates(HashSet<TagPredicate> parentsPredicates) {
        this.parentsPredicates = parentsPredicates;
    }

    public void setThisElementPredicate(TagPredicate thisElementPredicate) {
        this.thisElementPredicate = thisElementPredicate;
    }

    public void setChildrenPredicates(HashSet<TagPredicate> childrenPredicates) {
        this.childrenPredicates = childrenPredicates;
    }

    public KindOfPredicate whatKind(TagPredicate p) {
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

    public TagSearchPattern(HashSet<TagPredicate> parentsPredicates, TagPredicate thisElementPredicate, HashSet<TagPredicate> childrenPredicates) {
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
        TagSearchPattern tsp = (TagSearchPattern)super.clone();
        tsp.thisElementPredicate = thisElementPredicate;
        tsp.childrenPredicates = (HashSet<TagPredicate>) childrenPredicates.clone();
        tsp.parentsPredicates = (HashSet<TagPredicate>) parentsPredicates.clone();
        return tsp;
    }

    public void add(KindOfPredicate kind, TagPredicate p) {
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

    public void remove(TagPredicate p) {
        if (thisElementPredicate == p) {
            thisElementPredicate = null;
        } else if (childrenPredicates.contains(p)) {
            childrenPredicates.remove(p);
        } else if (parentsPredicates.contains(p)) {
            parentsPredicates.remove(p);
        }
    }

    public Set<TagPredicate> selectByKind(KindOfPredicate kind) {
        switch(kind) {
            case PARENT:
                return parentsPredicates;
            case CHILD:
                return childrenPredicates;
            default:
                Set<TagPredicate> r = new HashSet<TagPredicate>();
                r.add(thisElementPredicate);
                return r;
        }
    }
}
