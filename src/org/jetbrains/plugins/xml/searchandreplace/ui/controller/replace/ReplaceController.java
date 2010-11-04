package org.jetbrains.plugins.xml.searchandreplace.ui.controller.replace;


import com.intellij.lang.Language;
import com.intellij.openapi.project.Project;
import org.jetbrains.plugins.xml.searchandreplace.replace.InsertIntoTag;
import org.jetbrains.plugins.xml.searchandreplace.replace.InsertNearElement;
import org.jetbrains.plugins.xml.searchandreplace.replace.ReplacementProvider;
import org.jetbrains.plugins.xml.searchandreplace.ui.view.replace.ReplaceView;

import java.util.Arrays;

public class ReplaceController implements ReplaceView.ReplaceViewDelegate {

  private ReplaceView myView;
  private ReplacementController myReplacementController;

  public ReplaceView getView() {
    return myView;
  }

  public ReplaceController(Project project, Language language) {
    ReplacementController[] controllers = new ReplacementController[]{
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
  }

  public ReplacementProvider getReplacementProvider() {
    return myReplacementController == null ? null : myReplacementController.getReplacementProvider();
  }

}
