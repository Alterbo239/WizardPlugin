package com.plugin.wizard.steps;

import com.intellij.ide.wizard.AbstractWizardStepEx;
import com.intellij.ide.wizard.CommitStepException;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBList;
import com.intellij.util.ui.ColumnInfo;
import com.plugin.database.DatabaseConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.List;

public class Step2 extends AbstractWizardStepEx {
    private JPanel panel;
    private JList<String> tablesList;
    private DatabaseConfig config;

    public Step2(DatabaseConfig config) {
        super("Paso 2");

        this.config = config;

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
            // Ahora vamos a ello.
        } catch (SQLException e) {
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
