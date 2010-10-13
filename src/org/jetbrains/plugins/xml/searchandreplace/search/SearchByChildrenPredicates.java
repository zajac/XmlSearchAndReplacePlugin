package org.jetbrains.plugins.xml.searchandreplace.search;

import com.intellij.psi.xml.XmlElement;
import org.jetbrains.plugins.xml.searchandreplace.search.predicates.Not;
import org.jetbrains.plugins.xml.searchandreplace.search.predicates.XmlElementPredicate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SearchByChildrenPredicates extends Search {

    private HashSet<XmlElementPredicate> leftToCheck;
    private XmlElement candidate;

    private SearchByChildrenPredicates(SearchPattern pattern, HashSet<XmlElementPredicate> leftToCheck, XmlElement candidate) {
        super(pattern);
        this.leftToCheck = leftToCheck;
        this.candidate = candidate;
    }

    public SearchByChildrenPredicates(SearchPattern pattern, XmlElement candidate) {
        super(pattern);
        this.candidate = candidate;
        this.leftToCheck = pattern.getChildrenPredicates();
    }

    @Override
    public List<Search> searchesToContinue(XmlElement element) {
        List<Search> searches = new ArrayList<Search>();
        if (getPattern().getThisElementPredicate().apply(element)) {
            searches.add(new SearchByChildrenPredicates(getPattern(), (HashSet<XmlElementPredicate>) getPattern().getChildrenPredicates().clone(), element));
        }
        if (candidate != null) {
            ArrayList<XmlElementPredicate> succeeded = new ArrayList<XmlElementPredicate>();
            for (XmlElementPredicate p : leftToCheck) {
                if (!(p instanceof Not)) {
                    if (p.apply(element)) {
                        succeeded.add(p);
                    }
                } else {
                    if (!p.apply(element)) {
                        return new ArrayList<Search>();
                    }
                }
            }
            HashSet<XmlElementPredicate> newLeftToCheck = (HashSet<XmlElementPredicate>) leftToCheck.clone();
            newLeftToCheck.removeAll(succeeded);
            if (isEmptyOrContainsOnlyNOT(newLeftToCheck)) {
                getDelegate().foundTag(this, candidate);
                candidate = null;
                searches.add(new SearchForThisElement(getPattern()));
            } else if (succeeded.isEmpty()) {
                searches.add(new SearchByChildrenPredicates(getPattern(), newLeftToCheck, candidate));
            }
        }
        return searches;
    }

}
