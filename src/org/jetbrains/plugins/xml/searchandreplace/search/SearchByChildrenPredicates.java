package org.jetbrains.plugins.xml.searchandreplace.search;

import com.intellij.psi.xml.XmlElement;
import org.jetbrains.plugins.xml.searchandreplace.search.predicates.Not;
import org.jetbrains.plugins.xml.searchandreplace.search.predicates.XmlElementPredicate;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SearchByChildrenPredicates extends Search {

    private Set<XmlElementPredicate> leftToCheck;
    private XmlElement candidate;

    private SearchByChildrenPredicates(SearchPattern pattern, Set<XmlElementPredicate> leftToCheck, XmlElement candidate) {
        super(pattern);
        this.leftToCheck = leftToCheck;
        this.candidate = candidate;
    }

    public SearchByChildrenPredicates(SearchPattern pattern, XmlElement candidate) {
        super(pattern);
        this.candidate = candidate;
        this.leftToCheck = (Set<XmlElementPredicate>) pattern.getChildrenPredicates().clone();
    }

    @Override
    public List<Search> searchesToContinue(XmlElement element) {
        ArrayList<XmlElementPredicate> succeeded = new ArrayList<XmlElementPredicate>();
        for (XmlElementPredicate p : leftToCheck) {
            if (! (p instanceof Not)) {
                if (p.apply(element)) {
                    succeeded.add(p);
                }
            } else {
                if (!p.apply(element)) {
                    return new ArrayList<Search>();
                }
            }
        }
        List<Search> searches = new ArrayList<Search>();
        leftToCheck.removeAll(succeeded);
        if(isEmptyOrContainsOnlyNOT(leftToCheck)) {
            getDelegate().foundTag(this, candidate);
        } else if (succeeded.isEmpty()) {
            searches.add(this);
        } else {
            searches.add(new SearchByChildrenPredicates(getPattern(), leftToCheck, candidate));
        }
        if (getPattern().getThisElementPredicate().apply(element)) {
            searches.add(new SearchByChildrenPredicates(getPattern(), (Set<XmlElementPredicate>) getPattern().getChildrenPredicates().clone(), element));
        }
        return searches;
    }

}
