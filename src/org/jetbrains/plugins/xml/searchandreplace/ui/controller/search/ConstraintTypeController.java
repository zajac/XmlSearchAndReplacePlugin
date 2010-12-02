package org.jetbrains.plugins.xml.searchandreplace.ui.controller.search;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.persistence.ConstraintTypeSpecificEntry;
import org.jetbrains.plugins.xml.searchandreplace.search.predicates.Not;
import org.jetbrains.plugins.xml.searchandreplace.search.predicates.XmlElementPredicate;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.captures.Capture;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public abstract class ConstraintTypeController implements PersistentStateComponent<ConstraintTypeSpecificEntry> {


  public static final Key<ConstraintTypeController> USER_DATA_KEY =
          Key.create(ConstraintTypeController.class.getName());
  private ConstraintType constraintType;
  protected Project project;

  protected boolean isOkPattern(String tagName) {
    boolean okExpr = true;
    try {
      Pattern pattern = Pattern.compile(tagName);
    } catch(PatternSyntaxException e) {
      okExpr = false;
    }
    return okExpr;
  }

  public void useRegexps(boolean use) {
    //override who cares
  }

  public interface Delegate {
    void updateCaptures(ConstraintTypeController ptc);
    void validateChildren(ConstraintTypeController ctc);

    boolean useRegexps();

    void badInput(ConstraintTypeController constraintController);
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

  public ConstraintTypeController(ConstraintType constraintType, Project project) {
    this.constraintType = constraintType;
    this.project = project;
  }

  public ConstraintTypeController(Params p, ConstraintType constraintType, Project project) {
    this.p = p;
    this.constraintType = constraintType;
    this.project = project;
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
    return ConstraintTypesRegistry.getInstance(project).getConstraintTypes();
  }
}
