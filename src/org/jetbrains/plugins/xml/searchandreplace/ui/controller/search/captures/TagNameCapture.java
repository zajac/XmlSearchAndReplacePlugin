package org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.captures;

import com.intellij.psi.xml.XmlElement;
import com.intellij.psi.xml.XmlTag;
import org.jetbrains.plugins.xml.searchandreplace.replace.CapturePresentation;
import org.jetbrains.plugins.xml.searchandreplace.ui.CapturePresentationFactory;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.replace.Capture;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.ConstraintController;

public class TagNameCapture extends Capture {

  public TagNameCapture(ConstraintController constraintController) {
    super(constraintController);
  }

  @Override
  public CapturePresentation presentation() {
    CapturePresentation result =
            CapturePresentationFactory.instance().createPresentation(constraintController, "Tag Name");
    result.setCapture(this);
    return result;
  }

  @Override
  public String value(XmlElement element) {
    return element instanceof XmlTag ? ((XmlTag)element).getName() : null;
  }
}
