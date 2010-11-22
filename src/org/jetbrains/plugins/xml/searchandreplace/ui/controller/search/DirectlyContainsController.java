package org.jetbrains.plugins.xml.searchandreplace.ui.controller.search;

import org.jetbrains.plugins.xml.searchandreplace.search.predicates.DirectlyContains;
import org.jetbrains.plugins.xml.searchandreplace.search.predicates.XmlElementPredicate;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.replace.Capture;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.captures.DirectlyContainsTagNameCapture;

import java.util.ArrayList;
import java.util.Collection;

public class DirectlyContainsController extends TagPredicateController {

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
}
