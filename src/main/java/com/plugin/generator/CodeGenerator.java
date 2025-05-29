package com.plugin.generator;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectUtil;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;


import java.awt.*;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.List;


public class CodeGenerator {

    private Configuration configuration;

    public CodeGenerator() throws IOException {
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
                    VirtualFile srcDir = baseDir.findFileByRelativePath("src/main/java/com/plugin");
                    if (srcDir == null || !srcDir.isDirectory()) {
                        Messages.showErrorDialog((Component) project, "Esa ruta no existe.");
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
            case "TIMESTAMP", "DATETIME" , "DATE" , "TIME":
                return "Date";
            case "ENUM":
                return "Enum";
            default:
                return "Object";
        }
    }
    public  String mayuscula(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}