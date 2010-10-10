package org.jetbrains.plugins.xml.searchandreplace;

import com.intellij.openapi.util.Key;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiRecursiveElementVisitor;
import com.intellij.psi.xml.XmlTag;
import com.sun.tools.javac.util.Pair;
import org.jetbrains.plugins.xml.searchandreplace.predicates.TagPredicate;

import java.util.*;

public class TagSearch extends PsiRecursiveElementVisitor {

    private static class TagSearchState {

        private final TagSearchPattern.KindOfPredicate kind;

        public static final TagSearchState CHECK_PARENTS = new TagSearchState(TagSearchPattern.KindOfPredicate.PARENT);
        public static final TagSearchState CHECK_CHILDREN = new TagSearchState(TagSearchPattern.KindOfPredicate.CHILD);
        public static final TagSearchState CHECK_THIS_ELEMENT = new TagSearchState(TagSearchPattern.KindOfPredicate.THIS_ELEMENT);

        private TagSearchState(TagSearchPattern.KindOfPredicate kind) {
            this.kind = kind;
        }

        public static TagSearchState map(TagSearchPattern.KindOfPredicate kind) {
            for (TagSearchState state :  TagSearchState.class.getEnumConstants()) {
                if (state.kind == kind) {
                    return state;
                }
            }
            return null;
        }

        public TagSearchPattern.KindOfPredicate kind() {
            return kind;
        }
    }

    private static final Key<List<Pair<TagSearchPattern.KindOfPredicate, TagPredicate>>> USER_INFO_KEY = Key.create(TagSearch.class.getName());

    private TagSearchState myState;

    private TagSearchPattern myPattern;

    private TagSearchPattern myLeftToCheck;

    private XmlTag candidate;

    private TagSearchDelegate myDelegate = new TagSearchDelegate() {
        public void foundTag(TagSearch search, XmlTag tag) {
            System.out.println("found tag: " + tag);
        }
    };

    public TagSearch(TagSearchPattern pattern) {
        myPattern = pattern;
        try {
            myLeftToCheck = (TagSearchPattern) pattern.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }

        myState = TagSearchState.CHECK_PARENTS;
    }

    @Override
    public void visitElement(PsiElement element) {
        if (element instanceof XmlTag) {
            XmlTag tag = (XmlTag)element;
            for (TagPredicate p : myLeftToCheck.selectByKind(myState.kind())) {
                if (p.apply(tag)) {
                    List<Pair<TagSearchPattern.KindOfPredicate, TagPredicate>> l = tag.getUserData(USER_INFO_KEY);
                    if (l == null) {
                        l = new ArrayList<Pair<TagSearchPattern.KindOfPredicate, TagPredicate>>();
                        tag.putUserData(USER_INFO_KEY, l);
                    }
                    Pair<TagSearchPattern.KindOfPredicate, TagPredicate> pair = new Pair<TagSearchPattern.KindOfPredicate, TagPredicate>(myLeftToCheck.whatKind(p), p);
                    l.add(pair);
                    myLeftToCheck.remove(p);
                }
            }
            myState = calcCurrentState();
        }
        super.visitElement(element);
        if (element instanceof XmlTag) {
            XmlTag tag = (XmlTag)element;
            List<Pair<TagSearchPattern.KindOfPredicate, TagPredicate>> userData = tag.getUserData(USER_INFO_KEY);
            if (userData != null) {
                for (Pair<TagSearchPattern.KindOfPredicate, TagPredicate> pair : userData) {
                    myLeftToCheck.add(pair.fst, pair.snd);
                }
                tag.putUserData(USER_INFO_KEY, null);
            }
            myState = calcCurrentState();
        }
    }

    private TagSearchState calcCurrentState() {
        if (!myLeftToCheck.selectByKind(TagSearchPattern.KindOfPredicate.CHILD).isEmpty()) {
            return TagSearchState.CHECK_CHILDREN;
        }
        if (!myLeftToCheck.selectByKind(TagSearchPattern.KindOfPredicate.THIS_ELEMENT).isEmpty()) {
            return TagSearchState.CHECK_THIS_ELEMENT;
        }
        return TagSearchState.CHECK_PARENTS;
    }

    public TagSearchPattern getPattern() {
        return myPattern;
    }

    public TagSearchDelegate getDelegate() {
        return myDelegate;
    }

    public void setDelegate(TagSearchDelegate myDelegate) {
        this.myDelegate = myDelegate;
    }
}
