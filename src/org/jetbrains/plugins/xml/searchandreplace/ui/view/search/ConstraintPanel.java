package org.jetbrains.plugins.xml.searchandreplace.ui.view.search;

import com.intellij.ui.CollectionComboBoxModel;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.ConstraintType;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.captures.Capture;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ConstraintPanel extends JPanel {

  private static final ConstraintPanelDelegate DUMMY_DELEGATE = new ConstraintPanelDelegate() {

    public void addChild(ConstraintPanel panel) {

    }

    public void removeMe(ConstraintPanel panel) {

    }

    public List<ConstraintType> getChildrentConstraintTypes(ConstraintPanel panel) {
      return new ArrayList<ConstraintType>();
    }

    public void constraintTypeSelected(ConstraintPanel panel, ConstraintType selection) {

    }
  };

  private ConstraintPanelDelegate delegate = DUMMY_DELEGATE;

  private JButton addChildButton;
  private JComboBox predicateTypeChooser;
  private JPanel predicateTypeSpecific;
  private JPanel centerPanel;
  private JButton removeButton;
  private JPanel capturesPanel;
  private JPanel childrenSpace;

  public JPanel getChildrenSpace() {
    return childrenSpace;
  }

  public boolean canHaveChildren() {
    return canHaveChildren;
  }

  private boolean canHaveChildren;

  public ConstraintPanelDelegate getDelegate() {
    return delegate;
  }

  public void setDelegate(ConstraintPanelDelegate delegate) {
    this.delegate = delegate == null ? DUMMY_DELEGATE : delegate;
    reloadData();

  }

  public void reloadData() {
    if (!predicateTypeChooser.isVisible()) {
      return;
    }
    List<ConstraintType> constraintTypes = getDelegate().getChildrentConstraintTypes(this);
    final ConstraintPanel thisPanel = this;
    ComboBoxModel predicateTypeChooserModel = new CollectionComboBoxModel(constraintTypes, null) {
      @Override
      public void setSelectedItem(Object anItem) {
        if (anItem != getSelectedItem()) {
          super.setSelectedItem(anItem);
          getDelegate().constraintTypeSelected(thisPanel, (ConstraintType) anItem);
        }
      }
    };
    predicateTypeChooser.setModel(predicateTypeChooserModel);
  }

  private ConstraintType getSelectedPredicateType() {
    if (predicateTypeChooser == null) return null;
    return (ConstraintType) predicateTypeChooser.getModel().getSelectedItem();
  }

  public void setPredicateTypeSpecificView(JPanel view) {
    predicateTypeSpecific.removeAll();
    predicateTypeSpecific.add(view);
    predicateTypeSpecific.updateUI();
  }

  public ConstraintPanel(boolean canHaveChildren, boolean canBeRemoved) {
    this.canHaveChildren = canHaveChildren;

    final ConstraintPanel thisPanel = this;

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
      JPanel parent = (JPanel) predicateTypeChooser.getParent();
      parent.remove(predicateTypeChooser);
      parent.setPreferredSize(new Dimension(40, -1));
      parent.updateUI();
    }
    reloadData();

    if (!canHaveChildren) {
      addChildButton.setVisible(false);
    }
    predicateTypeSpecific.setLayout(new BoxLayout(predicateTypeSpecific, BoxLayout.LINE_AXIS));
    childrenSpace.setLayout(new BoxLayout(childrenSpace, BoxLayout.Y_AXIS));
    childrenSpace.setAlignmentX(JComponent.LEFT_ALIGNMENT);
    setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
    centerPanel.setAlignmentX(JComponent.LEFT_ALIGNMENT);
    add(centerPanel);
    childrenSpace.setVisible(false);
  }

  public void setCanHaveChildren(boolean b) {
    canHaveChildren = b;
    addChildButton.setVisible(b);
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
      captureView.setAlignmentX(JComponent.RIGHT_ALIGNMENT);
      capturesPanel.add(captureView);
    }
    capturesPanel.updateUI();
  }

  public void addChildPredicatePanel(ConstraintPanel panel) {
    if (!childrenSpace.isVisible()) {
      childrenSpace.setVisible(true);
    }
    panel.setAlignmentX(JComponent.LEFT_ALIGNMENT);
    childrenSpace.add(panel);
    childrenSpace.updateUI();
  }

  public void removeChildPredicatePanel(ConstraintPanel view) {
    childrenSpace.remove(view);
    if (childrenSpace.getComponentCount() == 0) {
      childrenSpace.setVisible(false);
    }
  }

  public void highlightCaptures(Capture active) {
    for (Component c : capturesPanel.getComponents()) {
      CaptureView captureView = (CaptureView) c;
      Color color = captureView.getCapture().presentation().getBackgroundColor();
      if (captureView.getCapture() == active) {
        captureView.setBackground(Color.GREEN);
      } else {
        captureView.setBackground(color);
      }
    }
  }

  public void setSelectedConstraintType(ConstraintType selectedConstraintType) {
    predicateTypeChooser.setSelectedItem(selectedConstraintType);
  }
}
