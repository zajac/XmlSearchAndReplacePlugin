package org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.captures;

import com.intellij.psi.xml.XmlElement;
import com.intellij.psi.xml.XmlTag;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.replace.Capture;
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
  public String value(XmlElement element) {
    return element instanceof XmlTag ? ((XmlTag)element).getName() : null;
  }
}
