package com.plugin.wizard.steps;

import com.intellij.ide.wizard.AbstractWizardStepEx;
import com.intellij.ide.wizard.CommitStepException;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBPasswordField;
import com.intellij.ui.components.JBTextField;
import com.plugin.database.DatabaseConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.naming.ConfigurationException;
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Step1 extends AbstractWizardStepEx {
    private final JPanel panel;
    private final ComboBox dbTypeCombo;
    private final JBTextField dbHostField;
    private final JBTextField dbPortField;
    private final JBTextField dbNameField;
    private final JBTextField dbUserField;
    private final JBPasswordField dbPasswordField;

    private final DatabaseConfig config;

    public Step1(DatabaseConfig config) {
        super("Paso 1");

        panel = new JPanel(new BorderLayout());
        panel.add(new JBLabel("Introduce los datos:"), BorderLayout.NORTH);

        dbTypeCombo = new ComboBox<>(new String[] { "MySQL", "PostgreSQL" });
        dbHostField = new JBTextField();
        dbPortField = new JBTextField();
        dbNameField = new JBTextField();
        dbUserField = new JBTextField();
        dbPasswordField = new JBPasswordField();

        this.config = config;

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));

        formPanel.add(new JBLabel("Tipo de base de datos:"));
        formPanel.add(dbTypeCombo);
        formPanel.add(new JBLabel("Host:"));
        formPanel.add(dbHostField);
        formPanel.add(new JBLabel("Puerto:"));
        formPanel.add(dbPortField);
        formPanel.add(new JBLabel("Nombre de base de datos:"));
        formPanel.add(dbNameField);
        formPanel.add(new JBLabel("Usuario"));
        formPanel.add(dbUserField);
        formPanel.add(new JBLabel("Password"));
        formPanel.add(dbPasswordField);

        panel.add(formPanel, BorderLayout.CENTER);
    }

    @Override
    public JComponent getComponent() {
        return panel;
    }

    public boolean validate() throws ConfigurationException {
        if (dbHostField.getText().trim().isEmpty()) {
            throw new ConfigurationException("Host no puede estar vacio");
        }
        return true;
    }

    @Override
    public void commit(CommitType commitType) throws CommitStepException {
        config.setType((String) dbTypeCombo.getSelectedItem());
        config.setHost(dbHostField.getText());
        try {
            config.setPort(Integer.parseInt(dbPortField.getText().trim()));
        } catch (NumberFormatException e) {
            throw new CommitStepException("Puerto invalido");
        }
        config.setDbName(dbNameField.getText());
        config.setUser(dbUserField.getText());
        config.setPassword(new String(dbPasswordField.getPassword()));

        Connection con = null;

        try {
            // Carga driver
            switch (config.getType()) {
                case "MySQL":
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    break;
                case "PostgreSQL":
                    Class.forName("org.postgresql.Driver");
                    break;
                default:
                    throw new CommitStepException("Tipo de base de datos no soportado");
            }

            con = getConnection(config);

            List<String> tables = fetchTables(con);
            config.setTables(tables);
        } catch (SQLException e) {
            throw new CommitStepException(e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {}
            }
        }
    }

    public static Connection getConnection(DatabaseConfig config) throws SQLException {
        String url;

        switch (config.getType()) {
            case "MySQL":
                url = "jdbc:mysql://" + config.getHost() + ":" + config.getPort() + "/" + config.getDbName();
                break;
            case "PostgreSQL":
                url = "jdbc:postgresql://" + config.getHost() + ":" + config.getPort() + "/" + config.getDbName();
                break;
            default:
                throw new SQLException("Tipo de base de datos no soportado");
        }
        return DriverManager.getConnection(url, config.getUser(), config.getPassword());
    }

    public static List<String> fetchTables(Connection con) throws SQLException {
        List<String> tables = new ArrayList<>();
        DatabaseMetaData meta = con.getMetaData();

        try (ResultSet rs = meta.getTables(null, null, null, new String[] { "TABLE" } )) {
            while (rs.next()) {
                String tableName = rs.getString("TABLE_NAME");

                if (!tableName.toLowerCase().startsWith("pma_") &&
                    !tableName.toLowerCase().startsWith("mysql") &&
                    !tableName.toLowerCase().startsWith("sys") &&
                    !tableName.toLowerCase().startsWith("information_schema") &&
                    !tableName.toLowerCase().startsWith("performance_schema")) {
                    tables.add(tableName);
                }

            }
        }

        return tables;
    }

    @Override
    public @NotNull Object getStepId() {
        return this;
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
