package org.jetbrains.plugins.xml.searchandreplace.ui.controller.replace;


import com.intellij.openapi.editor.impl.EditorImpl;
import com.intellij.psi.xml.XmlElement;
import org.jetbrains.plugins.xml.searchandreplace.replace.ReplacementProvider;
import org.jetbrains.plugins.xml.searchandreplace.replace.SetAttribute;
import org.jetbrains.plugins.xml.searchandreplace.replace.SetAttributeHelper;
import org.jetbrains.plugins.xml.searchandreplace.search.Node;
import org.jetbrains.plugins.xml.searchandreplace.ui.view.replace.SetAttributeView;

import java.util.Map;

public class SetAttributeController extends ReplacementController {

  private SetAttributeView myView = new SetAttributeView();

  protected CapturedReplacementController nameResolver;
  protected CapturedReplacementController valueResolver;

  @Override
  public SetAttributeView getView() {
    return myView;
  }

  @Override
  public void viewDidAppear() {
    super.viewDidAppear();
    EditorImpl nameEditor = getView().getNameEditor();
    if (nameEditor != null) {
      nameResolver = new CapturedReplacementController(nameEditor);
    }

    EditorImpl valueEditor = getView().getValueEditor();
    if (valueEditor != null) {
      valueResolver = new CapturedReplacementController(valueEditor);
    }
  }

  @Override
  protected ReplacementProvider getReplacementProvider() {
    return new SetAttribute(new MySetAttributeHelper());
  }

  @Override
  public String toString() {
    return "Set attribute";
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
