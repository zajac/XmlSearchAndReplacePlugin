package org.jetbrains.plugins.xml.searchandreplace.ui.controller.search;

import com.intellij.psi.xml.XmlElement;
import com.intellij.psi.xml.XmlTag;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.replace.Capture;
import org.jetbrains.plugins.xml.searchandreplace.replace.CapturePresentation;
import org.jetbrains.plugins.xml.searchandreplace.search.predicates.TagPredicate;
import org.jetbrains.plugins.xml.searchandreplace.search.predicates.XmlElementPredicate;
import org.jetbrains.plugins.xml.searchandreplace.ui.CapturePresentationFactory;
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
  public Collection<Capture> provideCaptures(final PredicateController predicateController) {
    ArrayList<Capture> captures = new ArrayList<Capture>();
    captures.add(new Capture() {
      @Override
      public CapturePresentation presentation() {
        CapturePresentation result =
                CapturePresentationFactory.instance().createPresentation(predicateController, "Tag Name");
        result.setCapture(this);
        return result;
      }

      @Override
      public String value(XmlElement element) {
        return element instanceof XmlTag ? ((XmlTag)element).getName() : null;
      }
    });
    return captures;
  }
}
