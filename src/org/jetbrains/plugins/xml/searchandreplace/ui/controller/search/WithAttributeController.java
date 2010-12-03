package org.jetbrains.plugins.xml.searchandreplace.ui.controller.search;

import com.intellij.openapi.project.Project;
import com.intellij.psi.xml.XmlAttribute;
import org.jetbrains.plugins.xml.searchandreplace.search.predicates.*;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.captures.AttributeNameCapture;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.captures.AttributeValueCapture;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.captures.Capture;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.persistence.ConstraintTypeSpecificEntry;
import org.jetbrains.plugins.xml.searchandreplace.ui.view.search.AttributePanel;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.PatternSyntaxException;


public class WithAttributeController extends ConstraintTypeController {

  @Override
  public ConstraintTypeSpecificEntry getState() {
    ConstraintTypeSpecificEntry state = new ConstraintTypeSpecificEntry();
    state.setAttrName(view.getAttrName());
    state.setValue(view.getValue());
    Comparator comparator = (Comparator) view.selectedComparator();
    if (comparator != null) {
      state.setComparator(comparator.name());
    }
    return state;
  }

  @Override
  public void loadState(ConstraintTypeSpecificEntry state) {
    view.setAttrName(state.getAttrName());
    view.setValue(state.getValue());

    for (Comparator comparator : comparators) {
      if (comparator.name().equals(state.getComparator())) {
        view.setSelectedComparator(comparator);
        break;
      }
    }
  }

  public interface Comparator {

    boolean compare(String value1, String value2);
    String name();
  }

  private AttributePanel view;
  private List<Comparator> comparators;

  public WithAttributeController(ConstraintType pt, Project project) {
    super(pt, project);
    Comparator[] comparators = new Comparator[]{new Comparator() {
      public boolean compare(String value1, String value2) {
        return Utils.wildcardMatches(value1, value2);
      }

      public String name() {
        return "equals";
      }

      public String toString() {
        return "=";
      }
    }, new Comparator() {

      public String toString() {
        return "!=";
      }

      public boolean compare(String value1, String value2) {
        return !value1.equals(value2);
      }

      @Override
      public String name() {
        return "not equals";
      }
    }, new Comparator() {

      public String toString() {
        return "<";
      }

      public boolean compare(String value1, String value2) {
        int i1 = 0, i2 = 0;
        try {
          i1 = Integer.parseInt(value1);
          i2 = Integer.parseInt(value2);
        } catch (NumberFormatException e) {
          e.printStackTrace();
        }
        return i1 < i2;
      }

      @Override
      public String name() {
        return "less";
      }
    }, new Comparator() {

      public String toString() {
        return ">";
      }

      public boolean compare(String value1, String value2) {
        int i1 = 0, i2 = 0;
        try {
          i1 = Integer.parseInt(value1);
          i2 = Integer.parseInt(value2);
        } catch (NumberFormatException e) {
          e.printStackTrace();
        }
        return i1 > i2;
      }

      @Override
      public String name() {
        return "more";
      }
    }, new Comparator() {

      public String toString() {
        return "<=";
      }

      public boolean compare(String value1, String value2) {
        int i1 = 0, i2 = 0;
        try {
          i1 = Integer.parseInt(value1);
          i2 = Integer.parseInt(value2);
        } catch (NumberFormatException e) {
          e.printStackTrace();
        }
        return i1 <= i2;
      }

      @Override
      public String name() {
        return "lessOrEquals";
      }
    }, new Comparator() {

      public String toString() {
        return ">=";
      }

      public boolean compare(String value1, String value2) {
        int i1 = 0, i2 = 0;
        try {
          i1 = Integer.parseInt(value1);
          i2 = Integer.parseInt(value2);
        } catch (NumberFormatException e) {
          e.printStackTrace();
        }
        return i1 >= i2;
      }

      @Override
      public String name() {
        return "moreOrEquals";
      }
    }, new Comparator() {
      boolean isOk = true;
      public String toString() {
        return "matches";
      }

      public boolean compare(String value1, String value2) {
        if (!isOk) return false;
        boolean matches;
        try {
          matches = value1.matches(value2);
        } catch(PatternSyntaxException e) {
          matches = false;
          isOk = false;
        }
        return matches;
      }

      @Override
      public String name() {
        return "matches";
      }
    }};

    this.comparators = Arrays.asList(comparators);
    view = new AttributePanel(this.comparators, project);
  }

  @Override
  public JPanel getView() {
    return view;
  }

  @Override
  public XmlElementPredicate buildPredicate() {
    final String attrName = view.getAttrName().trim();
    final String value = view.getValue();
    AttributePredicate predicate;
    AttributePredicate anyAttribute = new AttributePredicate() {
      @Override
      public boolean applyToAttribute(XmlAttribute a) {
        return true;
      }
    };
    if (attrName.isEmpty()) {
      predicate = anyAttribute;
    } else {
      final AttributePredicate p1, p2;
      if (getDelegate().useRegexps()) {
        if (isOkPattern(attrName)) {
          p1 = new AttributePredicate() {
            @Override
            public boolean applyToAttribute(XmlAttribute a) {
              return a.getName().matches(attrName);
            }
          };
        } else {
          getDelegate().badInput(this);
          return null;
        }
      } else {
        p1 = new AttributePredicate() {
          @Override
          public boolean applyToAttribute(XmlAttribute a) {
            return Utils.wildcardMatches(a.getName(), attrName);
          }
        };
      }
      final Comparator selected = (Comparator) view.selectedComparator();
      if (selected == null) {
        p2 = anyAttribute;
      } else {
        if (selected.name().equals("matches")) {
          if (!isOkPattern(value)) {
            getDelegate().badInput(this);
            return null;
          }
        }
        
        if (value.isEmpty()) {
          p2 = anyAttribute;
        } else {
          p2 = new AttributePredicate() {
            @Override
            public boolean applyToAttribute(XmlAttribute a) {
              return selected.compare(a.getValue(), value);
            }
          };
        }
      }
      predicate = new AttributePredicate() {
        @Override
        public boolean applyToAttribute(XmlAttribute a) {
          return p1.applyToAttribute(a) && p2.applyToAttribute(a);
        }
      };
    }
    return decorateWithNotIfNeccessary(new HasSpecificAttribute(predicate));
  }

  @Override
  public List<Capture> provideCaptures(ConstraintController constraintController) {
    ArrayList<Capture> captures = new ArrayList<Capture>();
    captures.add(new AttributeNameCapture(constraintController));
    captures.add(new AttributeValueCapture(constraintController));
    return captures;
  }

  @Override
  public void useRegexps(boolean use) {
    view.useRegexps(use);
  }

  @Override
  public List<ConstraintType> getAllowedChildrenTypes() {
    return Collections.emptyList();
  }
}
