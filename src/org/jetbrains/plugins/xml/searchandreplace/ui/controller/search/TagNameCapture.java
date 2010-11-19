package org.jetbrains.plugins.xml.searchandreplace.ui.controller.search;

import com.intellij.psi.xml.XmlElement;
import com.intellij.psi.xml.XmlTag;
import org.jetbrains.plugins.xml.searchandreplace.replace.CapturePresentation;
import org.jetbrains.plugins.xml.searchandreplace.search.predicates.XmlElementPredicate;
import org.jetbrains.plugins.xml.searchandreplace.ui.CapturePresentationFactory;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.replace.Capture;

/**
* Created by IntelliJ IDEA.
* User: zajac
* Date: 19.11.10
* Time: 15:51
* To change this template use File | Settings | File Templates.
*/
class TagNameCapture extends Capture {
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
  public String value(XmlElement element, XmlElementPredicate predicate) {
    return element instanceof XmlTag ? ((XmlTag)element).getName() : null;
  }
}
