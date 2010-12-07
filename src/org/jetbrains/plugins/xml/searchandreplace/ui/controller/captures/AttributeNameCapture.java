package org.jetbrains.plugins.xml.searchandreplace.ui.controller.captures;

import com.intellij.psi.PsiElement;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlElement;
import com.intellij.psi.xml.XmlTag;
import org.jetbrains.plugins.xml.searchandreplace.search.predicates.HasSpecificAttribute;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.ConstraintController;


public class AttributeNameCapture extends Capture {

  public AttributeNameCapture(ConstraintController constraintController) {
    super(constraintController);
  }

  @Override
  public String getName() {
    return "Attribute name";
  }

  @Override
  public String value(PsiElement element) {
    if (element instanceof XmlTag) {
      if (getPredicate().apply((XmlElement) element)) {
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
