package org.jetbrains.plugins.xml.searchandreplace.ui;

import com.intellij.lang.xml.XMLLanguage;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.intellij.plugins.xpathView.search.ScopePanel;
import org.intellij.plugins.xpathView.search.SearchScope;
import org.jetbrains.plugins.xml.searchandreplace.replace.ReplacementProvider;
import org.jetbrains.plugins.xml.searchandreplace.search.Pattern;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.replace.ReplaceController;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.PatternController;
import org.jetbrains.plugins.xml.searchandreplace.ui.view.replace.ReplaceView;

import javax.swing.*;

public class MainDialog extends DialogWrapper {

  private void createUIComponents() {
    scopePanel = new ScopePanel(project);
    scopePanel.initComponent(module, new SearchScope());
    patternController = new PatternController();
    patternView = patternController.getView();

    replaceController = new ReplaceController(project, XMLLanguage.INSTANCE);
    replaceView = replaceController.getView();
  }

  public interface MainDialogDelegate {

    void performSearch(MainDialog d);

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
    if (getDelegate() != null) {
      getDelegate().performSearch(this);
    }
    super.doOKAction();
  }
}
