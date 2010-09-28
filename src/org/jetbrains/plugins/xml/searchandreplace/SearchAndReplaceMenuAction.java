package org.jetbrains.plugins.xml.searchandreplace;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;

/**
 * Created by IntelliJ IDEA.
 * User: zajac
 * Date: 9/29/10
 * Time: 3:14 AM
 * To change this template use File | Settings | File Templates.
 */
public class SearchAndReplaceMenuAction extends AnAction {

    static {
        System.out.println("static initializer");
    }

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        Messages.showMessageDialog("Hello, world!!!", "dummy plugin", null);
    }

    @Override
    public void update(AnActionEvent e) {
        super.update(e);
        Project project = PlatformDataKeys.PROJECT.getData(e.getDataContext());
        e.getPresentation().setEnabled(project != null);

    }
}
