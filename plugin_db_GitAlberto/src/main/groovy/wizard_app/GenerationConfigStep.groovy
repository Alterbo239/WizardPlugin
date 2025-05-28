package wizard_app

import com.intellij.openapi.project.Project;
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.wizard.WizardNavigationState
import com.intellij.ui.wizard.WizardStep;
import com.intellij.util.ui.FormBuilder;
import com.intellij.util.ui.JBUI;

import javax.swing.*;
import java.awt.*;

public class GenerationConfigStep extends WizardStep {
    private final JBCheckBox generateGettersSetters;
    private final JBCheckBox generateConstructors;
    private final JBCheckBox generateToString;
    private final JTextField packageNameField;
    private final JComboBox<String> accessLevelCombo;

    public GenerationConfigStep(Project project) {
        setLayout(new BorderLayout());
        setBorder(JBUI.Borders.empty(10));

        generateGettersSetters = new JBCheckBox("Generar getters y setters", true);
        generateConstructors = new JBCheckBox("Generar constructores", true);
        generateToString = new JBCheckBox("Generar método toString", true);
        packageNameField = new JTextField("dto");

        String[] accessLevels = {"public" "private" "protected" "package-private"};
        accessLevelCombo = new JComboBox<>(accessLevels);

        JPanel panel = FormBuilder.createFormBuilder()
                .addLabeledComponent("Paquete:", packageNameField)
                .addLabeledComponent("Nivel de acceso:", accessLevelCombo)
                .addComponent(generateGettersSetters)
                .addComponent(generateConstructors)
                .addComponent(generateToString)
                .getPanel();

        add(panel, BorderLayout.CENTER);
    }

    @Override
    public String getTitle() {
        return "Configuración de Generación";
    }

    @Override
    JComponent prepare(WizardNavigationState wizardNavigationState) {
        return null
    }

    @Override
    public boolean isComplete() {
        return true; // La configuración siempre es válida con valores por defecto
    }

    public Map<String, Object> getGenerationConfig() {
        Map<String, Object> config = new HashMap<>();
        config.put("packageName", packageNameField.getText());
        config.put("accessLevel", accessLevelCombo.getSelectedItem());
        config.put("generateGettersSetters", generateGettersSetters.isSelected());
        config.put("generateConstructors", generateConstructors.isSelected());
        config.put("generateToString", generateToString.isSelected());
        return config;
    }
}
