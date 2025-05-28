package FormElement;

import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.components.JBTextField;
import com.intellij.ui.components.JBLabel;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.util.ui.JBUI;

import javax.swing.*;
import java.awt.*;

public class MyFormDialog extends DialogWrapper {
    private JBTextField hostField;
    private JBTextField portField;
    private JBTextField userField;
    private JBTextField databaseField;
    private JBTextField passwordField;
    private ComboBox<String> databaseTypeCombo;
    public ComboBox<String> tablasCombo;

    public MyFormDialog() {
        super(true);
        init();
        setTitle("Configuración de Base de Datos");
    }

    @Override
    public JComponent createCenterPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(JBUI.Borders.empty(10));
        GridBagConstraints gbc = new GridBagConstraints();

        // Configuración básica de GridBagConstraints
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = JBUI.insets(5);  // Espacio entre componentes

        // Inicializar componentes
        databaseTypeCombo = new ComboBox<>(new String[]{
                "MySQL",
                "PostgreSQL",
                "Oracle",
                "Microsoft SQL Server",
                "SQLite",
                "MongoDB",
                "MariaDB"
        });
        hostField = new JBTextField();
        portField = new JBTextField();
        databaseField = new JBTextField();
        userField = new JBTextField();
        passwordField = new JBTextField();


        // Agregar componentes
        // Las etiquetas van en la columna 0
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.0;
        panel.add(new JBLabel("Tipo de Base de Datos:"), gbc);

        gbc.gridy = 1;
        panel.add(new JBLabel("Host:"), gbc);

        gbc.gridy = 2;
        panel.add(new JBLabel("Puerto:"), gbc);

        gbc.gridy = 3;
        panel.add(new JBLabel("Base de Datos:"), gbc);

        gbc.gridy = 4;
        panel.add(new JBLabel("Usuario:"), gbc);

        gbc.gridy = 5;
        panel.add(new JBLabel("Contraseña:"), gbc);

        // Los campos van en la columna 1
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;  // Hace que los componentes se estiren horizontalmente
        panel.add(databaseTypeCombo, gbc);

        gbc.gridy = 1;
        panel.add(hostField, gbc);

        gbc.gridy = 2;
        panel.add(portField, gbc);

        gbc.gridy = 3;
        panel.add(databaseField, gbc);

        gbc.gridy = 4;
        panel.add(userField, gbc);

        gbc.gridy = 5;
        panel.add(passwordField, gbc);



        // Configurar el tamaño preferido
        panel.setPreferredSize(JBUI.size(400, 200));

        // Listener para el combo box
        databaseTypeCombo.addActionListener(e -> {
            String selectedDB = (String) databaseTypeCombo.getSelectedItem();
            switch (selectedDB) {
                case "MySQL", "MariaDB" -> portField.setText("3306");
                case "PostgreSQL" -> portField.setText("5432");
                case "Oracle" -> portField.setText("1521");
                case "Microsoft SQL Server" -> portField.setText("1433");
                case "MongoDB" -> portField.setText("27017");
                case "SQLite" -> {
                    portField.setText("");
                    portField.setEnabled(false);
                }
                default -> {
                    portField.setText("");
                    portField.setEnabled(true);
                }
            }
        });

        return panel;
    }

    // Getters
    public String getSelectedDatabaseType() {

        return (String) databaseTypeCombo.getSelectedItem();
    }

    public String getHost() {

        return hostField.getText();
    }

    public String getPort() {
        return portField.getText();
    }

    public  String getDatabase() {
        return databaseField.getText();
    }

    public String getUser() {
        return userField.getText();
    }

    public String getPassword() {
        return passwordField.getText();
    }

    public ComboBox<String> getTablasCombo() {
        return (ComboBox<String>) tablasCombo.getSelectedItem();
    }

    // Setters
    public void setTableList(String[] tablasArray) {
        tablasCombo.removeAllItems();
        for (String tabla : tablasArray) {
            tablasCombo.addItem(tabla);
        }
    }
}