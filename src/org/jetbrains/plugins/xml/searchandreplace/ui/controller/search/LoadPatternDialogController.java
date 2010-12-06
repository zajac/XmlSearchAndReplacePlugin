package org.jetbrains.plugins.xml.searchandreplace.ui.controller.search;


import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.persistence.PatternsStorage;
import org.jetbrains.plugins.xml.searchandreplace.ui.view.search.LoadPatternDialog;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LoadPatternDialogController implements LoadPatternDialog.Delegate {

  private LoadPatternDialog myView;
  private Project myProject;

  private PatternController selectedPatternController;


  public interface Delegate {
    void patternToLoadSelected(LoadPatternDialogController me, PatternController toShow);
  }

  private Delegate delegate;

  public LoadPatternDialog getView() {
    if (myView == null) {
      myView = new LoadPatternDialog(myProject);
      myView.setDelegate(this);
    }
    return myView;
  }

  public LoadPatternDialogController(@NotNull Project project) {
    myProject = project;
  }

  private PatternsStorage getStorage() {
    return PatternsStorage.getInstance(myProject);
  }

  @Override
  public List<String> getPatternsNames(LoadPatternDialog me) {
    PatternsStorage storage = getStorage();
    if (storage == null) return Collections.emptyList();
    return new ArrayList<String>(storage.getSavedPatternsNames());
  }

  @Override
  public JPanel getPatternView(LoadPatternDialog me, String patternName) {
    if (selectedPatternController == null) {
      PatternsStorage storage = getStorage();
      if (storage != null) {
        selectedPatternController = storage.load(patternName);
      }
    }
    if (selectedPatternController != null) {
      return selectedPatternController.getView();
    }
    return null;
  }

  @Override
  public void createNewPattern(LoadPatternDialog me, String patternName) {
    //TODO
  }

  @Override
  public void removePattern(LoadPatternDialog me, String patternName) {
    getStorage().delete(patternName);
    me.reloadData();
  }

  @Override
  public void loadSelectedPattern(LoadPatternDialog me) {
    if (delegate != null) {
      delegate.patternToLoadSelected(this, selectedPatternController);
    }
  }

  public void setDelegate(Delegate delegate) {
    this.delegate = delegate;
  }
}
