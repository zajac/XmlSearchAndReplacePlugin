package org.jetbrains.plugins.xml.searchandreplace;

import com.intellij.openapi.util.Key;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiRecursiveElementVisitor;
import com.intellij.psi.xml.XmlElement;
import com.sun.tools.javac.util.Pair;
import org.jetbrains.plugins.xml.searchandreplace.predicates.TagPredicate;
import org.jetbrains.plugins.xml.searchandreplace.predicates.XmlElementPredicate;

import java.util.*;

public class Search extends PsiRecursiveElementVisitor {

    private static class SearchState {

        private final SearchPattern.KindOfPredicate kind;

        public static final SearchState CHECK_PARENTS = new SearchState(SearchPattern.KindOfPredicate.PARENT);
        public static final SearchState CHECK_CHILDREN = new SearchState(SearchPattern.KindOfPredicate.CHILD);
        public static final SearchState CHECK_THIS_ELEMENT = new SearchState(SearchPattern.KindOfPredicate.THIS_ELEMENT);
        public static final SearchState SUCCESS = new SearchState(null);

        private SearchState(SearchPattern.KindOfPredicate kind) {
            this.kind = kind;
        }

        public SearchPattern.KindOfPredicate kind() {
            return kind;
        }
    }

    private SearchState myState;

    private SearchPattern myPattern;

    private SearchPattern myLeftToCheck;

    private XmlElement candidate;

    private TagSearchDelegate myDelegate = new TagSearchDelegate() {
        public void foundTag(Search search, XmlElement tag) {
            System.out.println("found tag: " + tag);
        }
    };

    public Search(SearchPattern pattern) {
        myPattern = pattern;
        try {
            myLeftToCheck = (SearchPattern) pattern.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }

        myState = SearchState.CHECK_PARENTS;
    }

    private static SearchState calcState(SearchPattern leftToCheck) {
        if (!leftToCheck.selectByKind(SearchPattern.KindOfPredicate.CHILD).isEmpty()) {
            return SearchState.CHECK_CHILDREN;
        }
        if (!leftToCheck.selectByKind(SearchPattern.KindOfPredicate.THIS_ELEMENT).isEmpty()) {
            return SearchState.CHECK_THIS_ELEMENT;
        }
        if (!leftToCheck.selectByKind(SearchPattern.KindOfPredicate.PARENT).isEmpty()) {
            return SearchState.CHECK_PARENTS;
        }
        return SearchState.SUCCESS;
    }

    SearchState calcNewState(XmlElement xmlElement, SearchState state, SearchPattern oldLeftToCheck, SearchPattern outNewLeftToCheck) {
        if (state == SearchState.SUCCESS) {
            return calcNewState(xmlElement, SearchState.CHECK_THIS_ELEMENT, oldLeftToCheck, outNewLeftToCheck);
        }
        for (XmlElementPredicate p : oldLeftToCheck.selectByKind(state.kind())) {
            if (!p.apply(xmlElement)) {
                outNewLeftToCheck.add(state.kind(), p);
            }
        }
        return calcState(outNewLeftToCheck);
    }

    @Override
    public void visitElement(PsiElement element) {
        if (element instanceof XmlElement) {
            XmlElement xmlElement = (XmlElement)element;
            SearchPattern newLeftToCheck = SearchPattern.createEmpty();
            SearchState oldState = myState;
            myState = calcNewState(xmlElement, oldState, myLeftToCheck, newLeftToCheck);
            SearchPattern oldLeftToCheck = myLeftToCheck;
            myLeftToCheck = newLeftToCheck;
            if (myState == SearchState.SUCCESS) {
                getDelegate().foundTag(this, candidate);
            } else if (myState == SearchState.CHECK_CHILDREN && oldState == SearchState.CHECK_THIS_ELEMENT) {
                candidate = xmlElement;
            }
            super.visitElement(element);

            myLeftToCheck = oldLeftToCheck;
            myState = oldState;
        }
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
}
