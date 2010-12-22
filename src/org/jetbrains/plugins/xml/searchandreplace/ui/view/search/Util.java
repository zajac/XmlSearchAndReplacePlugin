package org.jetbrains.plugins.xml.searchandreplace.ui.view.search;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.ui.EditorTextField;

public class Util {

  private static PsiFile getFile(EditorTextField e) {
    return PsiDocumentManager.getInstance(e.getProject()).getPsiFile(e.getDocument());
  }

  private static PsiFile createPsiFile(Project myProject, boolean regexps, boolean tagNameCompletionWorks, String text) {
    String name = tagNameCompletionWorks ? TagNameCompletionContributor.TAG_NAME_COMPLETION_WORKS : "*";
    String nameWithExt = name + (regexps ? ".regexp" : ".txt");
    FileType fileType = FileTypeManager.getInstance().getFileTypeByFileName(nameWithExt);
    return PsiFileFactory.getInstance(myProject).createFileFromText(nameWithExt, fileType, text, -1, true);
  }

  private static Document getDocument(Project myProject, PsiFile file) {
    return PsiDocumentManager.getInstance(myProject).getDocument(file);
  }

  static EditorTextField createRegexpEditor(Project myProject, boolean regexps) {
    final PsiFile file = createPsiFile(myProject, regexps, false, "");
    Document document = getDocument(myProject, file);
    return new EditorTextField(document, myProject, file.getFileType());
  }

  public static EditorTextField createRegexpEditorWithTagNameCompletion(Project project, boolean useRegexps) {
    final PsiFile file = createPsiFile(project, useRegexps, true, "");
    Document document = getDocument(project, file);
    return new EditorTextField(document, project, file.getFileType());
  }

  public static void useRegexps(EditorTextField editorTextField, Project myProject, boolean use) {

    PsiFile oldFile = getFile(editorTextField);
    PsiFile psiFile = createPsiFile(myProject, use, TagNameCompletionContributor.doesTagNameCompletionWork(oldFile), editorTextField.getText());

    editorTextField.setNewDocumentAndFileType(psiFile.getFileType(), getDocument(myProject, psiFile));
  }


}
