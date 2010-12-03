package org.jetbrains.plugins.xml.searchandreplace.ui;

import com.intellij.lang.xml.XMLLanguage;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import org.intellij.plugins.xpathView.search.ScopePanel;
import org.intellij.plugins.xpathView.search.SearchScope;
import org.jetbrains.plugins.xml.searchandreplace.replace.ReplacementProvider;
import org.jetbrains.plugins.xml.searchandreplace.search.Pattern;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.replace.ReplaceController;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.replace.persistence.ReplacementsStorage;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.PatternController;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.persistence.PatternsStorage;
import org.jetbrains.plugins.xml.searchandreplace.ui.view.replace.ReplaceView;

import javax.swing.*;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;

public class MainDialog extends DialogWrapper implements ContainerListener, PatternController.Delegate {

  private static PatternController ourPatternController = null;
  private boolean badInput = false;

  @Override
  public void pleaseAutoresizeWindow(PatternController c) {
    getWindow().pack();
  }

  @Override
  public void badInput(PatternController patternController) {
    Messages.showErrorDialog("You have typed incorrect pattern", "Error");
    badInput = true;
  }

  public interface MainDialogDelegate {
    void performSearch(MainDialog d);
  }

  private void createUIComponents() {
    scopePanel = new ScopePanel(project);

    PatternsStorage service = PatternsStorage.getInstance(project);
    SearchScope searchScope = service == null ? new SearchScope() :
            (service.getRecentScope() == null ? new SearchScope() : service.getRecentScope());

    scopePanel.initComponent(module, searchScope);

    if (service != null) {
      patternController = service.getRecent();
    }
    if (patternController == null) {
      patternController = new PatternController(project);
      if (service != null) {
        service.setRecent(patternController);
      }
    }
    patternView = patternController.getView();

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
  }

  public void componentAdded(ContainerEvent e) {
    getWindow().pack();
  }

  public void componentRemoved(ContainerEvent e) {
    getWindow().pack();
  }

  private JPanel centerPanel;

  private ScopePanel scopePanel;

  private PatternController patternController;

  private JPanel patternView;
  private ReplaceController replaceController;

  private ReplaceView replaceView;
  private MainDialogDelegate delegate;

  private Project project;
  private Module module;
  private Pattern pattern;

  public MainDialog(Project project, Module module) {
    super(project);

    this.project = project;
    this.module = module;

    setTitle("Xml search and replace");
    setModal(false);
    setOKButtonText("Find");

    patternView.addContainerListener(this);    
    replaceView.getReplacementSpecificView().addContainerListener(new ContainerListener() {

      public void componentAdded(ContainerEvent e) {
        getWindow().pack();
      }

      public void componentRemoved(ContainerEvent e) {
      }
    });

    patternController.setDelegate(this);

    init();
  }

  @Override
  protected JComponent createCenterPanel() {
    return centerPanel;
  }

  public ReplacementProvider getReplacementProvider() {
    return replaceController.getReplacementProvider();
  }

  public SearchScope getSelectedScope() {
    return scopePanel.getSearchScope();
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
