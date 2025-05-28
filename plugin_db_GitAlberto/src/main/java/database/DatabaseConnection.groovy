package database;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet

public class DatabaseConnection {
    private Connection connection;
    private final Project project;

    public DatabaseConnection(Project project) {
        this.project = project;
    }

    public Connection connect(String dbType, String host, String port, String database, String user, String password) {
        try {
            loadDriver(dbType);
            String url = buildConnectionUrl(dbType, host, port, database);
            connection = DriverManager.getConnection(url, user, password);
            return connection;
        } catch (Exception e) {
            Messages.showErrorDialog(project,
                    "Error de conexión: " + e.getMessage(),
                    "Error de Conexión");
            return null;
        }
    }

    private void loadDriver(String dbType) throws ClassNotFoundException {
        switch (dbType) {
            case "MySQL", "MariaDB" -> Class.forName("com.mysql.cj.jdbc.Driver");
            case "PostgreSQL" -> Class.forName("org.postgresql.Driver");
            case "Microsoft SQL Server" -> Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            case "SQLite" -> Class.forName("org.sqlite.JDBC");
        }
    }

    private String buildConnectionUrl(String dbType, String host, String port, String database) {
        return switch (dbType) {
            case "PostgreSQL" -> String.format("jdbc:postgresql://%s:%s/%s", host, port, database);
            case "MySQL", "MariaDB" -> String.format("jdbc:mysql://%s:%s/%s", host, port, database);
            case "Oracle" -> String.format("jdbc:oracle:thin:@%s:%s:%s", host, port, database);
            case "Microsoft SQL Server" -> String.format("jdbc:sqlserver://%s:%s;databaseName=%s", host, port, database);
            case "SQLite" -> String.format("jdbc:sqlite:%s", database);
            default -> throw new IllegalStateException("Tipo de base de datos no soportado: " + dbType);
        };
    }

    public List<String> getTableList(String database, String dbType) {
        List<String> tableNames = new ArrayList<>();
        try {
            if (connection != null && !connection.isClosed()) {
                DatabaseMetaData metaData = connection.getMetaData();
                ResultSet tables = metaData.getTables(null, null, null, new String[]{"TABLE"});

                while (tables.next()) {
                    tableNames.add(tables.getString("TABLE_NAME"));
                }
            }
        } catch (Exception e) {
            Messages.showErrorDialog(project,
                    "Error al obtener las tablas: " + e.getMessage(),
                    "Error");
        }
        return tableNames;
    }

    public Connection getConnection() {
        return connection;
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (Exception e) {
            Messages.showErrorDialog(project,
                    "Error al cerrar la conexión: " + e.getMessage(),
                    "Error");
        }
    }
}

