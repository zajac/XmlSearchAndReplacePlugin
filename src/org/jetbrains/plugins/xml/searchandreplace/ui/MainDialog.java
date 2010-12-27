package org.jetbrains.plugins.xml.searchandreplace.ui;

import com.intellij.ide.util.scopeChooser.ScopeChooserCombo;
import com.intellij.lang.xml.XMLLanguage;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.IconLoader;
import com.intellij.psi.search.SearchScope;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.plugins.xml.searchandreplace.replace.ReplacementProvider;
import org.jetbrains.plugins.xml.searchandreplace.search.Pattern;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.replace.ReplaceController;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.replace.persistence.ReplacementsStorage;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.LoadPatternDialogController;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.PatternController;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.persistence.PatternsStorage;
import org.jetbrains.plugins.xml.searchandreplace.ui.view.replace.ReplaceView;
import org.jetbrains.plugins.xml.searchandreplace.ui.view.search.LoadPatternDialog;
import org.jetbrains.plugins.xml.searchandreplace.ui.view.search.PatternView;

import javax.swing.*;
import java.awt.event.*;

public class MainDialog extends DialogWrapper implements ContainerListener, PatternController.Delegate, LoadPatternDialogController.Delegate {

  private boolean badInput = false;

  private LivePreview livePreview;

  @Override
  public void badInput(PatternController patternController) {
    Messages.showErrorDialog("You have typed incorrect pattern", "Error");
    badInput = true;
  }

  @Override
  public void pleaseAutoresizeWindow(PatternController c) {
    getWindow().pack();
  }

  @Override
  public void loadPattern(LoadPatternDialogController me, PatternController toShow) {
    setPatternController(toShow);
  }

  public interface MainDialogDelegate {
    void performSearch(MainDialog d);
  }

  public void setPatternController(PatternController patternController) {
    if (livePreview != null) {
      livePreview.tearDown();
    }
    livePreview = null;
    this.patternController = patternController;
    patternPanel.removeAll();
    if (patternController != null) {
      if (replaceController != null) {
        replaceController.setCapturesManager(patternController.getCapturesManager());
      }
      patternController.setDelegate(this);

      PatternView view = patternController.getView();
      view.setAlignmentX(JComponent.LEFT_ALIGNMENT);
      patternPanel.add(view);
      patternPanel.updateUI();

      getWindow().pack();
      PatternsStorage.getInstance(project).setRecent(patternController);

      livePreview = new LivePreview(editor, patternController);
    }
  }



  public PatternController getPatternController() {
    return patternController;
  }

  private void createUIComponents() {
    patternPanel = new JPanel();
    patternPanel.setLayout(new BoxLayout(patternPanel, BoxLayout.Y_AXIS));

    scopeChooserCombo = new ScopeChooserCombo(project, false, false, null);

    PatternsStorage service = PatternsStorage.getInstance(project);

    if (service != null) {
      setPatternController(service.getRecent());
    }
    if (patternController == null) {
      setPatternController(new PatternController(project));
      if (service != null) {
        service.setRecent(patternController);
      }
    }

    ReplacementsStorage storage = ReplacementsStorage.getInstance(project);
    if (storage != null) {
      replaceController = storage.getRecent();
    }
    if (replaceController == null) {
      replaceController = new ReplaceController(project, XMLLanguage.INSTANCE);
      if (storage != null) {
        storage.setRecent(replaceController);
      }
    }
    replaceController.setCapturesManager(patternController.getCapturesManager());
    replaceView = replaceController.getView();

    saveButton = new JButton("", IconLoader.findIcon("/actions/menu-saveall.png"));
    saveButton.setBorder(null);
    loadButton = new JButton("", IconLoader.findIcon("/actions/menu-open.png"));
    loadButton.setBorder(null);
  }

  private void save() {
    String name = Messages.showInputDialog(project, "pattern name", "save", null);
    if (name != null && !name.isEmpty()) {
      PatternsStorage storage = PatternsStorage.getInstance(project);
      storage.saveAs(patternController, name);
    }
  }

  public void componentAdded(ContainerEvent e) {
    getWindow().pack();
  }

  public void componentRemoved(ContainerEvent e) {
    getWindow().pack();
  }

  private JPanel centerPanel;

  private JPanel scopePanel;

  private PatternController patternController;

  private JPanel patternView;
  private ReplaceController replaceController;

  private ReplaceView replaceView;
  private JButton saveButton;
  private JButton loadButton;
  private JPanel patternPanel;
  private ScopeChooserCombo scopeChooserCombo;
  private MainDialogDelegate delegate;

  private Project project;
  private Module module;
  @Nullable
  private Editor editor;
  private Pattern pattern;

  public MainDialog(Project project, Module module, @Nullable Editor activeEditor) {
    super(project);

    this.project = project;
    this.module = module;
    editor = activeEditor;

    setTitle("Xml search and replace");
    setModal(false);
    setOKButtonText("Find");


    replaceView.getReplacementSpecificView().addContainerListener(new ContainerListener() {

      public void componentAdded(ContainerEvent e) {
        getWindow().pack();
      }

      public void componentRemoved(ContainerEvent e) {
      }
    });

    patternController.setDelegate(this);

    saveButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        save();
      }
    });

    loadButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        LoadPatternDialogController loadController = new LoadPatternDialogController(MainDialog.this.project);
        loadController.setDelegate(MainDialog.this);
        LoadPatternDialog view = loadController.getView();
        view.show();
      }
    });

    init();

    getWindow().addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosed(WindowEvent windowEvent) {
        if (livePreview != null) {
          livePreview.cleanUp();
        }
      }
    });
  }



  @Override
  protected JComponent createCenterPanel() {
    return centerPanel;
  }

  public ReplacementProvider getReplacementProvider() {
    return replaceController.getReplacementProvider();
  }

  public SearchScope getSelectedScope() {
    return scopeChooserCombo.getSelectedScope();
  }

  public MainDialogDelegate getDelegate() {
    return delegate;
  }

  public void setDelegate(MainDialogDelegate delegate) {
    this.delegate = delegate;
  }

  public Pattern getPattern() {
    return pattern;
  }

  @Override
  protected void doOKAction() {
    livePreview.cleanUp();
    pattern = patternController.buildPattern();
    if (badInput) {
      badInput = false;
      return;
    }
    if (getDelegate() != null) {
      getDelegate().performSearch(this);
    }
    super.doOKAction();
  }
}
