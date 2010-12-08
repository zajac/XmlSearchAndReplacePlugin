package org.jetbrains.plugins.xml.searchandreplace.replace;

import com.intellij.lang.Language;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.xml.XmlElement;
import com.intellij.psi.xml.XmlTag;
import com.intellij.psi.xml.XmlTagChild;
import org.jetbrains.plugins.xml.searchandreplace.search.Node;

import java.util.Map;

public class SurroundWithTag extends ReplacementProvider {
  private ReplacementProvider replacementProvider;

  public SurroundWithTag(ReplacementProvider replacementProvider) {
    super();
    this.replacementProvider = replacementProvider;
  }

  @Override
  public XmlTag getReplacementFor(XmlElement element, Map<Node, PsiElement> match) {
    XmlTag replacement = replacementProvider.getReplacementFor(element, match);
    XmlTagChild[] children = replacement.getValue().getChildren();
    if (children.length != 0) {
      if (children[0] instanceof  XmlTag) {
        XmlTag tag = (XmlTag) children[0];
        Language language = element.getLanguage();
        String text = "<" + tag.getName() + ">" + element.getText() + "</" + tag.getName() + ">";
        PsiFile dummy = PsiFileFactory.getInstance(element.getProject()).createFileFromText("dummy", language, text);

        Document document = PsiDocumentManager.getInstance(element.getProject()).getDocument(element.getContainingFile());
        if (document != null) {
          TextRange textRange = element.getTextRange();
          document.replaceString(textRange.getStartOffset(), textRange.getEndOffset(), dummy.getText());
        }
      }
    }

    return null;
  }
}
