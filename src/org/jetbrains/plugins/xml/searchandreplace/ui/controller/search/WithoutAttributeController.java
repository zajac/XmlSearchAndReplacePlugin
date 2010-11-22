package org.jetbrains.plugins.xml.searchandreplace.ui.controller.search;

import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlTag;
import org.jetbrains.plugins.xml.searchandreplace.search.predicates.TagPredicate;
import org.jetbrains.plugins.xml.searchandreplace.search.predicates.XmlElementPredicate;
import org.jetbrains.plugins.xml.searchandreplace.ui.view.AttributePanel;

import javax.swing.*;


public class WithoutAttributeController extends PredicateTypeController {

  final AttributePanel view = new AttributePanel(null);

  public WithoutAttributeController() {  }

  @Override
  public JPanel getView() {
    return view;
  }

  @Override
  public XmlElementPredicate buildPredicate() {
    return new TagPredicate() {
      @Override
      public boolean applyToTag(XmlTag tag) {
        String attrName = view.getAttrName();
        if (attrName == null) return true;
        for (XmlAttribute attr : tag.getAttributes()) {
          if (attr.getName().matches(attrName)) {
            return false;
          }
        }
        return true;
      }
    };
  }

}