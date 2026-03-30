import view.TelaLogin;

import javax.swing.*;

/**
 * Ponto de entrada do sistema ClinicaFácil.
 * Execute esta classe pelo NetBeans (botão Run Project).
 */
public class Main {

    public static void main(String[] args) {
        // Garante que a UI rode na Event Dispatch Thread (EDT) do Swing
        SwingUtilities.invokeLater(() -> {
            try {
                // Tenta aplicar a aparência nativa do sistema operacional
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}

            new TelaLogin().setVisible(true);
        });
    }
}
