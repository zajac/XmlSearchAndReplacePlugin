package org.jetbrains.plugins.xml.searchandreplace.ui;

import com.intellij.ui.CollectionComboBoxModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class PredicatePanel extends JPanel {

    private static final PredicatePanelDelegate DUMMY_DELEGATE = new PredicatePanelDelegate() {

        public void addChild(PredicatePanel panel) {

        }

        public List<PredicateType> getPredicateTypes(PredicatePanel panel) {
            return new ArrayList<PredicateType>();
        }

        public void predicateTypeSelected(PredicatePanel panel, PredicateType selection) {

        }
    };
    private PredicatePanelDelegate delegate = DUMMY_DELEGATE;

    private JButton addChildButton;
    private JComboBox predicateTypeChooser;
    private JPanel predicateTypeSpecific;

    public PredicatePanelDelegate getDelegate() {
        return delegate;
    }

    public void setDelegate(PredicatePanelDelegate delegate) {
        this.delegate = delegate == null ? DUMMY_DELEGATE : delegate;
        reloadData();
        if (predicateTypeChooser == null) {
            getDelegate().predicateTypeSelected(this, null);
        }
    }

    public void reloadData() {
        if (predicateTypeChooser == null) {
            return;
        }
        List<PredicateType> predicateTypes = getDelegate().getPredicateTypes(this);
        final PredicatePanel thisPanel = this;
        ComboBoxModel predicateTypeChooserModel = new CollectionComboBoxModel(predicateTypes, null) {
            @Override
            public void setSelectedItem(Object anItem) {
                if (anItem != getSelectedItem()) {
                    super.setSelectedItem(anItem);
                    getDelegate().predicateTypeSelected(thisPanel, (PredicateType)anItem);
                }
            }
        };
        predicateTypeChooser.setModel(predicateTypeChooserModel);
    }

    private PredicateType getSelectedPredicateType() {
        if (predicateTypeChooser == null) return null;
        return (PredicateType) predicateTypeChooser.getModel().getSelectedItem();
    }

    public void setPredicateTypeSpecificView(JPanel view) {
        predicateTypeSpecific.removeAll();
        predicateTypeSpecific.add(view);
        predicateTypeSpecific.updateUI();
    }

    public PredicatePanel(boolean canHaveChildren, boolean isRoot) {

        if (canHaveChildren) {
            addChildButton = new JButton("+");
            final PredicatePanel thisPanel = this;
            addChildButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    getDelegate().addChild(thisPanel);
                }
            });
        }
        if (!isRoot) {
            predicateTypeChooser = new JComboBox();
        }
        reloadData();


        predicateTypeSpecific = new JPanel();

        if (canHaveChildren) {
            add(addChildButton);
        }
        if (!isRoot) {
            add(predicateTypeChooser);
        }
        add(predicateTypeSpecific);
    }
}
