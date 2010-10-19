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

    public MainDialog(Project project, Module module) {
        super(project);

        this.project = project;
        this.module = module;

        setTitle("Xml search and replace");
        setModal(false);
        setOKButtonText("Close");
        init();
    }

    @Override
    protected JComponent createCenterPanel() {
        JPanel panel = new JPanel();
        scopePanel = new ScopePanel(project);
        scopePanel.initComponent(module, new SearchScope());
        panel.add(scopePanel);
        return panel;
    }
}
