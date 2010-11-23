package org.jetbrains.plugins.xml.searchandreplace.ui.controller.search;

import org.jetbrains.plugins.xml.searchandreplace.search.Node;
import org.jetbrains.plugins.xml.searchandreplace.search.Pattern;
import org.jetbrains.plugins.xml.searchandreplace.search.predicates.XmlElementPredicate;
import org.jetbrains.plugins.xml.searchandreplace.ui.ConstraintType;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.replace.Capture;
import org.jetbrains.plugins.xml.searchandreplace.ui.predicatetypes.RootConstraintType;
import org.jetbrains.plugins.xml.searchandreplace.ui.view.PredicatePanel;
import org.jetbrains.plugins.xml.searchandreplace.ui.view.PredicatePanelDelegate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ConstraintController implements PredicatePanelDelegate, ConstraintTypeController.Delegate {

  private PredicatePanel myView;

  public ConstraintTypeController getConstraintTypeController() {
    return constraintTypeController;
  }

  private ConstraintTypeController constraintTypeController;

  private ConstraintType selectedConstraintType;

  private PredicateControllerDelegate delegate;

  private ConstraintController parent;
  private Node builtNode;
  private Collection<Capture> captures;

  public ConstraintController(boolean canHaveChildren, ConstraintController parent) {
    this.parent = parent;
    myView = new PredicatePanel(canHaveChildren, parent != null);
    myView.setDelegate(this);
  }

  private int getIndent() {
    if (parent == null) return 0;
    return parent.getIndent() + 1;
  }

  public ConstraintController getParent() {
    return parent;
  }

  public PredicateControllerDelegate getDelegate() {
    return delegate;
  }

  public void setDelegate(PredicateControllerDelegate delegate) {
    this.delegate = delegate;
    myView.reloadData();
  }

  public PredicatePanel getView() {
    return myView;
  }

  public void addChild(PredicatePanel panel) {
    if (getDelegate() != null) {
      getDelegate().addChild(this);
    }
  }

  public List<ConstraintType> getChildrentConstraintTypes(PredicatePanel panel) {
    return getDelegate() == null ? new ArrayList<ConstraintType>() : getDelegate().getAllowedPredicateTypes(this);
  }

  public void constraintTypeSelected(PredicatePanel panel, ConstraintType selection) {
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

  public void removeMe(PredicatePanel panel) {
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
    myView.setCaptures(captures);
    if (delegate != null) {
      delegate.validateMe(this);
    }
  }
}
