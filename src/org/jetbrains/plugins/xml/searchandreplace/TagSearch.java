package org.jetbrains.plugins.xml.searchandreplace;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiRecursiveElementVisitor;
import com.intellij.psi.xml.XmlTag;

public class TagSearch extends PsiRecursiveElementVisitor {

    private TagSearchPattern myPattern;

    private TagSearchDelegate myDelegate = new TagSearchDelegate() {
        public void foundTag(TagSearch search, XmlTag tag) {
            System.out.println("found tag: " + tag);
        }
    };

    public TagSearch(TagSearchPattern pattern) {
        myPattern = pattern;
    }

    @Override
    public void visitElement(PsiElement element) {
        //TODO
        super.visitElement(element);
    }

    public TagSearchPattern getPattern() {
        return myPattern;
    }

    public TagSearchDelegate getMyDelegate() {
        return myDelegate;
    }

    public void setMyDelegate(TagSearchDelegate myDelegate) {
        this.myDelegate = myDelegate;
    }
}
