package view;

import Controller.LoginController;
import model.Usuario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TelaLogin extends JFrame {

    private final JTextField txtLogin    = new JTextField(20);
    private final JPasswordField txtSenha = new JPasswordField(20);
    private final JButton btnEntrar      = new JButton("Entrar");
    private final LoginController ctrl   = new LoginController();

    public TelaLogin() {
        super("ClinicaFácil — Login");
        construirUI();
    }

    private void construirUI() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        // Painel principal
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBackground(new Color(245, 248, 255));
        painel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Título
        JLabel lblTitulo = new JLabel("ClinicaFácil", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitulo.setForeground(new Color(33, 97, 140));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        painel.add(lblTitulo, gbc);

        JLabel lblSub = new JLabel("Sistema de Gestão de Clínica", SwingConstants.CENTER);
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSub.setForeground(Color.GRAY);
        gbc.gridy = 1;
        painel.add(lblSub, gbc);

        // Separador
        JSeparator sep = new JSeparator();
        gbc.gridy = 2; gbc.insets = new Insets(12, 0, 12, 0);
        painel.add(sep, gbc);
        gbc.insets = new Insets(6, 8, 6, 8);

        // Login
        gbc.gridwidth = 1; gbc.gridx = 0; gbc.gridy = 3;
        painel.add(new JLabel("Login:"), gbc);
        gbc.gridx = 1;
        painel.add(txtLogin, gbc);

        // Senha
        gbc.gridx = 0; gbc.gridy = 4;
        painel.add(new JLabel("Senha:"), gbc);
        gbc.gridx = 1;
        painel.add(txtSenha, gbc);

        // Botão
        btnEntrar.setBackground(new Color(33, 97, 140));
        btnEntrar.setForeground(Color.WHITE);
        btnEntrar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnEntrar.setFocusPainted(false);
        btnEntrar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        gbc.insets = new Insets(14, 8, 0, 8);
        painel.add(btnEntrar, gbc);

        add(painel);
        pack();
        setLocationRelativeTo(null);

        // Ações
        btnEntrar.addActionListener(e -> tentarLogin());
        txtSenha.addActionListener(e -> tentarLogin());
        txtLogin.addActionListener(e -> txtSenha.requestFocus());
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
