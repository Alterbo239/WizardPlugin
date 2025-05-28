package wizard_app

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.ComboBox
import com.intellij.ui.components.JBLabel
import com.intellij.ui.wizard.WizardNavigationState
import com.intellij.ui.wizard.WizardStep
import com.intellij.util.ui.JBUI

import javax.swing.JComponent
import javax.swing.JPanel
import java.awt.GridBagConstraints
import java.awt.GridBagLayout

class TableSelectionStep extends  WizardStep {

    private ComboBox<String> tablasCombo;

    TableSelectionStep(Project project){
        tablasCombo = new ComboBox<>();
    }

    @Override
    public JComponent getComponent() {
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
    public String getTitle() {
        return "Selección de Tabla";
    }

    @Override
    JComponent prepare(WizardNavigationState wizardNavigationState) {
        return null
    }

    public String getSelectedTable() {
        return (String) tablasCombo.getSelectedItem();
    }
}