package org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.captures;

import com.intellij.psi.xml.XmlElement;
import com.intellij.psi.xml.XmlTag;
import org.jetbrains.plugins.xml.searchandreplace.replace.CapturePresentation;
import org.jetbrains.plugins.xml.searchandreplace.ui.CapturePresentationFactory;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.replace.Capture;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.PredicateController;

public class TagNameCapture extends Capture {
  private final PredicateController predicateController;

  public TagNameCapture(PredicateController predicateController) {
    this.predicateController = predicateController;
  }

  @Override
  public CapturePresentation presentation() {
    CapturePresentation result =
            CapturePresentationFactory.instance().createPresentation(predicateController, "Tag Name");
    result.setCapture(this);
    return result;
  }

  @Override
  public String value(XmlElement element) {
    return element instanceof XmlTag ? ((XmlTag)element).getName() : null;
  }
}
