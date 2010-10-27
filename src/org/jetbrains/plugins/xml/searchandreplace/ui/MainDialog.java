package org.jetbrains.plugins.xml.searchandreplace.ui;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.intellij.plugins.xpathView.search.ScopePanel;
import org.intellij.plugins.xpathView.search.SearchScope;

import javax.swing.*;

public class MainDialog extends DialogWrapper {

    private Project project;
    private Module module;
    private ScopePanel scopePanel;
    private PatternController patternController = new PatternController();

    private ScopePanel createScopePanel() {
        ScopePanel scopePanel = new ScopePanel(project);
        scopePanel.initComponent(module, new SearchScope());
        return scopePanel;
    }

    public MainDialog(Project project, Module module) {
        super(project);

        this.project = project;
        this.module = module;

        setTitle("Xml search and replace");
        setModal(false);
        setOKButtonText("Search & Replace");        

        scopePanel = createScopePanel();

        init();
    }

    @Override
    protected JComponent createCenterPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(scopePanel);
        panel.add(patternController.getView());
        return panel;
    }

    public SearchScope getSelectedScope() {
        return scopePanel.getSearchScope();
    }
}
