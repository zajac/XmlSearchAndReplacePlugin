package org.jetbrains.plugins.xml.searchandreplace.ui.view.search;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.IconLoader;
import com.intellij.ui.CollectionListModel;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class LoadPatternDialog extends DialogWrapper implements ListSelectionListener {

  @Override
  public void valueChanged(ListSelectionEvent e) {
    int index = e.getFirstIndex();
    if (index < patternsList.getModel().getSize()) {
      String name = (String)patternsList.getModel().getElementAt(index);
      if (delegate != null) {
        JPanel patternView = delegate.getPatternView(this, name);
        patternPane.removeAll();
        patternNameLabel.setText("");
        if (patternView != null) {
          patternPane.add(patternView);
          patternNameLabel.setText(name);
        }
        patternPane.updateUI();
        getWindow().pack();
      }
    } else {
      nothingToShow();
    }
  }

  public interface Delegate {
    List<String> getPatternsNames(LoadPatternDialog me);
    JPanel getPatternView(LoadPatternDialog me, String patternName);
    void createNewPattern(LoadPatternDialog me, String patternName);
    void removePattern(LoadPatternDialog me, String patternName);
    void loadSelectedPattern(LoadPatternDialog me);
  }


  private JPanel centerPane;
  private JButton addButton;
  private JButton removeButton;
  private JList patternsList;
  private JPanel patternPane;
  private JScrollPane patternListScrollPane;
  private JLabel patternNameLabel;

  private Delegate delegate;

  @Override
  protected void doOKAction() {
    if (patternsList.getSelectedValue() != null && delegate != null) {
      patternPane.removeAll();
      delegate.loadSelectedPattern(this);
    }
    super.doOKAction();    //To change body of overridden methods use File | Settings | File Templates.
  }

  public void setDelegate(Delegate d) {
    if (delegate == null && d != null) {
      delegate = d;
      reloadData();
    } else {
      delegate = d;
    }
  }

  public Delegate getDelegate() {
    return delegate;
  }

  public LoadPatternDialog(Project project) {
    super(project);
    init();
    patternsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    reloadData();
    final LoadPatternDialog loadPatternDialog = this;
    addButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (delegate != null) {
          String name = Messages.showInputDialog("enter pattern name", "add new pattern", null);
          if (name != null && !name.isEmpty()) {
            delegate.createNewPattern(loadPatternDialog, name);
          }
        }
      }
    });
    removeButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (delegate != null) {
          delegate.removePattern(loadPatternDialog, (String)patternsList.getSelectedValue());
        }
      }
    });
    patternsList.addListSelectionListener(this);
    nothingToShow();
  }

  private void nothingToShow() {
    patternPane.add(new JLabel("Nothing to show"));
  }

  public void reloadData() {
    if (delegate != null) {
      List<String> patternsNames = delegate.getPatternsNames(this);
      patternsList.setModel(new CollectionListModel(patternsNames));
    }
  }

  @Override
  protected JComponent createCenterPanel() {
    return centerPane;
  }

  private void createUIComponents() {
    addButton = new JButton("", IconLoader.findIcon("/general/add.png"));
    removeButton = new JButton("", IconLoader.findIcon("/general/remove.png"));
    addButton.setBorder(null);
    removeButton.setBorder(null);

    patternPane = new JPanel();
    patternPane.setLayout(new BoxLayout(patternPane, BoxLayout.Y_AXIS));
  }
}
