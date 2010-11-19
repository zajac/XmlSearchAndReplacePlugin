package org.jetbrains.plugins.xml.searchandreplace.ui.controller.search;

import com.intellij.openapi.util.Key;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.replace.Capture;
import org.jetbrains.plugins.xml.searchandreplace.search.predicates.Not;
import org.jetbrains.plugins.xml.searchandreplace.search.predicates.XmlElementPredicate;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collection;

public abstract class PredicateTypeController {


  public static final Key<PredicateTypeController> USER_DATA_KEY =
          Key.create(PredicateTypeController.class.getName());

  public enum Params {NOT}

  Params p = null;

  public PredicateTypeController() {
  }

  public PredicateTypeController(Params p) {
    this.p = p;
  }

  public abstract JPanel getView();

  protected final XmlElementPredicate decorateWithNotIfNeccessary(XmlElementPredicate predicate) {
    return p == Params.NOT ? new Not(predicate) : predicate;
  }

  public abstract XmlElementPredicate buildPredicate();

  public boolean canBeParentOf(XmlElementPredicate predicate) {
    return !(predicate instanceof Not);
  }

  public boolean canBeChildOf(XmlElementPredicate predicate) {
    return true;
  }

  public Collection<Capture> provideCaptures(PredicateController predicateController) {
    return new ArrayList<Capture>();
  }

}
