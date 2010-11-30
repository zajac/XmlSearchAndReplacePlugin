package org.jetbrains.plugins.xml.searchandreplace.ui.controller.search;

import org.jetbrains.plugins.xml.searchandreplace.search.predicates.DirectlyContains;
import org.jetbrains.plugins.xml.searchandreplace.search.predicates.XmlElementPredicate;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.constraintTypes.Contains;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.constraintTypes.WithAttribute;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.constraintTypes.WithoutAttribute;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.captures.Capture;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.captures.DirectlyContainsTagNameCapture;

import java.util.ArrayList;
import java.util.Arrays;
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
  public ArrayList<Capture> provideCaptures(ConstraintController constraintController) {
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
    return Arrays.asList(
            ConstraintTypesRegistry.getInstance().byClass(WithAttribute.class),
            ConstraintTypesRegistry.getInstance().byClass(WithoutAttribute.class),
            ConstraintTypesRegistry.getInstance().
                    byClass(org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.constraintTypes.DirectlyContains.class),
            ConstraintTypesRegistry.getInstance().byClass(Contains.class));
  }
}
