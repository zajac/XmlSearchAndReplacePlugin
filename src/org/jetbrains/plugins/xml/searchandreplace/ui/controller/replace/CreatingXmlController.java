package org.jetbrains.plugins.xml.searchandreplace.ui.controller.replace;

import com.intellij.lang.Language;
import com.intellij.openapi.editor.RangeMarker;
import com.intellij.openapi.editor.impl.EditorImpl;
import com.intellij.openapi.project.Project;
import com.intellij.psi.xml.XmlElement;
import com.intellij.psi.xml.XmlTag;
import org.jetbrains.plugins.xml.searchandreplace.replace.ReplacementProvider;
import org.jetbrains.plugins.xml.searchandreplace.replace.Utils;
import org.jetbrains.plugins.xml.searchandreplace.search.Node;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.captures.Capture;
import org.jetbrains.plugins.xml.searchandreplace.ui.view.replace.MyEditorTextField;
import org.jetbrains.plugins.xml.searchandreplace.ui.view.replace.ReplacementView;

import javax.swing.*;
import java.util.Map;

public abstract class CreatingXmlController extends ReplacementController implements CapturedReplacementController.Delegate, MyEditorTextField.Delegate {
  private String textToSet;

  @Override
  public ReplacementControllerState getState() {
    ReplacementControllerState state = new ReplacementControllerState();
    state.setText(myView.getText());
    return state;
  }

  @Override
  public void loadState(ReplacementControllerState state) {
    textToSet = state.getText();
  }

  CapturedReplacementController nested;

  private ReplacementView myView;
  private Project myProject;
  private Language myLanguage;

  public CreatingXmlController(Project project, Language language) {

    myProject = project;
    myLanguage = language;
    myView = new ReplacementView(project);
    myView.setDelegate(this);
  }

  @Override
  public JPanel getView() {
    return myView;
  }

  @Override
  public void viewDidAppear() {
    final EditorImpl editor = myView.getEditor();
    nested = new CapturedReplacementController(editor, getCapturesManager());
    nested.setDelegate(this);
    if (textToSet != null) {
      myView.getTextField().setText(textToSet);
      textToSet = null;
    }
  }

  protected ReplacementProvider createReplacementProviderWithMyXml() {
    return new ReplacementProvider() {
      @Override
      public XmlTag getReplacementFor(XmlElement element, Map<Node, XmlElement> match) {
        return Utils.createXmlElement(myLanguage, myProject, nested.resolveCaptures(match));
      }
    };
  }

  @Override
  public void newCaptureInserted(Capture capture, RangeMarker where) {
    if (nested != null) {
      nested.addCaptureEntry(capture, where);
    }
  }
}
