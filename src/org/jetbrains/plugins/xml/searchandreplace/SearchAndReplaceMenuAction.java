package org.jetbrains.plugins.xml.searchandreplace;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.*;
import com.intellij.psi.xml.XmlTag;
import java.util.ArrayList;

public class SearchAndReplaceMenuAction extends AnAction {

    static {
        System.out.println("static initializer");
    }


    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        Messages.showMessageDialog("Hello, world!!!", "dummy plugin", null);
        Project project = PlatformDataKeys.PROJECT.getData(anActionEvent.getDataContext());
        Editor editor = PlatformDataKeys.EDITOR.getData(anActionEvent.getDataContext());

        if (editor != null && project != null) {
            PsiFile psiFile = PsiDocumentManager.getInstance(project).getPsiFile(editor.getDocument());
            if (psiFile != null) {
                System.out.println("got a psifile!");
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
            } else {
                System.out.println("couldn't get psifile");
            }
        } else {
            System.out.println("couldn't get editor");
        }
    }

    @Override
    public void update(AnActionEvent anActionEvent) {
        //super.update(anActionEvent);
        Project project = PlatformDataKeys.PROJECT.getData(anActionEvent.getDataContext());
        anActionEvent.getPresentation().setEnabled(project != null);
    }
}
