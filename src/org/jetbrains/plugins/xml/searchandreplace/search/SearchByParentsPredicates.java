package org.jetbrains.plugins.xml.searchandreplace.search;


import com.intellij.psi.xml.XmlElement;
import org.jetbrains.plugins.xml.searchandreplace.search.predicates.XmlElementPredicate;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SearchByParentsPredicates extends Search {

    private Set<XmlElementPredicate> leftToCheck;

    private SearchByParentsPredicates(SearchPattern pattern, Set<XmlElementPredicate> leftToCheck) {
        super(pattern);
        this.leftToCheck = leftToCheck;
    }

    public SearchByParentsPredicates(SearchPattern pattern) {
        super(pattern);
        this.leftToCheck = (Set<XmlElementPredicate>) pattern.getParentsPredicates().clone();
    }

    @Override
    public List<Search> searchesToContinue(XmlElement element) {
        List<XmlElementPredicate> succeeded = new ArrayList<XmlElementPredicate>();
        for (XmlElementPredicate p : leftToCheck) {
            if (p.apply(element)) {
                succeeded.add(p);
            }
        }
        leftToCheck.removeAll(succeeded);
        ArrayList<Search> searches = new ArrayList<Search>();
        if (isEmptyOrContainsOnlyNOT(leftToCheck)) {
            searches.add(new SearchForThisElement(getPattern()));
        } else {
            searches.add(this);
        }
        return searches;
    }
}
