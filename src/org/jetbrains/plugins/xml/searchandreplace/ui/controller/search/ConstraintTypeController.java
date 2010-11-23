package org.jetbrains.plugins.xml.searchandreplace.ui.controller.search;

import com.intellij.openapi.util.Key;
import org.jetbrains.plugins.xml.searchandreplace.search.predicates.Not;
import org.jetbrains.plugins.xml.searchandreplace.search.predicates.XmlElementPredicate;
import org.jetbrains.plugins.xml.searchandreplace.ui.ConstraintType;
import org.jetbrains.plugins.xml.searchandreplace.ui.PredicateTypeRegistry;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.replace.Capture;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class ConstraintTypeController {


  public static final Key<ConstraintTypeController> USER_DATA_KEY =
          Key.create(ConstraintTypeController.class.getName());
  private ConstraintType constraintType;

  public interface Delegate {
    void updateCaptures(ConstraintTypeController ptc);
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

  public ConstraintTypeController(ConstraintType constraintType) {
    this.constraintType = constraintType;
  }

  public ConstraintTypeController(Params p, ConstraintType constraintType) {
    this.p = p;
    this.constraintType = constraintType;
  }

  public ConstraintType getConstraintType() {
    return constraintType;
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

  public Collection<Capture> provideCaptures(ConstraintController constraintController) {
    return new ArrayList<Capture>();
  }


  public List<ConstraintType> getAllowedChildrenTypes() {
    return PredicateTypeRegistry.getInstance().getConstraintTypes();
  }
}
