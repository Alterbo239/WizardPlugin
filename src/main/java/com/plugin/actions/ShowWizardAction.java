package com.plugin.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.plugin.wizard.DatabaseWizard;
import org.jetbrains.annotations.NotNull;

public class ShowWizardAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project p = e.getProject();
        if (p != null) {
            DatabaseWizard wizard = new DatabaseWizard(p);
            if (wizard.showAndGet()) {

            }
        }
    }
}
