package org.jetbrains.plugins.xml.searchandreplace.ui.controller.replace;



import com.intellij.lang.Language;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.project.Project;
import com.intellij.util.ArrayUtil;
import org.jetbrains.plugins.xml.searchandreplace.replace.InsertIntoTag;
import org.jetbrains.plugins.xml.searchandreplace.replace.InsertNearElement;
import org.jetbrains.plugins.xml.searchandreplace.replace.ReplacementProvider;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.captures.CapturesManager;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.replace.persistence.ReplacementEntry;
import org.jetbrains.plugins.xml.searchandreplace.ui.view.replace.ReplaceView;

import java.util.Arrays;

public class ReplaceController implements ReplaceView.ReplaceViewDelegate, PersistentStateComponent<ReplacementEntry> {

  private ReplaceView myView;
  private ReplacementController myReplacementController;
  private CapturesManager capturesManager;
  ReplacementController[] controllers;

  public Project getProject() {
    return project;
  }

  public Language getLanguage() {
    return language;
  }

  private Project project;
  private Language language;

  public ReplaceView getView() {
    return myView;
  }

  public CapturesManager getCapturesManager() {
    return capturesManager;
  }

  public void setCapturesManager(CapturesManager capturesManager) {
    this.capturesManager = capturesManager;
    for (ReplacementController rc : controllers) {
      rc.setCapturesManager(capturesManager);
    }
  }

  public ReplaceController(Project project, Language language) {
    this.project = project;
    this.language = language;
    controllers = new ReplacementController[]{
            new InsertIntoTagController(project, language, InsertIntoTag.Anchor.BEGIN),
            new InsertIntoTagController(project, language, InsertIntoTag.Anchor.END),
            new InsertNearElementController(project, language, InsertNearElement.Anchor.BEFORE),
            new InsertNearElementController(project, language, InsertNearElement.Anchor.AFTER),
            new ReplaceWithContentsController(project, language),
            new ReplaceButLeaveContentsController(project, language),
            new ReplaceContentsOnlyController(project, language),
            new SetAttributeController(),
            new RemoveAttributeController()
    };

    myView = new ReplaceView(Arrays.asList(controllers));
    myView.setDelegate(this);
  }

  public void replacementTypeSelected(ReplaceView view, Object selectedItem) {
    myReplacementController = (ReplacementController) selectedItem;
    view.setReplacementSpecificView(myReplacementController.getView());
//    myReplacementController.viewDidAppear();
  }

  public ReplacementProvider getReplacementProvider() {
    return myReplacementController == null ? null : myReplacementController.getReplacementProvider();
  }

  @Override
  public ReplacementEntry getState() {
    ReplacementEntry state = new ReplacementEntry();
    if (myReplacementController == null) return state;

    state.setReplacementControllerIndex(ArrayUtil.find(controllers, myReplacementController));

    ReplacementControllerState nestedState = myReplacementController.getState();
    state.setReplacementControllerState(nestedState);
    return state;
  }

  @Override
  public void loadState(ReplacementEntry state) {


    int index = state.getReplacementControllerIndex();
    myReplacementController = controllers[index];
    myView.setReplacementSpecificView(myReplacementController.getView());
    myView.getReplacementTypeChooser().setSelectedItem(myReplacementController);

    if (myReplacementController != null) {
      myReplacementController.loadState(state.getReplacementControllerState());
    }
  }
}
