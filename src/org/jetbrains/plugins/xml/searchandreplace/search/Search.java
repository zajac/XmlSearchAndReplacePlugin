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

    private TagSearchDelegate myDelegate = new TagSearchDelegate() {
        public void foundTag(Search search, XmlElement tag) {
            System.out.println(this + "found tag: " + tag);
        }
    };

    public static Search createSearchForPattern(SearchPattern pattern) {
        assert pattern.getThisElementPredicate() != null;
        Search result;
        if (!pattern.getParentsPredicates().isEmpty()) {
            result = new SearchByParentsPredicates(pattern);
        } else {
            result = new SearchForThisElement(pattern);
        }
        result.subSearches.add(result);
        return result;
    }

    protected Search(SearchPattern pattern) {
        myPattern = pattern;
    }

    public SearchPattern getPattern() {
        return myPattern;
    }

    public TagSearchDelegate getDelegate() {
        return myDelegate;
    }

    public void setDelegate(TagSearchDelegate myDelegate) {
        this.myDelegate = myDelegate;
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
            }
            subSearches = searchesToContinue;
        }
        super.visitElement(element);
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
