package org.jetbrains.plugins.xml.searchandreplace.ui.controller.search;

import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlElement;
import com.intellij.psi.xml.XmlTag;
import org.jetbrains.plugins.xml.searchandreplace.replace.CapturePresentation;
import org.jetbrains.plugins.xml.searchandreplace.search.predicates.HasSpecificAttribute;
import org.jetbrains.plugins.xml.searchandreplace.search.predicates.XmlElementPredicate;
import org.jetbrains.plugins.xml.searchandreplace.ui.CapturePresentationFactory;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.replace.Capture;


public class AttributeNameCapture extends Capture {
  private PredicateController predicateController;

  public AttributeNameCapture(PredicateController predicateController) {
    this.predicateController = predicateController;
  }

  @Override
  public CapturePresentation presentation() {
    CapturePresentation result =
            CapturePresentationFactory.instance().createPresentation(predicateController, "Attribute name");
    result.setCapture(this);
    return result;
  }

  @Override
  public String value(XmlElement element, XmlElementPredicate predicate) {
    if (element instanceof XmlTag) {
      //Set<XmlElementPredicate> flatten = predicate.flatten();
      if (getPredicate().apply(element)) {
        if (getPredicate() instanceof HasSpecificAttribute) {
          for (XmlAttribute attr : ((XmlTag) element).getAttributes()) {
            if ( ((HasSpecificAttribute) getPredicate()).getAttributePredicate().applyToAttribute(attr) ) {
              return attr.getName();
            }
          }
        }
      }
    }
    return null;
  }
}
