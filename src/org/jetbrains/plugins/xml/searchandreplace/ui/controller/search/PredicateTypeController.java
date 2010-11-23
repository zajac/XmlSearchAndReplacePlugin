package org.jetbrains.plugins.xml.searchandreplace.ui.controller.search;

import com.intellij.openapi.util.Key;
import org.jetbrains.plugins.xml.searchandreplace.search.predicates.Not;
import org.jetbrains.plugins.xml.searchandreplace.search.predicates.XmlElementPredicate;
import org.jetbrains.plugins.xml.searchandreplace.ui.PredicateType;
import org.jetbrains.plugins.xml.searchandreplace.ui.PredicateTypeRegistry;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.replace.Capture;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class PredicateTypeController {


  public static final Key<PredicateTypeController> USER_DATA_KEY =
          Key.create(PredicateTypeController.class.getName());
  private PredicateType predicateType;

  public interface Delegate {
    void updateCaptures(PredicateTypeController ptc);
  }

  private Delegate delegate;

  public Delegate getDelegate() {
    return delegate;
  }

  public void setDelegate(Delegate delegate) {
    this.delegate = delegate;
  }

  public enum Params {NOT}

  protected Params p = null;

  public PredicateTypeController(PredicateType predicateType) {
    this.predicateType = predicateType;
  }

  public PredicateTypeController(Params p, PredicateType predicateType) {
    this.p = p;
    this.predicateType = predicateType;
  }

  public PredicateType getPredicateType() {
    return predicateType;
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


  public List<PredicateType> getAllowedChildrenTypes() {
    return PredicateTypeRegistry.getInstance().getPredicateTypes();
  }
}
