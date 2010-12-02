package org.jetbrains.plugins.xml.searchandreplace.ui.view.search;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.ui.EditorTextField;

/**
 * Created by IntelliJ IDEA.
 * User: zajac
 * Date: 02.12.10
 * Time: 18:38
 * To change this template use File | Settings | File Templates.
 */
public class Util {
  static EditorTextField createRegexpEditor(Project myProject, boolean regexps) {
    String s = regexps ? "*.regexp" : "*.txt";
    FileType fileType = FileTypeManager.getInstance().getFileTypeByFileName(s);
    final PsiFile file = PsiFileFactory.getInstance(myProject).createFileFromText(s, fileType, "", -1, true);
    Document document = PsiDocumentManager.getInstance(myProject).getDocument(file);
    return new EditorTextField(document, myProject, fileType);
  }

  public static void useRegexps(EditorTextField editorTextField, Project myProject, boolean use) {
    String s = use ? "*.regexp" : "*.txt";
    FileType fileType = FileTypeManager.getInstance().getFileTypeByFileName(s);
    final PsiFile file = PsiFileFactory.getInstance(myProject).createFileFromText(s, fileType, editorTextField.getText(), -1, true);
    Document document = PsiDocumentManager.getInstance(myProject).getDocument(file);
    editorTextField.setNewDocumentAndFileType(fileType, document);
  }
}
