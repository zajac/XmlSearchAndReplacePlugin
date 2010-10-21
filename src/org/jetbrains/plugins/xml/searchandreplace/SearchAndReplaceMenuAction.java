package org.jetbrains.plugins.xml.searchandreplace;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.event.EditorMouseEvent;
import com.intellij.openapi.editor.event.EditorMouseListener;
import com.intellij.openapi.editor.markup.HighlighterLayer;
import com.intellij.openapi.editor.markup.HighlighterTargetArea;
import com.intellij.openapi.editor.markup.RangeHighlighter;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.xml.XmlElement;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import org.jetbrains.plugins.xml.searchandreplace.search.Pattern;
import org.jetbrains.plugins.xml.searchandreplace.search.TagSearchObserver;
import org.jetbrains.plugins.xml.searchandreplace.search.predicates.TagPredicate;
import org.jetbrains.plugins.xml.searchandreplace.search.predicates.XmlElementPredicate;
import org.jetbrains.plugins.xml.searchandreplace.ui.MainDialog;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class SearchAndReplaceMenuAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {

        Project project = PlatformDataKeys.PROJECT.getData(anActionEvent.getDataContext());
        final Editor editor = PlatformDataKeys.EDITOR.getData(anActionEvent.getDataContext());

        if (editor != null && project != null) {
            PsiFile psiFile = PsiDocumentManager.getInstance(project).getPsiFile(editor.getDocument());
            if (psiFile != null) {
                TagSearchObserver myObserver = new TagSearchObserver() {
                    private void highlightElement(PsiElement tag) {
                        TextRange textRange = tag.getTextRange();
                        TextAttributes ta = new TextAttributes();
                        ta.setBackgroundColor(Color.CYAN);
                        final RangeHighlighter rh = editor.getMarkupModel().addRangeHighlighter(
                                textRange.getStartOffset(),
                                textRange.getEndOffset(),
                                HighlighterLayer.SELECTION + 1,
                                ta,
                                HighlighterTargetArea.EXACT_RANGE);                        
                        editor.addEditorMouseListener(new EditorMouseListener() {
                            public void mousePressed(EditorMouseEvent e) {

                            }

                            public void mouseClicked(EditorMouseEvent e) {
                                editor.getMarkupModel().removeHighlighter(rh);
                                editor.removeEditorMouseListener(this);
                            }

                            public void mouseReleased(EditorMouseEvent e) {

                            }

                            public void mouseEntered(EditorMouseEvent e) {
                                
                            }

                            public void mouseExited(EditorMouseEvent e) {

                            }
                        });
                    }

                    public void elementFound(XmlElement tag) {
                        if (tag != null && tag instanceof XmlTag && tag.getFirstChild() != null && tag.getFirstChild().getNextSibling() != null) {
                            if (tag.getFirstChild() != null) {
                                PsiElement tagName = tag.getFirstChild().getNextSibling();
                                highlightElement(tagName);
                            }
                        } else {
                            highlightElement(tag);
                        }
                    }
                };

                if (psiFile instanceof XmlFile) {
                    XmlElement root = (XmlElement) psiFile;
                    Pattern myPattern = createTestPattern();
                    myPattern.match(root, myObserver);
                }

                VirtualFile virtualFile = psiFile.getVirtualFile();
                if (virtualFile != null) {
                    Module module = ProjectRootManager.getInstance(project).getFileIndex().getModuleForFile(virtualFile);
                    if (module != null) {
                        new MainDialog(project, module).show();
                    }
                }
            }
        } else {
            System.out.println("couldn't get editor");
        }
    }

    private HashSet<Pattern.Node> l(Pattern.Node... nodes) {
        return new HashSet<Pattern.Node>(Arrays.asList(nodes));
    }

    private Pattern createTestPattern() {
        Pattern.Node n1 = new Pattern.Node(createTestPredicate("TAG1"), false);
        Pattern.Node n2 = new Pattern.Node(createTestPredicate("TAG2"), false);
        Pattern.Node n3 = new Pattern.Node(createTestPredicate("TAG3"), false);
        Pattern.Node n4 = new Pattern.Node(createTestPredicate("TAG4"), false);
        Pattern.Node n5 = new Pattern.Node(createTestPredicate("TAG5"), false);
        Pattern.Node n0 = new Pattern.Node(createTestPredicate("TAG0"), false);
        Pattern.Node n = new Pattern.Node(createTestPredicate("TAG"), true);
        n.setChildren(l(n3, n4));
        n1.setChildren(l(n));
        n2.setChildren(l(n));
        n3.setChildren(l(n5));
        n0.setChildren(l(n2));
        return new Pattern(l(n0, n, n1, n2, n3, n4, n5));
    }

    private XmlElementPredicate createTestPredicate(final String tagName) {
        return new TagPredicate() {

            public String toString() {
                return tagName;
            }

            @Override
            public boolean applyToTag(XmlTag tag) {
                return tag.getName().equals(tagName);
            }
            @Override
            public String getDisplayName() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }
        };
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
                        tags.add((XmlTag) element);
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
