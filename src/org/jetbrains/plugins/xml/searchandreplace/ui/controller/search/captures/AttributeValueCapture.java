package org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.captures;

import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlElement;
import com.intellij.psi.xml.XmlTag;
import org.jetbrains.plugins.xml.searchandreplace.search.predicates.HasSpecificAttribute;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.replace.Capture;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.ConstraintController;

public class AttributeValueCapture extends Capture {

  public AttributeValueCapture(ConstraintController constraintController) {
    super(constraintController);
  }

  @Override
  public String getName() {
    return "Attribute name";
  }

  @Override
  public String value(XmlElement element) {
     if (element instanceof XmlTag) {
      //Set<XmlElementPredicate> flatten = predicate.flatten();
      if (getPredicate().apply(element)) {
        if (getPredicate() instanceof HasSpecificAttribute) {
          for (XmlAttribute attr : ((XmlTag) element).getAttributes()) {
            if ( ((HasSpecificAttribute) getPredicate()).getAttributePredicate().applyToAttribute(attr) ) {
              return attr.getValue();
            }
          }
        }
      }
    }
    return null;
  }
}
