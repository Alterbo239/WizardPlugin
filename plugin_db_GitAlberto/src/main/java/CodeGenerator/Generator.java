package CodeGenerator;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectUtil;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import freemarker.cache.FileTemplateLoader;
import net.bytebuddy.dynamic.DynamicType;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;


import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.nio.charset.StandardCharsets;
import java.util.*;


public class Generator {

    private Configuration configuration;

    public  Generator() throws IOException {
        configuration= new Configuration(Configuration.VERSION_2_3_28);
        try{
            // Usar el ClassLoader para cargar los recursos
            configuration.setClassLoaderForTemplateLoading(getClass().getClassLoader(), "templates");
            configuration.setDefaultEncoding("UTF-8");

            // Para depuración - imprime la ruta de búsqueda
            System.out.println("Buscando plantilla en: " +
                    getClass().getClassLoader().getResource("templates"));


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createDTOClass(String selectedTable, Connection conn, Project project, Map<String, Object> config) throws SQLException, IOException, TemplateException {
        DatabaseMetaData data = conn.getMetaData();
        ResultSet columns = data.getColumns(null, null, selectedTable, null);

        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("className", mayuscula(selectedTable));
        dataModel.put("selectedTable", mayuscula(selectedTable));
        dataModel.put("packageName", config.get("packageName"));
        dataModel.put("accessLevel", config.get("accessLevel"));
        dataModel.put("generateGettersSetters", config.get("generateGettersSetters"));
        dataModel.put("generateConstructors", config.get("generateConstructors"));
        dataModel.put("generateToString", config.get("generateToString"));

        List<Map<String, String>> columnList = new ArrayList<>();
        while (columns.next()) {
            Map<String, String> column = new HashMap<>();
            column.put("name", columns.getString("COLUMN_NAME"));
            column.put("type", mapearClaseJava(columns.getString("TYPE_NAME")));
            columnList.add(column);
        }
        dataModel.put("columns", columnList);

        Template plantilla = configuration.getTemplate("plantilla.ftl");

        WriteCommandAction.runWriteCommandAction(project, () -> {
            try {
                VirtualFile baseDir = ProjectUtil.guessProjectDir(project);
                if (baseDir != null) {
                    // Buscar o crear el directorio src si no existe
                    VirtualFile srcDir = baseDir.findChild("src");
                    if (srcDir == null) {
                        srcDir = baseDir.createChildData(this, "src");
                    }
                    VirtualFile dtoDir = srcDir.findChild("dto");
                    if (dtoDir == null) {
                        dtoDir = srcDir.createChildDirectory(this, "dto");
                    }
                    String fileName = mayuscula(selectedTable) + "DTO.java";
                    VirtualFile file = dtoDir.createChildData(this, fileName);

                    StringWriter writer = new StringWriter();
                    plantilla.process(dataModel, writer);
                    file.setBinaryContent(writer.toString().getBytes(StandardCharsets.UTF_8));

                    Messages.showInfoMessage(project,
                            "Archivo DTO generado exitosamente en: src/dto/" + fileName,
                            "Éxito");

                }
            } catch (IOException | TemplateException e) {
                Messages.showErrorDialog(project,
                        "Error al crear el archivo DTO: " + e.getMessage(),
                        "Error");
            }
        });
    }

    public  void createDTOClass(String selectedTable, Connection conn, Project project) throws SQLException, IOException, TemplateException {
        DatabaseMetaData data = conn.getMetaData();
        ResultSet columns = data.getColumns(null, null, selectedTable, null);

        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("className", mayuscula(selectedTable));
        dataModel.put("selectedTable", mayuscula(selectedTable));

        List<Map<String, String>> columnList = new ArrayList<>();
        while (columns.next()) {
            Map<String, String> column = new HashMap<>();
            column.put("name", columns.getString("COLUMN_NAME"));
            column.put("type", mapearClaseJava(columns.getString("TYPE_NAME")));
            columnList.add(column);
        }
        dataModel.put("columns", columnList);

        Template plantilla = configuration.getTemplate("plantilla.ftl");

        WriteCommandAction.runWriteCommandAction(project, () -> {
            try {
                VirtualFile baseDir = ProjectUtil.guessProjectDir(project);
                if (baseDir != null) {
                    // Buscar o crear el directorio src si no existe
                    VirtualFile srcDir = baseDir.findChild("src");
                    if (srcDir == null) {
                        srcDir = baseDir.createChildData(this, "src");
                    }
                    VirtualFile dtoDir = srcDir.findChild("dto");
                    if (dtoDir == null) {
                        dtoDir = srcDir.createChildDirectory(this, "dto");
                    }
                    String fileName = mayuscula(selectedTable) + "DTO.java";
                    VirtualFile file = dtoDir.createChildData(this, fileName);

                    StringWriter writer = new StringWriter();
                    plantilla.process(dataModel, writer);
                    file.setBinaryContent(writer.toString().getBytes(StandardCharsets.UTF_8));

                    Messages.showInfoMessage(project,
                            "Archivo DTO generado exitosamente en: src/dto/" + fileName,
                            "Éxito");

                }
            } catch (IOException | TemplateException e) {
                Messages.showErrorDialog(project,
                        "Error al crear el archivo DTO: " + e.getMessage(),
                        "Error");
            }
        });
    }

    // Mapear el tipo de dato de la base de datos a un tipo de Java
    public String mapearClaseJava(String fieldType){
        String realType = fieldType.toUpperCase().replaceAll( "\\d" , "");

        switch(realType){
            case "VARCHAR", "CHAR" , "TEXT":
                return "String";
            case "INT" , "INTEGER" , "SMALLINT", "TINYINT":
                return "int";
            case "DOUBLE", "FLOAT", "REAL", "DECIMAL":
                return "double";
            case "BIGINT":
                return "long";
            case "BOOLEAN":
                return "boolean";
            /*case "TIMESTAMP", "DATETIME" , "DATE" , "TIME":
                return Date.class;
            case "ENUM":
                return Enum.class;*/
            default:
                return "Object";
        }
    }
    public  String mayuscula(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}

/*public void createDTOClass(String selectedTable, Connection conn, Project project) throws SQLException, IOException {
        StringBuilder contenidoClase = new StringBuilder();
        DatabaseMetaData data = conn.getMetaData();
        ResultSet columns = data.getColumns(null, null, selectedTable, null);

        contenidoClase.append("public class ").append(mayuscula(selectedTable)).append("DTO {\n");

        while (columns.next()) {
            String columnName = columns.getString("COLUMN_NAME");
            String typeName = columns.getString("TYPE_NAME");
            String fieldType = mapearClaseJava(typeName);

            contenidoClase.append("     private ").append(fieldType).append(" ").append(columnName).append(";\n");
        }
        contenidoClase.append("\n   public  ").append(mayuscula(selectedTable)).append("DTO() {}\n");

        StringBuilder constructorParams = new StringBuilder();
        StringBuilder constructorBody = new StringBuilder();
        columns.beforeFirst();

        while (columns.next()) {
            String columnName = columns.getString("COLUMN_NAME");
            String typeName = columns.getString("TYPE_NAME");
            String fieldType = mapearClaseJava(typeName);

            if (!constructorParams.isEmpty()) {
                constructorParams.append(", ");
            }
            constructorParams.append(fieldType).append(" ").append(columnName);

            constructorBody.append("    this.").append(columnName).append(" = ").append(columnName).append(";\n");
        }

        contenidoClase.append("    public ").append(mayuscula(selectedTable)).append("DTO(")
                .append(constructorParams).append(") {\n")
                .append(constructorBody)
                .append("    }\n");

        columns.beforeFirst();

        while (columns.next()) {
            String columnName = columns.getString("COLUMN_NAME");
            String typeName = columns.getString("TYPE_NAME");
            String fieldType = mapearClaseJava(typeName);

            contenidoClase.append("    public ").append(fieldType).append(" get")
                    .append(mayuscula(columnName)).append("() {\n")
                    .append("        return ").append(columnName).append(";\n")
                    .append("    }\n");

            // Agregar setter
            contenidoClase.append("    public void set").append(mayuscula(columnName)).append("(")
                    .append(fieldType).append(" ").append(columnName).append(") {\n")
                    .append("        this.").append(columnName).append(" = ")
                    .append(columnName).append(";\n")
                    .append("    }\n");

        }
        columns.beforeFirst();

        //Metodo toString
        contenidoClase.append("    @Override\n");
        contenidoClase.append("    public String toString() {\n").append("        return \"");

        boolean isFirst = true;
        while (columns.next()) {
            String columnName = columns.getString("COLUMN_NAME");

            if (!isFirst) {
                contenidoClase.append(" +\n            \"");
            } else {
                isFirst = false;
            }

            contenidoClase.append(columnName).append("=\"+ get")
                    .append(mayuscula(columnName)).append("()");
        }
        contenidoClase.append(";\n    }\n");

        contenidoClase.append("}");


        WriteCommandAction.runWriteCommandAction(project, () -> {
            try {
                VirtualFile baseDir = ProjectUtil.guessProjectDir(project);
                if (baseDir != null) {
                    // Buscar o crear el directorio src si no existe
                    VirtualFile srcDir = baseDir.findChild("src");
                    if (srcDir == null) {
                        srcDir = baseDir.createChildDirectory(this, "src");
                    }

                    // Buscar o crear el directorio dto dentro de src
                    VirtualFile dtoDir = srcDir.findChild("dto");
                    if (dtoDir == null) {
                        dtoDir = srcDir.createChildDirectory(this, "dto");
                    }

                    // Crear el archivo .java en el directorio dto
                    String fileName = mayuscula(selectedTable) + "DTO.java";
                    VirtualFile file = dtoDir.createChildData(this, fileName);
                    file.setBinaryContent(contenidoClase.toString().getBytes(StandardCharsets.UTF_8));

                    Messages.showInfoMessage(project,
                            "Archivo DTO generado exitosamente en: src/dto/" + fileName,
                            "Éxito");
                }
            } catch (IOException e) {
                Messages.showErrorDialog(project,
                        "Error al crear el archivo DTO: " + e.getMessage(),
                        "Error");
            }
        });
    }*/
