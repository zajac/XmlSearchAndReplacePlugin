package org.jetbrains.plugins.xml.searchandreplace.ui.controller.search;

import com.intellij.psi.xml.XmlAttribute;
import org.jetbrains.plugins.xml.searchandreplace.persistence.ConstraintTypeSpecificEntry;
import org.jetbrains.plugins.xml.searchandreplace.search.predicates.AttributePredicate;
import org.jetbrains.plugins.xml.searchandreplace.search.predicates.HasSpecificAttribute;
import org.jetbrains.plugins.xml.searchandreplace.search.predicates.XmlElementPredicate;
import org.jetbrains.plugins.xml.searchandreplace.ui.ConstraintType;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.replace.Capture;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.captures.AttributeNameCapture;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.captures.AttributeValueCapture;
import org.jetbrains.plugins.xml.searchandreplace.ui.view.AttributePanel;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;


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

  private interface Comparator {

    boolean compare(String value1, String value2);
    String name();
  }

  private AttributePanel view;
  private List<Comparator> comparators;

  public WithAttributeController(ConstraintType pt) {
    super(pt);
    Comparator[] comparators = new Comparator[]{new Comparator() {
      public boolean compare(String value1, String value2) {
        return value1.equals(value2);
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

      public String toString() {
        return "matches";
      }

      public boolean compare(String value1, String value2) {
        return value1.matches(value2);
      }

      @Override
      public String name() {
        return "matches";
      }
    }};

    this.comparators = Arrays.asList(comparators);
    view = new AttributePanel(this.comparators);
  }

  @Override
  public JPanel getView() {
    return view;
  }

  @Override
  public XmlElementPredicate buildPredicate() {
    return decorateWithNotIfNeccessary(new HasSpecificAttribute(new AttributePredicate() {
      @Override
      public boolean applyToAttribute(XmlAttribute a) {
        if (view.getAttrName().isEmpty()) {
          return true;
        }
        if (a.getName().matches(view.getAttrName())) {
          if (view.getValue().isEmpty()) {
            return true;
          }
          Comparator selected = (Comparator) view.selectedComparator();
          if (selected.compare(a.getValue(), view.getValue())) {
            return true;
          }
        }
        return false;
      }

    }));
  }

  @Override
  public Collection<Capture> provideCaptures(ConstraintController constraintController) {
    ArrayList<Capture> captures = new ArrayList<Capture>();
    captures.add(new AttributeNameCapture(constraintController));
    captures.add(new AttributeValueCapture(constraintController));
    return captures;
  }

  @Override
  public List<ConstraintType> getAllowedChildrenTypes() {
    return new ArrayList<ConstraintType>();
  }
}
