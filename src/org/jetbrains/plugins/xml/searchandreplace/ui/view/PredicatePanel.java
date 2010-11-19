package org.jetbrains.plugins.xml.searchandreplace.ui.view;

import com.intellij.ui.CollectionComboBoxModel;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.replace.Capture;
import org.jetbrains.plugins.xml.searchandreplace.ui.PredicateType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PredicatePanel extends JPanel {

  private static final PredicatePanelDelegate DUMMY_DELEGATE = new PredicatePanelDelegate() {

    public void addChild(PredicatePanel panel) {

    }

    public void removeMe(PredicatePanel panel) {

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
  private JPanel centerPanel;
  private JButton removeButton;
  private JPanel indentPanel;
  private JPanel capturesPanel;
  private int indent;

  public boolean canHaveChildren() {
    return canHaveChildren;
  }

  private boolean canHaveChildren;

  public PredicatePanelDelegate getDelegate() {
    return delegate;
  }

  public void setDelegate(PredicatePanelDelegate delegate) {
    this.delegate = delegate == null ? DUMMY_DELEGATE : delegate;
    reloadData();
    if (!predicateTypeChooser.isVisible()) {
      getDelegate().predicateTypeSelected(this, null);
    }
  }

  public void reloadData() {
    if (!predicateTypeChooser.isVisible()) {
      return;
    }
    List<PredicateType> predicateTypes = getDelegate().getPredicateTypes(this);
    final PredicatePanel thisPanel = this;
    ComboBoxModel predicateTypeChooserModel = new CollectionComboBoxModel(predicateTypes, null) {
      @Override
      public void setSelectedItem(Object anItem) {
        if (anItem != getSelectedItem()) {
          super.setSelectedItem(anItem);
          getDelegate().predicateTypeSelected(thisPanel, (PredicateType) anItem);
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

  public PredicatePanel(boolean canHaveChildren, boolean canBeRemoved, int indent) {
    this.canHaveChildren = canHaveChildren;

    final PredicatePanel thisPanel = this;

    addChildButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        getDelegate().addChild(thisPanel);
      }
    });

    removeButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        getDelegate().removeMe(thisPanel);
      }
    });
    if (!canBeRemoved) {
      predicateTypeChooser.setVisible(false);
      removeButton.setVisible(false);
    }
    reloadData();

    if (!canHaveChildren) {
      addChildButton.setVisible(false);
    }
    setIndent(indent);
    ((FlowLayout)predicateTypeSpecific.getLayout()).setVgap(0);
    add(centerPanel);
  }

  private void setIndent(int indent) {
    this.indent = indent;
    updateIndent();
  }

  private void updateIndent() {
    indentPanel.setMinimumSize(new Dimension(30 * indent, 10));
  }

  public void setCanHaveChildren(boolean b) {
    canHaveChildren = b;
    addChildButton.setVisible(b);
    updateIndent();
    updateUI();
  }

  private void createUIComponents() {
    capturesPanel = new JPanel();
    capturesPanel.setLayout(new BoxLayout(capturesPanel, BoxLayout.PAGE_AXIS));    
  }

  public void setCaptures(Collection<Capture> captures) {
    capturesPanel.removeAll();
    for (Capture capture : captures) {
      JComponent captureView = new CaptureView(capture);
      capturesPanel.add(captureView);
    }
    capturesPanel.updateUI();
  }
}
