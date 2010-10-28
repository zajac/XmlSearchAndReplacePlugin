package org.jetbrains.plugins.xml.searchandreplace.ui.predicatetypes;

import com.intellij.psi.xml.XmlAttribute;
import com.intellij.ui.CollectionComboBoxModel;
import org.jetbrains.plugins.xml.searchandreplace.search.Pattern;
import org.jetbrains.plugins.xml.searchandreplace.search.predicates.And;
import org.jetbrains.plugins.xml.searchandreplace.search.predicates.AttributePredicate;
import org.jetbrains.plugins.xml.searchandreplace.search.predicates.HasSpecificAttribute;
import org.jetbrains.plugins.xml.searchandreplace.search.predicates.XmlElementPredicate;
import org.jetbrains.plugins.xml.searchandreplace.ui.PredicateType;
import org.jetbrains.plugins.xml.searchandreplace.ui.PredicateTypeController;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class WithAttribute implements PredicateType {

    private interface Comparator {
        boolean compare(String value1, String value2);
    }

    public String toString() {
        return "With Attribute";
    }

    public PredicateTypeController createNewController() {
        final JPanel view = new JPanel();
        view.setLayout(new FlowLayout());
        final JTextField attributeNameField = new JTextField();
        view.add(attributeNameField);
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
        final JComboBox compareFunctionChooser = new JComboBox(new CollectionComboBoxModel(Arrays.asList(comparators), comparators[0]));
        view.add(compareFunctionChooser);
        final JTextField valueField = new JTextField();
        view.add(valueField);
        return new PredicateTypeController() {
            @Override
            public JPanel getView() {
                return view;
            }

            @Override
            public XmlElementPredicate buildPredicate() {
                return decorateWithNotIfNeccessary(new HasSpecificAttribute(new AttributePredicate(){
                    @Override
                    public boolean applyToAttribute(XmlAttribute a) {
                        if (attributeNameField.getText().isEmpty()) {
                            return true;
                        }
                        if (attributeNameField.getText().equals(a.getName())) {
                            if (valueField.getText().isEmpty()) {
                                return true;
                            }
                            Comparator selected = (Comparator) compareFunctionChooser.getModel().getSelectedItem();
                            if (selected.compare(a.getValue(), valueField.getText())) {
                                return true;
                            }
                        }
                        return false;
                    }

                }));
            }
        };
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
