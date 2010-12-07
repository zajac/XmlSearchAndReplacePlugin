package org.jetbrains.plugins.xml.searchandreplace.ui.controller.search;


import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.persistence.PatternsStorage;
import org.jetbrains.plugins.xml.searchandreplace.ui.view.search.LoadPatternDialog;
import org.jetbrains.plugins.xml.searchandreplace.ui.view.search.PatternView;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LoadPatternDialogController implements LoadPatternDialog.Delegate, PatternsStorage.PatternsStorageListener {

  private LoadPatternDialog myView;
  private Project myProject;

  private PatternController selectedPatternController;

  @Override
  public void storageStateChanged() {
    myView.reloadData();
  }


  public interface Delegate {
    void loadPattern(LoadPatternDialogController me, PatternController toShow);
  }

  private Delegate delegate;

  public LoadPatternDialog getView() {
    if (myView == null) {
      myView = new LoadPatternDialog(myProject);
      final LoadPatternDialogController loadPatternDialogController = this;
      myView.getWindow().addWindowListener(new WindowAdapter() {
        @Override
        public void windowClosed(WindowEvent e) {
          getStorage().removeListener(loadPatternDialogController);
        }
      });
      myView.setDelegate(this);
    }
    return myView;
  }

  public LoadPatternDialogController(@NotNull Project project) {
    myProject = project;
    getStorage().addListener(this);
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
      load(patternName);
    }
    if (selectedPatternController != null) {
      PatternView view = selectedPatternController.getView();
      selectedPatternController.setPreviewMode(true);
      return view;
    }
    return null;
  }

  private void load(String patternName) {
    PatternsStorage storage = getStorage();
    if (storage != null) {
      selectedPatternController = storage.load(patternName);
    }
  }

  @Override
  public void removePattern(LoadPatternDialog me, String patternName) {
    getStorage().delete(patternName);
    me.reloadData();
  }

  @Override
  public void loadSelectedPattern(LoadPatternDialog me) {
    if (delegate != null) {
      selectedPatternController.setPreviewMode(false);
      delegate.loadPattern(this, selectedPatternController);
    }
  }

  @Override
  public void patternSelected(LoadPatternDialog loadPatternDialog, String name) {
    load(name);
  }

  public void setDelegate(Delegate delegate) {
    this.delegate = delegate;
  }
}
