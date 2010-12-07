package org.jetbrains.plugins.xml.searchandreplace.ui.view.search;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
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
    int index = patternsList.getSelectedIndex();
    if (index < patternsList.getModel().getSize() && index >= 0) {
      String name = (String)patternsList.getModel().getElementAt(index);
      if (delegate != null) {
        delegate.patternSelected(this, name);
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

  public String getSelectedPatternName() {
    return (String) patternsList.getSelectedValue();
  }

  public interface Delegate {
    List<String> getPatternsNames(LoadPatternDialog me);
    JPanel getPatternView(LoadPatternDialog me, String patternName);

    void removePattern(LoadPatternDialog me, String patternName);
    void loadSelectedPattern(LoadPatternDialog me);

    void patternSelected(LoadPatternDialog loadPatternDialog, String name);
  }


  private JPanel centerPane;
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
    super.doOKAction();
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
    setModal(false);
    init();
    patternsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    reloadData();
    final LoadPatternDialog loadPatternDialog = this;
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
    removeButton = new JButton("", IconLoader.findIcon("/general/remove.png"));
    removeButton.setBorder(null);

    patternPane = new JPanel();
    patternPane.setLayout(new BoxLayout(patternPane, BoxLayout.Y_AXIS));
  }
}
