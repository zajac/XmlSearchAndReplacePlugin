package org.jetbrains.plugins.xml.searchandreplace.search;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiRecursiveElementVisitor;
import com.intellij.psi.xml.XmlElement;
import org.jetbrains.plugins.xml.searchandreplace.search.predicates.Not;
import org.jetbrains.plugins.xml.searchandreplace.search.predicates.XmlElementPredicate;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public abstract class Search extends PsiRecursiveElementVisitor {

    private SearchPattern myPattern;

    private List<Search> subSearches = new ArrayList<Search>();

    private TagSearchObserver myObserver = new TagSearchObserver() {
        public void elementFound(Search search, XmlElement tag) {
            System.out.println(this + "found tag: " + tag);
        }
    };

    public static Search createSearchForPattern(final SearchPattern pattern) {
        assert pattern.getThisElementPredicate() != null;
        Search result = new Search(pattern) {
            @Override
            public List<Search> searchesToContinue(XmlElement element) {
                ArrayList<Search> searches = new ArrayList<Search>();
                if (!pattern.getParentsPredicates().isEmpty()) {
                    searches.add(new SearchByParentsPredicates(pattern));
                } else {
                    searches.add(new SearchForThisElement(pattern));
                }
                return searches;
            }
        };
        result.subSearches.add(result);
        return result;
    }

    protected Search(SearchPattern pattern) {
        myPattern = pattern;
    }

    public SearchPattern getPattern() {
        return myPattern;
    }

    public TagSearchObserver getObserver() {
        return myObserver;
    }

    public void setDelegate(TagSearchObserver myObserver) {
        this.myObserver = myObserver;
    }

    public abstract List<Search> searchesToContinue(XmlElement element);

    @Override
    public final void visitElement(PsiElement element) {
        if (element instanceof XmlElement) {
            XmlElement xmlElement = (XmlElement)element;
            List<Search> searchesToContinue = new ArrayList<Search>();
            for(Search s : subSearches) {
                List<Search> c = s.searchesToContinue(xmlElement);
                searchesToContinue.addAll(c);
                for (Search toContinue : searchesToContinue) {
                    toContinue.setDelegate(getObserver());
                }
            }
            List<Search> backup = subSearches;
            subSearches = searchesToContinue;
            super.visitElement(element);
            subSearches = backup;
        }


    }

    protected static boolean isEmptyOrContainsOnlyNOT(Set<XmlElementPredicate> leftToCheck) {
        boolean isEmpty = true;
        for (XmlElementPredicate p : leftToCheck) {
            if (! (p instanceof Not)) {
                isEmpty = false;
                break;
            }
        }
        return isEmpty;
    }
}
