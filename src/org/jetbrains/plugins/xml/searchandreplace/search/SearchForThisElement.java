package org.jetbrains.plugins.xml.searchandreplace.search;

import com.intellij.psi.xml.XmlElement;

import java.util.ArrayList;
import java.util.List;

public class SearchForThisElement extends Search {
    public SearchForThisElement(SearchPattern pattern) {
        super(pattern);
    }

    @Override
    public List<Search> searchesToContinue(XmlElement element) {
        ArrayList<Search> searches = new ArrayList<Search>();
        if (getPattern().getThisElementPredicate().apply(element)) {
            searches.add(new SearchByChildrenPredicates(getPattern(), element));
        } else {
            searches.add(this);
        }
        return searches;
    }
}
