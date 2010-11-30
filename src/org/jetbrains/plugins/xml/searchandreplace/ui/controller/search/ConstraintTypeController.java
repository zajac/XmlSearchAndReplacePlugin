package org.jetbrains.plugins.xml.searchandreplace.ui.controller.search;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.util.Key;
import org.jetbrains.plugins.xml.searchandreplace.persistence.ConstraintTypeSpecificEntry;
import org.jetbrains.plugins.xml.searchandreplace.search.predicates.Not;
import org.jetbrains.plugins.xml.searchandreplace.search.predicates.XmlElementPredicate;
import org.jetbrains.plugins.xml.searchandreplace.ui.ConstraintType;
import org.jetbrains.plugins.xml.searchandreplace.ui.ConstraintTypesRegistry;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.replace.Capture;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public abstract class ConstraintTypeController implements PersistentStateComponent<ConstraintTypeSpecificEntry> {


  public static final Key<ConstraintTypeController> USER_DATA_KEY =
          Key.create(ConstraintTypeController.class.getName());
  private ConstraintType constraintType;

  public interface Delegate {
    void updateCaptures(ConstraintTypeController ptc);
    void validateChildren(ConstraintTypeController ctc);
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

  public ArrayList<Capture> provideCaptures(ConstraintController constraintController) {
    return new ArrayList<Capture>();
  }


  public List<ConstraintType> getAllowedChildrenTypes() {
    return ConstraintTypesRegistry.getInstance().getConstraintTypes();
  }
}
