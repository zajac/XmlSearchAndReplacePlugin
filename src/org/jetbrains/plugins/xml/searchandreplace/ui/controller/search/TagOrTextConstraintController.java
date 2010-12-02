package org.jetbrains.plugins.xml.searchandreplace.ui.controller.search;

import com.intellij.openapi.project.Project;
import com.intellij.psi.xml.XmlElement;
import com.intellij.psi.xml.XmlText;
import org.jetbrains.plugins.xml.searchandreplace.search.predicates.*;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.captures.Capture;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.captures.TagNameCapture;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.constraintTypes.Inside;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.constraintTypes.NotContains;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.constraintTypes.NotInside;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.persistence.ConstraintTypeSpecificEntry;
import org.jetbrains.plugins.xml.searchandreplace.ui.view.search.TagPredicatePanel;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TagOrTextConstraintController extends ConstraintTypeController implements TagPredicatePanel.Delegate {
  private TagPredicatePanel myView;

  @Override
  public JPanel getView() {
    return myView;
  }

  public TagOrTextConstraintController(ConstraintType constraintType, boolean strictlyTag, Project project) {
    this(constraintType, null, strictlyTag, project);
  }

  public TagOrTextConstraintController(ConstraintType constraintType, Params p, boolean strictlyTag, Project project) {
    super(p, constraintType, project);
    myView = new TagPredicatePanel(strictlyTag, project);
    myView.setDelengate(this);
  }

  public boolean isConstraintOnText() {
    return myView.selectedCard().equals(TagPredicatePanel.TEXT);
  }

  @Override
  public XmlElementPredicate buildPredicate() {
    if (myView.selectedCard().equals(TagPredicatePanel.TAG)) {
      String tagName = myView.getTagName();
      boolean okExpr = isOkPattern(tagName);
      XmlElementPredicate predicate = okExpr ? (tagName.isEmpty() ? new AnyTag() :  new TagNameMatches(tagName)) : new False();
      return decorateWithNotIfNeccessary(predicate);
    } else {
      String text = myView.getText();
      XmlElementPredicate predicate = isOkPattern(text) ? (text.isEmpty() ? new XmlElementPredicate() {
        @Override
        public boolean apply(XmlElement element) {
          return element instanceof XmlText;
        }
      } : new MatchesXmlTextPredicate(text)) : new False();
      return decorateWithNotIfNeccessary(predicate);
    }
  }

  @Override
  public ArrayList<Capture> provideCaptures(ConstraintController constraintController) {
    if (myView.selectedCard().equals(TagPredicatePanel.TAG)) {
      if (p != Params.NOT) {
        ArrayList<Capture> captures = new ArrayList<Capture>();
        captures.add(new TagNameCapture(constraintController));
        return captures;
      }
    }
    return super.provideCaptures(constraintController);
  }

  @Override
  public void stateChanged(TagPredicatePanel tpp) {
    if (getDelegate() != null) {
      getDelegate().updateCaptures(this);
      getDelegate().validateChildren(this);
    }
  }

  @Override
  public List<ConstraintType> getAllowedChildrenTypes() {
    if (myView.selectedCard().equals(TagPredicatePanel.TAG)) {
      if (getConstraintType() instanceof NotInside || getConstraintType() instanceof NotContains) {
        return new ArrayList<ConstraintType>();
      } else {
        return super.getAllowedChildrenTypes();
      }
    } else {
      return Arrays.asList(ConstraintTypesRegistry.getInstance(project).byClass(Inside.class),
              ConstraintTypesRegistry.getInstance(project).byClass(NotInside.class));
    }
  }

  @Override
  public ConstraintTypeSpecificEntry getState() {
    ConstraintTypeSpecificEntry state = new ConstraintTypeSpecificEntry();
    state.setTextOrTag(myView.selectedCard());
    state.setText(myView.getText());
    return state;
  }

  @Override
  public void loadState(ConstraintTypeSpecificEntry state) {
    if (state.getTextOrTag().equals(TagPredicatePanel.TEXT)) {
      myView.setSelectedCard(TagPredicatePanel.TEXT);
    } else {
      myView.setSelectedCard(TagPredicatePanel.TAG);
    }
    myView.setText(state.getText());
  }

}
