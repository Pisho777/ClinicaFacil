package view;

import Controller.LoginController;
import model.PerfilUsuario;
import model.Usuario;

import javax.swing.*;
import java.awt.*;

public class TelaPrincipal extends JFrame {

    private final Usuario usuarioLogado;

    public TelaPrincipal(Usuario usuario) {
        super("ClinicaFácil — Painel Principal");
        this.usuarioLogado = usuario;
        construirUI();
    }

    private void construirUI() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(780, 520);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- Cabeçalho ---
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(33, 97, 140));
        header.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));

        JLabel lblSistema = new JLabel("ClinicaFácil");
        lblSistema.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblSistema.setForeground(Color.BLACK);
        header.add(lblSistema, BorderLayout.WEST);

        JLabel lblUsuario = new JLabel("Olá, " + usuarioLogado.getNome()
                + "  |  " + usuarioLogado.getPerfil().getDescricao());
        lblUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblUsuario.setForeground(new Color(200, 220, 255));
        header.add(lblUsuario, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // --- Painel de botões ---
        JPanel centro = new JPanel(new GridLayout(2, 3, 20, 20));
        centro.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        centro.setBackground(new Color(245, 248, 255));

        centro.add(criarBotaoMenu("Pacientes", "Cadastro e pesquisa", () -> abrirPacientes()));
        centro.add(criarBotaoMenu("Médicos",   "Cadastro e gestão",   () -> abrirMedicos()));
        centro.add(criarBotaoMenu("Agenda",    "Agendamento de consultas", () -> abrirAgenda()));

        // Oculta opções restritas para recepcionistas
        JButton btnProntuarios = criarBotaoMenu("Prontuários", "Registro clínico", () -> abrirAgenda());
        if (usuarioLogado.getPerfil() == PerfilUsuario.RECEPCAO) {
            btnProntuarios.setEnabled(false);
            btnProntuarios.setToolTipText("Acesso restrito a médicos e administradores");
        }
        centro.add(btnProntuarios);

        JButton btnAdmin = criarBotaoMenu("Administração", "Usuários e config.", () -> {});
        if (usuarioLogado.getPerfil() != PerfilUsuario.ADMIN) {
            btnAdmin.setEnabled(false);
            btnAdmin.setToolTipText("Acesso exclusivo do administrador");
        }
        centro.add(btnAdmin);

        JButton btnSair = criarBotaoMenu("Sair", "Encerrar sessão", () -> fazerLogout());
        btnSair.setBackground(new Color(200, 60, 50));
        centro.add(btnSair);

        add(centro, BorderLayout.CENTER);

        // --- Rodapé ---
        JLabel rodape = new JLabel("ClinicaFácil v1.0 — Projeto Acadêmico Senac", SwingConstants.CENTER);
        rodape.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        rodape.setForeground(Color.GRAY);
        rodape.setBorder(BorderFactory.createEmptyBorder(6, 0, 6, 0));
        add(rodape, BorderLayout.SOUTH);
    }

    private JButton criarBotaoMenu(String titulo, String subtitulo, Runnable acao) {
        JButton btn = new JButton("<html><b>" + titulo + "</b><br><small>" + subtitulo + "</small></html>");
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setBackground(new Color(33, 97, 140));
        btn.setForeground(Color.BLACK);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> acao.run());
        return btn;
    }

    private void abrirPacientes() {
        new TelaPaciente(this).setVisible(true);
    }

    private void abrirMedicos() {
        new TelaMedico(this).setVisible(true);
    }

    private void abrirAgenda() {
        new TelaAgenda(this).setVisible(true);
    }

    private void fazerLogout() {
        if (JOptionPane.showConfirmDialog(this, "Deseja sair do sistema?", "Sair",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            LoginController.logout();
            dispose();
            new TelaLogin().setVisible(true);
        }
    }
}
