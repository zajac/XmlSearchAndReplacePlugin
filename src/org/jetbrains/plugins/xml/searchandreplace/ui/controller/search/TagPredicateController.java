package org.jetbrains.plugins.xml.searchandreplace.ui.controller.search;

import com.intellij.psi.xml.XmlTag;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.replace.Capture;
import org.jetbrains.plugins.xml.searchandreplace.search.predicates.TagPredicate;
import org.jetbrains.plugins.xml.searchandreplace.search.predicates.XmlElementPredicate;
import org.jetbrains.plugins.xml.searchandreplace.ui.view.TagPredicatePanel;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collection;

public class TagPredicateController extends PredicateTypeController {
  private TagPredicatePanel myView = new TagPredicatePanel();

  @Override
  public JPanel getView() {
    return myView;
  }

  public TagPredicateController() {
  }

  public TagPredicateController(Params p) {
    super(p);
  }

  @Override
  public XmlElementPredicate buildPredicate() {
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
  }

  @Override
  public Collection<Capture> provideCaptures(PredicateController predicateController) {
    if (p != Params.NOT) {
      ArrayList<Capture> captures = new ArrayList<Capture>();
      captures.add(new TagNameCapture(predicateController));
      return captures;
    } else {
      return super.provideCaptures(predicateController);
    }
  }

}
