package test;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;

public class Test extends AnAction {
    public static void main(String[] args) {
        System.out.println("Hola Mundo");
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        System.out.println("esta es la acción");
        Messages.showInfoMessage("Primera Acccion", "Info");
        Messages.getInformationIcon();
    }
}
