package wizard_app

import CodeGenerator.Generator
import com.intellij.ide.wizard.AbstractWizard
import com.intellij.ui.wizard.WizardStep;
import database.DatabaseConnection;
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages

public class DatabaseWizard extends AbstractWizard<WizardStep> {
    private final Project project;
    private final DatabaseConnection dbConnection;
    private final Generator generator;

    public DatabaseWizard(Project project) {
        super("Asistente de Base de Datos", project);
        this.project = project;
        this.dbConnection = new DatabaseConnection(project);
        this.generator = new Generator();

        addStep(new ConnectionStep(project));
        addStep(new TableSelectionStep(project));
        addStep(new GenerationConfigStep(project));
    }

    @Override
    protected void doOKAction() {
        if (isLastStep()) {
            try {
                String selectedTable = getStep(1).getSelectedTable()
                Map<String, Object> config = getStep(2).getGenerationConfig()
                generator.createDTOClass(selectedTable, dbConnection.getConnection(), project, config)
                super.doOKAction()
            } catch (Exception ex) {
                Messages.showErrorDialog(project,
                        "Error al generar DTO: " + ex.getMessage(),
                        "Error")
            } finally {
                dbConnection.close()
            }
        } else {
            super.doOKAction()
        }
    }


    @Override
    protected void doNextAction() {
        if (getCurrentStep() instanceof ConnectionStep) {
            if (!validateConnection()) {
                return;
            }
        }
        super.doNextAction();
    }

    @Override
    protected String getHelpID() {
        return null
    }

    private boolean validateConnection() {
        ConnectionStep step = (ConnectionStep) getStep(0);
        return dbConnection.connect(
                step.getDatabaseType(),
                step.getHost(),
                step.getPort(),
                step.getDatabase(),
                step.getUser(),
                step.getPassword()) != null;
    }
}
