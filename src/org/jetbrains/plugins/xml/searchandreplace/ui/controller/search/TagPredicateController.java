package org.jetbrains.plugins.xml.searchandreplace.ui.controller.search;

import com.intellij.psi.xml.XmlTag;
import org.jetbrains.plugins.xml.searchandreplace.search.predicates.MatchesXmlTextPredicate;
import org.jetbrains.plugins.xml.searchandreplace.search.predicates.TagPredicate;
import org.jetbrains.plugins.xml.searchandreplace.search.predicates.XmlElementPredicate;
import org.jetbrains.plugins.xml.searchandreplace.ui.PredicateType;
import org.jetbrains.plugins.xml.searchandreplace.ui.PredicateTypeRegistry;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.replace.Capture;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.captures.TagNameCapture;
import org.jetbrains.plugins.xml.searchandreplace.ui.predicatetypes.Inside;
import org.jetbrains.plugins.xml.searchandreplace.ui.predicatetypes.NotContains;
import org.jetbrains.plugins.xml.searchandreplace.ui.predicatetypes.NotInside;
import org.jetbrains.plugins.xml.searchandreplace.ui.view.TagPredicatePanel;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class TagPredicateController extends PredicateTypeController implements TagPredicatePanel.Delegate {
  private TagPredicatePanel myView;

  @Override
  public JPanel getView() {
    return myView;
  }

  public TagPredicateController(PredicateType predicateType, boolean strictlyTag) {
    this(predicateType, null, strictlyTag);
  }

  public TagPredicateController(PredicateType predicateType, Params p, boolean strictlyTag) {
    super(p, predicateType);
    myView = new TagPredicatePanel(strictlyTag);
    myView.setDelengate(this);
  }

  @Override
  public XmlElementPredicate buildPredicate() {
    if (myView.selectedCard().equals(TagPredicatePanel.TAG)) {
      final String tagName = myView.getTagName();
      if (tagName.isEmpty()) {
        return null;
      }
      return decorateWithNotIfNeccessary(new TagPredicate() {

        @Override
        public String toString() {
          return tagName;
        }

        @Override
        public boolean applyToTag(XmlTag tag) {
          return tag.getName().matches(tagName);
        }

      });
    } else {
      final String text = myView.getText();
      if (text.isEmpty()) {
        return null;
      }
      return decorateWithNotIfNeccessary(new MatchesXmlTextPredicate(text));
    }
  }

  @Override
  public Collection<Capture> provideCaptures(PredicateController predicateController) {
    if (myView.selectedCard().equals(TagPredicatePanel.TAG)) {
      if (p != Params.NOT) {
        ArrayList<Capture> captures = new ArrayList<Capture>();
        captures.add(new TagNameCapture(predicateController));
        return captures;
      }
    }
    return super.provideCaptures(predicateController);
  }

  @Override
  public void stateChanged(TagPredicatePanel tpp) {
    if (getDelegate() != null) {
      getDelegate().updateCaptures(this);
    }
  }

  @Override
  public List<PredicateType> getAllowedChildrenTypes() {
    if (myView.selectedCard().equals(TagPredicatePanel.TAG)) {
      if (getPredicateType() instanceof NotInside || getPredicateType() instanceof NotContains) {
        return new ArrayList<PredicateType>();
      } else {
        return super.getAllowedChildrenTypes();
      }
    } else {
      return Arrays.asList(PredicateTypeRegistry.getInstance().byClass(Inside.class),
              PredicateTypeRegistry.getInstance().byClass(NotInside.class));
    }
  }
}
