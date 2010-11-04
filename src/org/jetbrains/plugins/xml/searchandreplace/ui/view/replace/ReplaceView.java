package org.jetbrains.plugins.xml.searchandreplace.ui.view.replace;

import com.intellij.ui.CollectionComboBoxModel;

import javax.swing.*;
import java.util.List;


public class ReplaceView extends JPanel {

  private ReplaceViewDelegate delegate;
  private JPanel centerPane;
  private JComboBox replacementTypeChooser;
  private JPanel replacementSpecific;

  public interface ReplaceViewDelegate {
    void replacementTypeSelected(ReplaceView view, Object selectedItem);
  }

  public ReplaceView(List replacementTypes) {
    add(centerPane);
    final ReplaceView thisView = this;
    replacementTypeChooser.setModel(new CollectionComboBoxModel(replacementTypes, null) {
      @Override
      public void setSelectedItem(Object anItem) {
        super.setSelectedItem(anItem);
        if (delegate != null) {
          delegate.replacementTypeSelected(thisView, anItem);
        }
      }
    });
  }

  public void setDelegate(ReplaceViewDelegate delegate) {
    this.delegate = delegate;
  }

  public ReplaceViewDelegate getDelegate() {
    return delegate;
  }

  public void setReplacementSpecificView(JPanel view) {
    replacementSpecific.removeAll();
    replacementSpecific.add(view);
    replacementSpecific.updateUI();
  }
}
