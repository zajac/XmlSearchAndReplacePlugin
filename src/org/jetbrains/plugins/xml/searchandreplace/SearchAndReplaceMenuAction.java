package org.jetbrains.plugins.xml.searchandreplace;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.xml.XmlTag;

import java.util.ArrayList;

public class SearchAndReplaceMenuAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {

        Project project = PlatformDataKeys.PROJECT.getData(anActionEvent.getDataContext());
        Editor editor = PlatformDataKeys.EDITOR.getData(anActionEvent.getDataContext());

        if (editor != null && project != null) {
            PsiFile psiFile = PsiDocumentManager.getInstance(project).getPsiFile(editor.getDocument());

        } else {
            System.out.println("couldn't get editor");
        }
    }

    private void testingStuff(Project project, PsiFile psiFile) {
        if (psiFile != null) {
            class RecursivePsiFilteringVisitor extends PsiRecursiveElementVisitor {

                public ArrayList<XmlTag> getTags() {
                    return tags;
                }

                private ArrayList<XmlTag> tags = new ArrayList<XmlTag>();

                public RecursivePsiFilteringVisitor() {
                    super(true);
                }

                @Override
                public void visitElement(PsiElement element) {
                    if (element instanceof XmlTag) {
                        System.out.println("found tag: " + element.toString());
                        tags.add((XmlTag)element);
                    }
                    super.visitElement(element);
                }
            }
            final RecursivePsiFilteringVisitor recursivePsiFilteringVisitor = new RecursivePsiFilteringVisitor();
            psiFile.accept(recursivePsiFilteringVisitor);
            final XmlTag myDummyTag = XmlElementFactory.getInstance(project).createTagFromText("<MyDummyTag/>");
            ApplicationManager.getApplication().runWriteAction(new Runnable() {
                public void run() {
                    for (XmlTag a : recursivePsiFilteringVisitor.getTags()) {
                        a.getParent().addAfter(myDummyTag, a);
                    }
                }
            });
        }
    }

    @Override
    public void update(AnActionEvent anActionEvent) {
        Project project = PlatformDataKeys.PROJECT.getData(anActionEvent.getDataContext());
        anActionEvent.getPresentation().setEnabled(project != null);
    }
}
