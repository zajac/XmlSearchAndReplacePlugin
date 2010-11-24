package org.jetbrains.plugins.xml.searchandreplace.ui.controller.search;

import org.jetbrains.plugins.xml.searchandreplace.search.predicates.DirectlyContains;
import org.jetbrains.plugins.xml.searchandreplace.search.predicates.XmlElementPredicate;
import org.jetbrains.plugins.xml.searchandreplace.ui.ConstraintType;
import org.jetbrains.plugins.xml.searchandreplace.ui.PredicateTypeRegistry;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.replace.Capture;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.captures.DirectlyContainsTagNameCapture;
import org.jetbrains.plugins.xml.searchandreplace.ui.constraintTypes.WithAttribute;
import org.jetbrains.plugins.xml.searchandreplace.ui.constraintTypes.WithoutAttribute;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class DirectlyContainsController extends TagOrTextConstraintController {

  public DirectlyContainsController(ConstraintType pt, boolean strictlyTag) {
    super(pt, strictlyTag);
  }

  @Override
  public XmlElementPredicate buildPredicate() {
    XmlElementPredicate predicate = super.buildPredicate();
    return new DirectlyContains(predicate);
  }

  @Override
  public Collection<Capture> provideCaptures(ConstraintController constraintController) {
    ArrayList<Capture> captures = new ArrayList<Capture>();
    if (!isConstraintOnText()) {
      captures.add(new DirectlyContainsTagNameCapture(constraintController));
    }
    return captures;
  }

  @Override
  public List<ConstraintType> getAllowedChildrenTypes() {
    if (isConstraintOnText()) {
      return new ArrayList<ConstraintType>();
    }
    return Arrays.asList(PredicateTypeRegistry.getInstance().byClass(WithAttribute.class),
            PredicateTypeRegistry.getInstance().byClass(WithoutAttribute.class),
            PredicateTypeRegistry.getInstance().
                    byClass(org.jetbrains.plugins.xml.searchandreplace.ui.constraintTypes.DirectlyContains.class));
  }
}
