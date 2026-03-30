package util;

import javax.swing.JOptionPane;
import java.awt.Component;

/**
 * Helper centralizado para exibir diálogos JOptionPane padronizados.
 */
public class Mensagem {

    private Mensagem() {}

    public static void info(Component pai, String texto) {
        JOptionPane.showMessageDialog(pai, texto, "Informação", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void erro(Component pai, String texto) {
        JOptionPane.showMessageDialog(pai, texto, "Erro", JOptionPane.ERROR_MESSAGE);
    }

    public static void aviso(Component pai, String texto) {
        JOptionPane.showMessageDialog(pai, texto, "Atenção", JOptionPane.WARNING_MESSAGE);
    }

    public static boolean confirmar(Component pai, String texto) {
        int r = JOptionPane.showConfirmDialog(pai, texto, "Confirmar",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        return r == JOptionPane.YES_OPTION;
    }
}
