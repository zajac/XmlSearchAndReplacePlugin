package org.jetbrains.plugins.xml.searchandreplace.ui.controller.search;

import org.jetbrains.plugins.xml.searchandreplace.search.predicates.DirectlyContains;
import org.jetbrains.plugins.xml.searchandreplace.search.predicates.XmlElementPredicate;
import org.jetbrains.plugins.xml.searchandreplace.ui.PredicateType;
import org.jetbrains.plugins.xml.searchandreplace.ui.PredicateTypeRegistry;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.replace.Capture;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.captures.DirectlyContainsTagNameCapture;
import org.jetbrains.plugins.xml.searchandreplace.ui.predicatetypes.WithAttribute;
import org.jetbrains.plugins.xml.searchandreplace.ui.predicatetypes.WithoutAttribute;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class DirectlyContainsController extends TagPredicateController {

  public DirectlyContainsController(PredicateType pt, boolean strictlyTag) {
    super(pt, strictlyTag);
  }

  @Override
  public XmlElementPredicate buildPredicate() {
    XmlElementPredicate predicate = super.buildPredicate();
    return new DirectlyContains(predicate);
  }

  @Override
  public Collection<Capture> provideCaptures(PredicateController predicateController) {
    ArrayList<Capture> captures = new ArrayList<Capture>();
    captures.add(new DirectlyContainsTagNameCapture(predicateController));
    return captures;
  }

  @Override
  public List<PredicateType> getAllowedChildrenTypes() {
    return Arrays.asList(PredicateTypeRegistry.getInstance().byClass(WithAttribute.class),
            PredicateTypeRegistry.getInstance().byClass(WithoutAttribute.class));
  }
}
