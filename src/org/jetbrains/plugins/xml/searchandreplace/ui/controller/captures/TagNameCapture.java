package org.jetbrains.plugins.xml.searchandreplace.ui.controller.captures;

import com.intellij.psi.PsiElement;
import com.intellij.psi.xml.XmlTag;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.ConstraintController;

public class TagNameCapture extends Capture {

  public TagNameCapture(ConstraintController constraintController) {
    super(constraintController);
  }

  @Override
  public String getName() {
    return "Tag name";
  }

  @Override
  public String value(PsiElement element) {
    return element instanceof XmlTag ? ((XmlTag)element).getName() : null;
  }
}
