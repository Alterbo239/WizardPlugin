package com.plugin.wizard;

import com.intellij.ide.wizard.AbstractWizard;
import com.intellij.ide.wizard.Step;
import com.intellij.openapi.project.Project;
import com.plugin.database.DatabaseConfig;
import com.plugin.wizard.steps.Step1;
import com.plugin.wizard.steps.Step2;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DatabaseWizard extends AbstractWizard<Step> {
    private final List<Step> steps;
    private final DatabaseConfig config = new DatabaseConfig();

    public DatabaseWizard(Project p) {
        super("Nuevo constructor", p);
        steps = List.of(new Step1(config), new Step2(config, p));

        for (Step step : steps) {
            addStep(step);
        }

        init();
    }

    @Override
    protected void doOKAction() {
        super.doOKAction();
    }

    protected Step getNextStep(Step step) {
        int index = steps.indexOf(step);
        return index < steps.size() - 1 ? steps.get(index + 1) : null;
    }

    protected Step getPreviousStep(Step step) {
        int index = steps.indexOf(step);
        return index > 0 ? steps.get(index - 1) : null;
    }

    @Override
    protected @Nullable @NonNls String getHelpID() {
        return "No se";
    }
}
