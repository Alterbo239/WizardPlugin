package com.plugin.wizard.steps;

import com.intellij.ide.wizard.AbstractWizardStepEx;
import com.intellij.ide.wizard.CommitStepException;
import com.intellij.openapi.project.Project;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBList;
import com.intellij.util.ui.ColumnInfo;
import com.plugin.database.DatabaseConfig;
import com.plugin.generator.CodeGenerator;
import freemarker.template.TemplateException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Step2 extends AbstractWizardStepEx {
    private final JPanel panel;
    private final JList<String> tablesList;
    private final DatabaseConfig config;
    private final Project p;
    private final Map<String, Object> map = new HashMap<>();

    public Step2(DatabaseConfig config, Project p) {
        super("Paso 2");

        this.config = config;
        this.p = p;

        panel = new JPanel(new BorderLayout());
        panel.add(new JBLabel("Selecciona una tabla: "), BorderLayout.NORTH);

        tablesList = new JBList<>();
        panel.add(new JScrollPane(tablesList), BorderLayout.CENTER);
    }

    @Override
    public JComponent getComponent() {
        List<String> tablas = config.getTables();

        if (tablas != null) {
            tablesList.setListData(tablas.toArray(new String[0]));
        }

        return panel;
    }

    @Override
    public void commit(CommitType commitType) throws CommitStepException {
        String selectedTable = (String) tablesList.getSelectedValue();

        if (selectedTable == null || selectedTable.isEmpty()) {
            throw new CommitStepException("Debes seleccionar una tabla");
        }

        try (Connection con = Step1.getConnection(config)) {
            CodeGenerator cg = new CodeGenerator();
            cg.createDTOClass(selectedTable, con, p, map);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (TemplateException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public @NotNull Object getStepId() {
        return null;
    }

    @Override
    public @Nullable Object getNextStepId() {
        return null;
    }

    @Override
    public @Nullable Object getPreviousStepId() {
        return null;
    }

    @Override
    public boolean isComplete() {
        return false;
    }

    @Override
    public @Nullable JComponent getPreferredFocusedComponent() {
        return null;
    }
}
