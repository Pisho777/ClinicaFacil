package view;

import Controller.LoginController;
import model.Usuario;

import javax.swing.*;
import java.awt.*;

/**
 * Tela de Login — layout split:
 *  lado esquerdo: gradiente brand com identidade visual
 *  lado direito : formulário de acesso
 */
public class TelaLogin extends JFrame {

    private final JTextField    txtLogin = DS.campo(20);
    private final JPasswordField txtSenha = DS.campoSenha(20);
    private final JButton       btnEntrar = DS.btnBrand("Entrar no sistema");
    private final LoginController ctrl    = new LoginController();

    public TelaLogin() {
        super("ClinicaFácil");
        construirUI();
    }

    private void construirUI() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        JPanel root = new JPanel(new GridLayout(1, 2));
        root.setPreferredSize(new Dimension(820, 500));

        root.add(painelEsquerdo());
        root.add(painelDireito());

        add(root);
        pack();
        setLocationRelativeTo(null);

        btnEntrar.addActionListener(e -> tentarLogin());
        txtSenha.addActionListener(e -> tentarLogin());
        txtLogin.addActionListener(e -> txtSenha.requestFocus());
    }

    // ── Lado esquerdo: gradiente brand ──────────────────────────
    private JPanel painelEsquerdo() {
        // Painel com paintComponent sobrescrito para gradiente
        JPanel p = new JPanel(new GridBagLayout()) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setPaint(new GradientPaint(0, 0, DS.BRAND, getWidth(), getHeight(), DS.BRAND_MID));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        p.setOpaque(false);
        p.setBorder(DS.emptyBorder(48, 48, 48, 48));

        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 0; g.fill = GridBagConstraints.HORIZONTAL;
        g.anchor = GridBagConstraints.NORTHWEST;
        g.insets = new Insets(0, 0, 0, 0);

        // Logo mark
        JLabel logo = new JLabel("CF");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        logo.setForeground(DS.WHITE);
        logo.setOpaque(true);
        logo.setBackground(new Color(255, 255, 255, 38));
        logo.setBorder(DS.emptyBorder(8, 12, 8, 12));
        g.gridy = 0; g.insets = new Insets(0, 0, 20, 0);
        p.add(logo, g);

        // Título
        JLabel titulo = new JLabel("ClinicaFácil");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titulo.setForeground(DS.WHITE);
        g.gridy = 1; g.insets = new Insets(0, 0, 6, 0);
        p.add(titulo, g);

        // Subtítulo
        JLabel sub = new JLabel("<html><p style='width:220px;color:rgba(255,255,255,0.75);"
                + "font-size:12px;line-height:150%'>"
                + "Sistema de gestão para clínicas e consultórios médicos de pequeno e médio porte."
                + "</p></html>");
        g.gridy = 2; g.insets = new Insets(0, 0, 28, 0);
        p.add(sub, g);

        // Features
        String[] feats = {"Agendamento inteligente", "Prontuário digital",
                          "Controle de acesso por perfil", "Relatórios de atendimento"};
        g.insets = new Insets(0, 0, 8, 0);
        for (String f : feats) {
            JLabel feat = new JLabel("●  " + f);
            feat.setFont(DS.F_SMALL);
            feat.setForeground(new Color(255, 255, 255, 217));
            g.gridy++;
            p.add(feat, g);
        }

        return p;
    }

    // ── Lado direito: formulário ─────────────────────────────────
    private JPanel painelDireito() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(DS.WHITE);
        p.setBorder(DS.emptyBorder(40, 36, 40, 36));

        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 0; g.fill = GridBagConstraints.HORIZONTAL;
        g.anchor = GridBagConstraints.NORTHWEST;
        g.weightx = 1;

        // Cabeçalho do form
        JLabel h2 = new JLabel("Bem-vindo de volta");
        h2.setFont(new Font("Segoe UI", Font.BOLD, 20));
        h2.setForeground(DS.TEXT);
        g.gridy = 0; g.insets = new Insets(0, 0, 4, 0);
        p.add(h2, g);

        JLabel lsub = DS.labelMuted("Acesse com suas credenciais de usuário");
        g.gridy = 1; g.insets = new Insets(0, 0, 24, 0);
        p.add(lsub, g);

        // Campo Login
        g.gridy = 2; g.insets = new Insets(0, 0, 4, 0);
        p.add(DS.labelCampo("Login"), g);
        txtLogin.setToolTipText("Login de acesso");
        g.gridy = 3; g.insets = new Insets(0, 0, 14, 0);
        p.add(txtLogin, g);

        // Campo Senha
        g.gridy = 4; g.insets = new Insets(0, 0, 4, 0);
        p.add(DS.labelCampo("Senha"), g);
        g.gridy = 5; g.insets = new Insets(0, 0, 20, 0);
        p.add(txtSenha, g);

        // Botão
        btnEntrar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnEntrar.setBorder(DS.emptyBorder(11, 0, 11, 0));
        g.gridy = 6; g.insets = new Insets(0, 0, 16, 0);
        p.add(btnEntrar, g);

        // Dica de credenciais
        JPanel hint = new JPanel(new BorderLayout());
        hint.setBackground(DS.BRAND_LIGHT);
        hint.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 3, 0, 0, DS.BRAND_MID),
            DS.emptyBorder(8, 12, 8, 12)));
        JLabel hintTxt = new JLabel("<html><b>Perfis disponíveis:</b> admin / recepcao / medico<br>"
                + "Senha padrão: <b>123</b></html>");
        hintTxt.setFont(DS.F_SMALL);
        hintTxt.setForeground(DS.BRAND);
        hint.add(hintTxt);
        g.gridy = 7; g.insets = new Insets(0, 0, 0, 0);
        p.add(hint, g);

        return p;
    }

    private void tentarLogin() {
        String login = txtLogin.getText().trim();
        String senha = new String(txtSenha.getPassword());
        Usuario u = ctrl.autenticar(login, senha, this);
        if (u != null) {
            dispose();
            new TelaPrincipal(u).setVisible(true);
        } else {
            txtSenha.setText("");
            txtSenha.requestFocus();
        }
    }
}
