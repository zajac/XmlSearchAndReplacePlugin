package org.jetbrains.plugins.xml.searchandreplace.ui.view.search;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class PatternView extends JPanel {

  public interface Delegate {
    void useRegexps(boolean use);
  }

  private JPanel constraintsPanel;
  private JPanel centerPane;
  private JCheckBox useRegexCheckbox;
  private Delegate delegate;

  public Delegate getDelegate() {
    return delegate;
  }

  public void setDelegate(Delegate delegate) {
    this.delegate = delegate;
  }

  public void setUseRegexps(boolean use) {
    useRegexCheckbox.setSelected(use);
  }

  public boolean getUseRegexps() {
    return useRegexCheckbox.isSelected();
  }

  public PatternView() {
    add(centerPane);
    updateUI();
  }

  public void addConstraintView(ConstraintPanel panel, ConstraintPanel parent) {
    if (parent == null) {
      constraintsPanel.add(panel);
    } else {
      parent.addChildPredicatePanel(panel);
    }
    updateUI();
  }

  public void removeConstraintView(ConstraintPanel view, ConstraintPanel parentPanel) {
    if (parentPanel != null) {
      parentPanel.removeChildPredicatePanel(view);
    } else {
      view.getParent().remove(view);
    }
    useRegexCheckbox.addItemListener(new ItemListener() {
      @Override
      public void itemStateChanged(ItemEvent e) {
        getDelegate().useRegexps(useRegexCheckbox.isSelected());
      }
    });
    updateUI();
  }

  private void createUIComponents() {
    constraintsPanel = new JPanel();
    LayoutManager lm = new BoxLayout(constraintsPanel, BoxLayout.Y_AXIS);
    constraintsPanel.setLayout(lm);
  }
}
