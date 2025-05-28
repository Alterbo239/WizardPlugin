package wizard_app

import FormElement.MyFormDialog
import com.intellij.openapi.project.Project
import com.intellij.ui.wizard.WizardNavigationState
import com.intellij.ui.wizard.WizardStep

import javax.swing.JComponent

class ConnectionStep  extends  WizardStep{
    private  final  MyFormDialog formDialog;

    public  ConnectionStep(Project project){
        this.formDialog= new MyFormDialog();
    }

    @Override
    public  JComponent getComponent(){
        return  formDialog.createCenterPanel();
    }

    @Override
    public  String getTitle(){
        return  "Configuración de Conexión"
    }

    @Override
    JComponent prepare(WizardNavigationState wizardNavigationState) {
        return getComponent();
    }

    String getDatabaseType() {
        return formDialog.getSelectedDatabaseType()
    }

    String getHost() {
        return formDialog.getHost()
    }

    String getPort() {
        return formDialog.getPort()
    }

    String getDatabase() {
        return formDialog.getDatabase()
    }

    String getUser() {
        return formDialog.getUser()
    }

    String getPassword() {
        return formDialog.getPassword()
    }

}
