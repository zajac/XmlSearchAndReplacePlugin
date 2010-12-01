package org.jetbrains.plugins.xml.searchandreplace.ui.controller.replace;


import com.intellij.openapi.editor.impl.EditorImpl;
import com.intellij.psi.xml.XmlElement;
import org.jetbrains.plugins.xml.searchandreplace.replace.ReplacementProvider;
import org.jetbrains.plugins.xml.searchandreplace.replace.SetAttribute;
import org.jetbrains.plugins.xml.searchandreplace.replace.SetAttributeHelper;
import org.jetbrains.plugins.xml.searchandreplace.search.Node;
import org.jetbrains.plugins.xml.searchandreplace.ui.view.replace.MyEditorTextField;
import org.jetbrains.plugins.xml.searchandreplace.ui.view.replace.SetAttributeView;

import java.util.Map;

public class SetAttributeController extends ReplacementController  {

  private SetAttributeView myView = new SetAttributeView();

  protected CapturedReplacementController nameResolver;
  protected CapturedReplacementController valueResolver;

  private String nameToSet = null;
  private String valueToSet = null;

  public SetAttributeController() {
    myView.getNameField().setDelegate(new MyEditorTextField.Delegate() {
      @Override
      public void viewDidAppear() {
        EditorImpl nameEditor = getView().getNameEditor();
        if (nameEditor != null) {
          nameResolver = new CapturedReplacementController(nameEditor, getCapturesManager());
        }
        if (nameToSet != null) {
          myView.getNameField().setText(nameToSet);
          nameToSet = null;
        }
      }
    });
    myView.getValueField().setDelegate(new MyEditorTextField.Delegate() {
      @Override
      public void viewDidAppear() {
        EditorImpl valueEditor = getView().getValueEditor();
        if (valueEditor != null) {
          valueResolver = new CapturedReplacementController(valueEditor, getCapturesManager());
        }

        if (valueToSet != null) {
          myView.getValueField().setText(valueToSet);
          valueToSet = null;
        }
      }
    });

  }

  @Override
  public SetAttributeView getView() {
    return myView;
  }

  @Override
  protected ReplacementProvider getReplacementProvider() {
    return new SetAttribute(new MySetAttributeHelper());
  }

  @Override
  public String toString() {
    return "Set attribute";
  }

  @Override
  public ReplacementControllerState getState() {
    ReplacementControllerState state = new ReplacementControllerState();
    state.setAttrName(myView.getNameEditor().getDocument().getText());
    state.setAttrValue(myView.getValueEditor().getDocument().getText());
    return state;
  }

  @Override
  public void loadState(ReplacementControllerState state) {
    nameToSet = state.getAttrName();
    valueToSet = state.getAttrValue();
  }

  protected class MySetAttributeHelper implements SetAttributeHelper {
    @Override
    public String attributeName(Map<Node, XmlElement> match) {
      return nameResolver.resolveCaptures(match);
    }

    @Override
    public String attributeValue(Map<Node, XmlElement> match) {
      return valueResolver.resolveCaptures(match);
    }
  }
}
