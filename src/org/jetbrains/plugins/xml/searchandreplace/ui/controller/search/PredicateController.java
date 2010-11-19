package org.jetbrains.plugins.xml.searchandreplace.ui.controller.search;

import org.jetbrains.plugins.xml.searchandreplace.ui.controller.replace.Capture;
import org.jetbrains.plugins.xml.searchandreplace.search.Node;
import org.jetbrains.plugins.xml.searchandreplace.search.Pattern;
import org.jetbrains.plugins.xml.searchandreplace.search.predicates.XmlElementPredicate;
import org.jetbrains.plugins.xml.searchandreplace.ui.PredicateType;
import org.jetbrains.plugins.xml.searchandreplace.ui.predicatetypes.RootPredicateType;
import org.jetbrains.plugins.xml.searchandreplace.ui.view.PredicatePanel;
import org.jetbrains.plugins.xml.searchandreplace.ui.view.PredicatePanelDelegate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PredicateController implements PredicatePanelDelegate {

  private PredicatePanel myView;
  private PredicateTypeController predicateTypeController;

  private PredicateType selectedPredicateType;

  private PredicateControllerDelegate delegate;

  private PredicateController parent;
  private Node builtNode;
  private Collection<Capture> captures;

  public PredicateController(boolean canHaveChildren, PredicateController parent) {
    this.parent = parent;
    myView = new PredicatePanel(canHaveChildren, parent != null, getIndent());
    myView.setDelegate(this);
  }

  private int getIndent() {
    if (parent == null) return 0;
    return parent.getIndent() + 1;
  }

  public PredicateController getParent() {
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

  public List<PredicateType> getPredicateTypes(PredicatePanel panel) {
    return getDelegate() == null ? new ArrayList<PredicateType>() : getDelegate().getAllowedPredicateTypes(this);
  }

  public void predicateTypeSelected(PredicatePanel panel, PredicateType selection) {
    if (selection != null) {
      selectedPredicateType = selection;
    } else {
      selectedPredicateType = new RootPredicateType();
    }
    predicateTypeController = selectedPredicateType.createNewController();
    myView.setPredicateTypeSpecificView(predicateTypeController.getView());
    captures = predicateTypeController.provideCaptures(this);
    myView.setCaptures(captures);
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
    if (predicateTypeController == null) return;
    XmlElementPredicate predicate = predicateTypeController.buildPredicate();
    if (predicate != null) {
      builtNode = new Node(predicate, parent == null);
      for (Capture c : captures) {
        c.setNode(builtNode);
      }
      Node parentNode = parent == null ? null : parent.getBuiltNode();
      if (parent == null || parentNode != null) {
        builtNode.putUserData(PredicateTypeController.USER_DATA_KEY, predicateTypeController);
        selectedPredicateType.addNodeToPattern(p, builtNode, parentNode);
      }
    }
  }

  public PredicateType getSelectedPredicateType() {
    return selectedPredicateType;
  }

  public void setCanHaveChildren(boolean b) {
    getView().setCanHaveChildren(b);
  }
}
