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
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.psi.xml.XmlElement;
import com.intellij.psi.xml.XmlTag;
import org.jetbrains.plugins.xml.searchandreplace.search.TagSearchObserver;
import org.jetbrains.plugins.xml.searchandreplace.search.predicates.TagPredicate;
import org.jetbrains.plugins.xml.searchandreplace.search.predicates.XmlElementPredicate;
import org.jetbrains.plugins.xml.searchandreplace.search.Search;
import org.jetbrains.plugins.xml.searchandreplace.search.SearchPattern;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;

public class SearchAndReplaceMenuAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {

        Project project = PlatformDataKeys.PROJECT.getData(anActionEvent.getDataContext());
        final Editor editor = PlatformDataKeys.EDITOR.getData(anActionEvent.getDataContext());

        if (editor != null && project != null) {
            PsiFile psiFile = PsiDocumentManager.getInstance(project).getPsiFile(editor.getDocument());
            if (psiFile != null) {

                HashSet<XmlElementPredicate> parents = new HashSet<XmlElementPredicate>();
                parents.add(new TagPredicate() {

                    @Override
                    public String getDisplayName() {
                        return null;  //To change body of implemented methods use File | Settings | File Templates.
                    }

                    @Override
                    public boolean applyToTag(XmlTag tag) {
                        return tag.getName().equals("myParentTag");
                    }
                });
                parents.add(new TagPredicate() {

                    @Override
                    public boolean applyToTag(XmlTag tag) {
                        return tag.getName().equals("myParentTag2");
                    }

                    @Override
                    public String getDisplayName() {
                        return null;  //To change body of implemented methods use File | Settings | File Templates.
                    }
                });

                HashSet<XmlElementPredicate> children = new HashSet<XmlElementPredicate>();
                children.add(new TagPredicate() {

                    @Override
                    public boolean applyToTag(XmlTag tag) {
                        return tag.getName().equals("myChildTag");
                    }

                    @Override
                    public String getDisplayName() {
                        return null;  //To change body of implemented methods use File | Settings | File Templates.
                    }
                });

                SearchPattern testSearchPattern = new SearchPattern(parents, new TagPredicate() {
                    @Override
                    public boolean applyToTag(XmlTag tag) {
                        return tag.getName().equals("TAG");
                    }

                    @Override
                    public String getDisplayName() {
                        return null;  //To change body of implemented methods use File | Settings | File Templates.
                    }
                }, children);
                Search searchForPattern = Search.createSearchForPattern(testSearchPattern);
                searchForPattern.setDelegate(new TagSearchObserver() {
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

                    public void elementFound(Search search, XmlElement tag) {
                        if (tag instanceof XmlTag && tag.getFirstChild() != null && tag.getFirstChild().getNextSibling() != null) {
                            highlightElement(tag.getFirstChild().getNextSibling());
                        } else {
                            highlightElement(tag);
                        }
                    }
                });
                psiFile.accept(searchForPattern);
            }
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
