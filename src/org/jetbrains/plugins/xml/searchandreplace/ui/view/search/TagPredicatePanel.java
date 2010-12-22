package org.jetbrains.plugins.xml.searchandreplace.ui.view.search;

import com.intellij.openapi.project.Project;
import com.intellij.ui.CollectionComboBoxModel;
import com.intellij.ui.EditorTextField;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Arrays;
import java.util.List;

public class TagPredicatePanel extends JPanel {

  private Project myProject;

  private void createUIComponents() {
    editorTextField = Util.createRegexpEditorWithTagNameCompletion(myProject, false);
  }

  public void setText(String text) {
    editorTextField.setText(text);
  }

  public void setSelectedCard(String value) {
    textOrTag.setSelectedItem(value);
  }

  public void setPreviewMode(boolean previewMode) {
    editorTextField.setEnabled(!previewMode);
    textOrTag.setEnabled(!previewMode);
  }

  public interface Delegate {
    void stateChanged(TagPredicatePanel tpp);
  }

  public void useRegexps(boolean b) {
    Util.useRegexps(editorTextField, myProject, b);
  }

  public static final String TAG = "Tag";
  public static final String TEXT = "Text";
  private static final String DEFAULT_CARD = TAG;

  private Delegate delegate;

  public Delegate getDelegate() {
    return delegate;
  }

  public void setDelengate(Delegate delegate) {
    this.delegate = delegate;
  }

  private JPanel pane;
  private JComboBox textOrTag;
  private EditorTextField editorTextField;

  public TagPredicatePanel(boolean tagOnly, Project project) {
    this.myProject = project;
    Util.useRegexps(editorTextField, myProject, false);
    List<String> items = tagOnly ? Arrays.asList(TAG) : Arrays.asList(TAG, TEXT);
    textOrTag.setModel(new CollectionComboBoxModel(items, DEFAULT_CARD));
    setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
    pane.setAlignmentX(JComponent.LEFT_ALIGNMENT);
    add(pane);
    final TagPredicatePanel tagPredicatePanel = this;
    textOrTag.addItemListener(new ItemListener() {
      @Override
      public void itemStateChanged(ItemEvent itemEvent) {
        if (getDelegate() != null) {
          getDelegate().stateChanged(tagPredicatePanel);
        }
      }
    });

    updateUI();
  }

  public String selectedCard() {
    return (String) textOrTag.getSelectedItem();
  }

  public String getTagName() {
    return editorTextField.getText();
  }

  public String getText() {
    return editorTextField.getText();
  }
}
