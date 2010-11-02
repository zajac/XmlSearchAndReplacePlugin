package org.jetbrains.plugins.xml.searchandreplace.ui.predicatetypes;

import com.intellij.psi.xml.XmlAttribute;
import org.jetbrains.plugins.xml.searchandreplace.search.Pattern;
import org.jetbrains.plugins.xml.searchandreplace.search.predicates.And;
import org.jetbrains.plugins.xml.searchandreplace.search.predicates.AttributePredicate;
import org.jetbrains.plugins.xml.searchandreplace.search.predicates.HasSpecificAttribute;
import org.jetbrains.plugins.xml.searchandreplace.search.predicates.XmlElementPredicate;
import org.jetbrains.plugins.xml.searchandreplace.ui.PredicateType;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.PredicateTypeController;
import org.jetbrains.plugins.xml.searchandreplace.ui.view.AttributePanel;

import javax.swing.*;
import java.util.Arrays;

public class WithAttribute implements PredicateType {


  private static class WithAttributeController extends PredicateTypeController {

    private interface Comparator {

      boolean compare(String value1, String value2);
    }

    private AttributePanel view;

    public WithAttributeController() {
      Comparator[] comparators = new Comparator[]{ new Comparator() {
        public boolean compare(String value1, String value2) {
          return value1.equals(value2);
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
      }, new Comparator() {

        public String toString() {
          return "<";
        }

        public boolean compare(String value1, String value2) {
          int i1 = 0, i2 = 0;
          try {
            i1 = Integer.parseInt(value1);
            i2 = Integer.parseInt(value2);
          } catch(NumberFormatException e) {
            e.printStackTrace();
          }
          return i1 < i2;
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
          } catch(NumberFormatException e) {
            e.printStackTrace();
          }
          return i1 > i2;
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
          } catch(NumberFormatException e) {
            e.printStackTrace();
          }
          return i1 <= i2;
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
          } catch(NumberFormatException e) {
            e.printStackTrace();
          }
          return i1 >= i2;
        }
      }};

      view = new AttributePanel(Arrays.asList(comparators));
    }

    @Override
    public JPanel getView() {
      return view;
    }

    @Override
    public XmlElementPredicate buildPredicate() {
      return decorateWithNotIfNeccessary(new HasSpecificAttribute(new AttributePredicate(){
        @Override
        public boolean applyToAttribute(XmlAttribute a) {
          if (view.getAttrName().isEmpty()) {
            return true;
          }
          if (view.getAttrName().equals(a.getName())) {
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
  }

    public String toString() {
        return "With Attribute";
    }

    public PredicateTypeController createNewController() {
        return new WithAttributeController();
    }

    public void addNodeToPattern(Pattern p, Pattern.Node node, Pattern.Node parent) {
        if (parent != null) {
            And predicate = new And(parent.getPredicate(), node.getPredicate());
            parent.setPredicate(predicate);
        } else {
            p.getAllNodes().add(node);
        }
    }
}
