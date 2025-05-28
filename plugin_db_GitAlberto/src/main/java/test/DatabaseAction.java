package test;

//import Wizard.DatabaseWizard;
import wizard_app.DatabaseWizard;
import  wizard_app.DatabaseWizard;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectUtil;
import java.nio.charset.StandardCharsets;
import java.io.IOException;

        import java.sql.*;


public class DatabaseAction  extends AnAction {
    private String selectedTable;

    @Override
    public void actionPerformed(AnActionEvent e) {
        DatabaseWizard wizard = new DatabaseWizard(e.getProject());
        wizard.show();
        /*MyFormDialog dialog = new MyFormDialog();
        if (dialog.showAndGet()) {
            try {
                DatabaseConnection dbConnection = new DatabaseConnection(e.getProject());

                // Conectar a la base de datos
                if (dbConnection.connect(
                        dialog.getSelectedDatabaseType(),
                        dialog.getHost(),
                        dialog.getPort(),
                        dialog.getDatabase(),
                        dialog.getUser(),
                        dialog.getPassword()) != null) {

                    // Obtener la lista de tablas
                    List<String> tablasList = dbConnection.getTableList(
                            dialog.getDatabase(),
                            dialog.getSelectedDatabaseType());

                    if (!tablasList.isEmpty()) {
                        // Mostrar el diálogo de selección de tabla
                        //createTablesPanel(tablasList);

                        if (selectedTable != null) {
                            // Generar el DTO
                            Generator generator = new Generator();
                            generator.createDTOClass(selectedTable, dbConnection.getConnection(), e.getProject());
                        } else {
                            Messages.showErrorDialog(e.getProject(),
                                    "No se seleccionó ninguna tabla.",
                                    "Error de Selección");
                        }
                    } else {
                        Messages.showErrorDialog(e.getProject(),
                                "No se encontraron tablas en la base de datos.",
                                "Sin Tablas");
                    }
                }

                // Cerrar la conexión
                dbConnection.close();

            } catch (Exception ex) {
                Messages.showErrorDialog(e.getProject(),
                        "Error: " + ex.getMessage(),
                        "Error");
            }
        }*/
    }

    private String buildConnectionUrl(String dbType, String host, String port, String database) {
        switch (dbType) {
            case "MySQL":
            case "MariaDB":
                return "jdbc:mysql://" + host + ":" + port;
            case "PostgreSQL":
                return "jdbc:postgresql://" + host + ":" + port + "/" + database;
            case "Oracle":
                return "jdbc:oracle:thin:@" + host + ":" + port;
            case "Microsoft SQL Server":
                return "jdbc:sqlserver://" + host + ":" + port;
            case "MongoDB":
                return "mongodb://" + host + ":" + port;
            case "SQLite":
                return "jdbc:sqlite:" + host;  // Para SQLite, el host sería la ruta al archivo
            default:
                return "";
        }
    }

    private Connection connectToDatabase(String dbType, String url, String user, String password) throws Exception {
        switch (dbType) {
            case "MySQL", "MariaDB" -> Class.forName("com.mysql.cj.jdbc.Driver");
            case "PostgreSQL" -> Class.forName("org.postgresql.Driver");
            case "Microsoft SQL Server" -> Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            case "SQLite" -> Class.forName("org.sqlite.JDBC");
        }
        return DriverManager.getConnection(url, user, password);
    }

   /* private JComponent createTablesPanel(java.util.List<String> tablasList) {
        DialogWrapper tablesDialog = new DialogWrapper(true) {
            ComboBox<String> tablasCombo = new ComboBox<>(tablasList.toArray(new String[0]));
            {
                init();
                setTitle("Seleccionar Tabla");
            }
            @Override
            protected JComponent createCenterPanel() {
                JPanel panel = new JPanel(new GridBagLayout());
                panel.setBorder(JBUI.Borders.empty(10));
                GridBagConstraints gbc = new GridBagConstraints();

                // Configuración básica de GridBagConstraints
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.insets = JBUI.insets(5);  // Espacio entre componentes


                // Agregar la etiqueta
                gbc.gridx = 0;
                gbc.gridy = 0;
                gbc.weightx = 0.0;
                panel.add(new JBLabel("Tablas disponibles:"), gbc);

                // Agregar el ComboBox
                gbc.gridx = 1;
                gbc.gridy = 0;
                gbc.weightx = 1.0;
                panel.add(tablasCombo, gbc);

                // Configurar el tamaño del panel
                panel.setPreferredSize(JBUI.size(400, 100));
                return panel;
            }

            @Override
            protected void doOKAction() {
                selectedTable = (String) tablasCombo.getSelectedItem();
                super.doOKAction();
            }
        };
        tablesDialog.show();
        return tablesDialog.getContentPanel() ;
    }*/

    private void createJsonFile(String selectedTable, Connection conn, String database, Project project, String dbType) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            String query = "";
            if (dbType.equalsIgnoreCase("MySQL") || dbType.equalsIgnoreCase("MariaDB")) {
                query = "SELECT * FROM " + database + "." + selectedTable;
            } else if (dbType.equalsIgnoreCase("PostgreSQL")) {
                query = "SELECT * FROM \"" + selectedTable + "\"";
            }else {
                throw new SQLException("Tipo de base de datos no soportado: " + dbType);
            }
            ResultSet rs = stmt.executeQuery(query);
            ResultSetMetaData data = rs.getMetaData();
            int columnCount = data.getColumnCount();
            java.util.List<String> elementsList = new java.util.ArrayList<>();

            StringBuilder jsonContent = new StringBuilder("[\n");
            boolean firstRow = true;

            while (rs.next()) {
                if (!firstRow) {
                    jsonContent.append(",\n");
                }
                jsonContent.append("  {\n");

                for (int i = 1; i <= columnCount; i++) {
                    if (i > 1) {
                        jsonContent.append(",\n");
                    }
                    String columnName = data.getColumnName(i);
                    String value = rs.getString(i);
                    jsonContent.append("    \"").append(columnName).append("\": ");

                    if (value == null) {
                        jsonContent.append("null");
                    } else {
                        jsonContent.append("\"").append(value.replace("\"", "\\\"")).append("\"");
                    }
                }

                jsonContent.append("\n  }");
                firstRow = false;
            }

            jsonContent.append("\n]");

            // Crear el archivo usando VirtualFile
            WriteCommandAction.runWriteCommandAction(project, () -> {
                try {
                    VirtualFile baseDir = ProjectUtil.guessProjectDir(project);
                    if (baseDir != null) {
                        String fileName = selectedTable + ".json";
                        VirtualFile jsonFile = baseDir.createChildData(this, fileName);
                        jsonFile.setBinaryContent(jsonContent.toString().getBytes(StandardCharsets.UTF_8));

                        Messages.showInfoMessage(project,
                                "Archivo JSON creado exitosamente: " + fileName,
                                "Éxito");
                    }
                } catch (IOException e) {
                    Messages.showErrorDialog(project,
                            "Error al crear el archivo JSON: " + e.getMessage(),
                            "Error");
                }
            });
        }
    }


}

