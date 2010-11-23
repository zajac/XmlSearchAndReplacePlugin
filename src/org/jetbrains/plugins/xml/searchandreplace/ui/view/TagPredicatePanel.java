package org.jetbrains.plugins.xml.searchandreplace.ui.view;

import com.intellij.ui.CollectionComboBoxModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Arrays;

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
  private JTextField tagField;
  private JPanel cardsPanel;
  private JTextField textField;

  public TagPredicatePanel(boolean tagOnly) {
    textOrTag.setModel(new CollectionComboBoxModel(Arrays.asList(TAG, TEXT), DEFAULT_CARD));
    final TagPredicatePanel tpp = this;
    textOrTag.addItemListener(new ItemListener() {
      @Override
      public void itemStateChanged(ItemEvent itemEvent) {
        ((CardLayout)cardsPanel.getLayout()).show(cardsPanel, (String)itemEvent.getItem());
        if(getDelegate() != null) {
          getDelegate().stateChanged(tpp);
        }
      }
    });
    if (tagOnly) {
      textOrTag.setVisible(false);
    }
    ((CardLayout)cardsPanel.getLayout()).show(cardsPanel, DEFAULT_CARD);

    setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
    pane.setAlignmentX(JComponent.LEFT_ALIGNMENT);
    add(pane);
  }

  public String selectedCard() {
    return (String) textOrTag.getSelectedItem();
  }

  public String getTagName() {
    return tagField.getText();
  }

  public String getText() {
    return textField.getText();
  }
}
