package org.jetbrains.plugins.xml.searchandreplace.search;


import com.intellij.psi.xml.XmlElement;
import org.jetbrains.plugins.xml.searchandreplace.search.predicates.XmlElementPredicate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SearchByParentsPredicates extends Search {

    private HashSet<XmlElementPredicate> leftToCheck;

    private SearchByParentsPredicates(SearchPattern pattern, HashSet<XmlElementPredicate> leftToCheck) {
        super(pattern);
        this.leftToCheck = leftToCheck;
    }

    public SearchByParentsPredicates(SearchPattern pattern) {
        super(pattern);
        this.leftToCheck = pattern.getParentsPredicates();
    }

    @Override
    public List<Search> searchesToContinue(XmlElement element) {
        List<XmlElementPredicate> succeeded = new ArrayList<XmlElementPredicate>();
        for (XmlElementPredicate p : leftToCheck) {
            if (p.apply(element)) {
                succeeded.add(p);
            }
        }
        HashSet<XmlElementPredicate> newLeftToCheck = (HashSet<XmlElementPredicate>) leftToCheck.clone();
        newLeftToCheck.removeAll(succeeded);
        ArrayList<Search> searches = new ArrayList<Search>();
        if (isEmptyOrContainsOnlyNOT(newLeftToCheck)) {
            searches.add(new SearchForThisElement(getPattern()));
        } else {
            searches.add(new SearchByParentsPredicates(getPattern(), newLeftToCheck));
        }
        return searches;
    }
}
