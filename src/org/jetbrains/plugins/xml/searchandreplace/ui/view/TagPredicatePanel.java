package org.jetbrains.plugins.xml.searchandreplace.ui.view;

import com.intellij.ui.CollectionComboBoxModel;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Arrays;
import java.util.List;

public class TagPredicatePanel extends JPanel {

  private void createUIComponents() {
    // TODO: place custom component creation code here
  }

  public interface Delegate {
    void stateChanged(TagPredicatePanel tpp);
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
  private JTextField textField;

  public TagPredicatePanel(boolean tagOnly) {
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
    return textField.getText();
  }

  public String getText() {
    return textField.getText();
  }
}
