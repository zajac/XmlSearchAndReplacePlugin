package org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.captures;

import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlElement;
import com.intellij.psi.xml.XmlTag;
import org.jetbrains.plugins.xml.searchandreplace.search.predicates.HasSpecificAttribute;
import org.jetbrains.plugins.xml.searchandreplace.ui.CapturesManager;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.replace.Capture;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.ConstraintController;


public class AttributeNameCapture extends Capture {

  public AttributeNameCapture(ConstraintController constraintController) {
    super(constraintController);
    CapturesManager.instance().registerNewCapture(constraintController, "Attribute name", this);
  }

  @Override
  public String value(XmlElement element) {
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
