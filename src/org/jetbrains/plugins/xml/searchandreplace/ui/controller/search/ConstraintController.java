package org.jetbrains.plugins.xml.searchandreplace.ui.controller.search;

import com.intellij.openapi.components.PersistentStateComponent;
import org.jetbrains.plugins.xml.searchandreplace.persistence.ConstraintEntry;
import org.jetbrains.plugins.xml.searchandreplace.persistence.ConstraintTypeSpecificEntry;
import org.jetbrains.plugins.xml.searchandreplace.search.Node;
import org.jetbrains.plugins.xml.searchandreplace.search.Pattern;
import org.jetbrains.plugins.xml.searchandreplace.search.predicates.XmlElementPredicate;
import org.jetbrains.plugins.xml.searchandreplace.ui.ConstraintType;
import org.jetbrains.plugins.xml.searchandreplace.ui.ConstraintTypesRegistry;
import org.jetbrains.plugins.xml.searchandreplace.ui.constraintTypes.RootConstraintType;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.replace.Capture;
import org.jetbrains.plugins.xml.searchandreplace.ui.view.ConstraintPanel;
import org.jetbrains.plugins.xml.searchandreplace.ui.view.ConstraintPanelDelegate;

import java.util.ArrayList;
import java.util.List;

public class ConstraintController implements ConstraintPanelDelegate, ConstraintTypeController.Delegate, PersistentStateComponent<ConstraintEntry> {

  private ConstraintPanel myView;

  private ConstraintTypeController constraintTypeController;

  private ConstraintType selectedConstraintType;

  private ConstraintControllerDelegate delegate;

  private ConstraintController parent;

  private Node builtNode;

  private ArrayList<Capture> captures = new ArrayList<Capture>();

  public ArrayList<Capture> getCaptures() {
    return captures;
  }

  public ConstraintTypeController getConstraintTypeController() {
    return constraintTypeController;
  }

  public ConstraintController(boolean canHaveChildren, ConstraintController parent) {
    this.parent = parent;
    myView = new ConstraintPanel(canHaveChildren, parent != null);
    myView.setDelegate(this);
  }

  private int getIndent() {
    if (parent == null) return 0;
    return parent.getIndent() + 1;
  }

  public ConstraintController getParent() {
    return parent;
  }

  public ConstraintControllerDelegate getDelegate() {
    return delegate;
  }

  public void setDelegate(ConstraintControllerDelegate delegate) {
    this.delegate = delegate;
    myView.reloadData();
  }

  public ConstraintPanel getView() {
    return myView;
  }

  public void addChild(ConstraintPanel panel) {
    if (getDelegate() != null) {
      getDelegate().addChild(this);
    }
  }

  public List<ConstraintType> getChildrentConstraintTypes(ConstraintPanel panel) {
    return getDelegate() == null ? new ArrayList<ConstraintType>() : getDelegate().getAllowedPredicateTypes(this);
  }

  public void constraintTypeSelected(ConstraintPanel panel, ConstraintType selection) {
    if (selection == selectedConstraintType && selectedConstraintType != null) return;
    if (selection != null) {
      selectedConstraintType = selection;
    } else {
      selectedConstraintType = new RootConstraintType();
    }
    if (constraintTypeController != null) {
      constraintTypeController.setDelegate(null);
    }
    constraintTypeController = selectedConstraintType.createNewController();
    constraintTypeController.setDelegate(this);
    myView.setPredicateTypeSpecificView(constraintTypeController.getView());
    updateCaptures(constraintTypeController);
    if (getDelegate() != null) {
      getDelegate().validateMe(this);
    }
  }

  public void removeMe(ConstraintPanel panel) {
    if (getDelegate() != null) {
      getDelegate().removeMe(this);
    }
  }

  public Node getBuiltNode() {
    return builtNode;
  }

  public void buildNode(Pattern p) {
    if (constraintTypeController == null) return;
    XmlElementPredicate predicate = constraintTypeController.buildPredicate();
    if (predicate != null) {
      builtNode = new Node(predicate, parent == null);
      for (Capture c : captures) {
        c.setPredicate(predicate);
      }
      Node parentNode = parent == null ? null : parent.getBuiltNode();
      if (parent == null || parentNode != null) {
        builtNode.putUserData(ConstraintTypeController.USER_DATA_KEY, constraintTypeController);
        builtNode = selectedConstraintType.addNodeToPattern(p, builtNode, parentNode);
      }
    }
  }

  public ConstraintType getSelectedConstraintType() {
    return selectedConstraintType;
  }

  public void setCanHaveChildren(boolean b) {
    getView().setCanHaveChildren(b);
  }

  @Override
  public void updateCaptures(ConstraintTypeController ptc) {
    captures = ptc.provideCaptures(this);
  }

  @Override
  public void validateChildren(ConstraintTypeController ctc) {
    if (delegate != null) {
      delegate.validateMe(this);
    }
  }

  public void highlightCaptures(Capture active) {

    myView.highlightCaptures(active);
  }

  @Override
  public ConstraintEntry getState() {
    if (selectedConstraintType == null) return new ConstraintEntry();

    ConstraintEntry constraintEntry = new ConstraintEntry();
    constraintEntry.setConstraintTypeClassSelected(selectedConstraintType.getClass().getName());

    ConstraintTypeSpecificEntry constraintTypeControllerState = constraintTypeController.getState();
    constraintEntry.setConstraintTypeSpecificEntry(constraintTypeControllerState);

    ArrayList<String> capturesIds = new ArrayList<String>();
    for (Capture c : captures) {
      capturesIds.add(c.presentation().getIdentifier());
    }
    constraintEntry.setCapturesIds(capturesIds);
    return constraintEntry;
  }

  @Override
  public void loadState(ConstraintEntry state) {

    String constraintTypeClassSelected = state.getConstraintTypeClassSelected();
    if (constraintTypeClassSelected == null || constraintTypeClassSelected.isEmpty()) return;
    ConstraintType constraintType = null;
    try {
      constraintType = ConstraintTypesRegistry.getInstance().byClass(Class.forName(constraintTypeClassSelected));
    } catch (ClassNotFoundException e) {
      constraintType = null;
    }

    if (constraintType == null) {
      constraintType = new RootConstraintType();
    }

    selectedConstraintType = constraintType;

    constraintTypeController = selectedConstraintType.createNewController();
    constraintTypeController.setDelegate(this);

    myView.setSelectedConstraintType(constraintType);
    myView.setPredicateTypeSpecificView(constraintTypeController.getView());

    updateCaptures(constraintTypeController);

    getDelegate().loadCapturesFor(this, state);

    myView.setCaptures(captures);

    constraintTypeController.loadState(state.getConstraintTypeSpecificEntry());
  }
}
