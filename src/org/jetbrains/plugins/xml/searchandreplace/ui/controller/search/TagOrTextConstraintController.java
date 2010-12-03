package org.jetbrains.plugins.xml.searchandreplace.ui.controller.search;

import com.intellij.openapi.project.Project;
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
import java.util.Collections;
import java.util.List;

public class TagOrTextConstraintController extends ConstraintTypeController implements TagPredicatePanel.Delegate {
  private TagPredicatePanel myView;
  private boolean useRegexps = false;


  @Override
  public void useRegexps(boolean use) {
    if (myView.selectedCard().equals(myView.TEXT)) {
      myView.useRegexps(false);
    } else {
      myView.useRegexps(use);
    }
    useRegexps = use;
  }

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
    XmlElementPredicate predicate = null;
    if (myView.selectedCard().equals(TagPredicatePanel.TAG)) {
      String tagName = myView.getTagName().trim();
      if (getDelegate().useRegexps()) {
        if (isOkPattern(tagName)) {
          predicate = new TagNameMatches(tagName);
        } else {
          getDelegate().badInput(this);
        }
      } else {
        if (!tagName.isEmpty()) {
          predicate = new TagNameWildcardMatches(tagName);
        } else {
          predicate = new AnyTag();
        }
      }
    } else {
      String text = myView.getText().trim();
      if (text.isEmpty()) {
        predicate = new AnyXmlText();
      } else {
        predicate = new MatchesXmlTextPredicate(text);
      }
    }
    return decorateWithNotIfNeccessary(predicate);
  }

  @Override
  public List<Capture> provideCaptures(ConstraintController constraintController) {
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
      if (tpp.selectedCard().equals(TagPredicatePanel.TEXT)) {
        tpp.useRegexps(false);
      } else {
        tpp.useRegexps(useRegexps);
      }
      getDelegate().fetchCapturesFrom(this);
      getDelegate().validateChildren(this);
    }
  }

  @Override
  public List<ConstraintType> getAllowedChildrenTypes() {
    if (myView.selectedCard().equals(TagPredicatePanel.TAG)) {
      if (getConstraintType() instanceof NotInside || getConstraintType() instanceof NotContains) {
        return Collections.emptyList();
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
      myView.useRegexps(false);
    } else {
      myView.setSelectedCard(TagPredicatePanel.TAG);
    }
    myView.setText(state.getText());
  }

}
