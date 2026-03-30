package view;

import Controller.LoginController;
import model.PerfilUsuario;
import model.Usuario;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

/**
 * Tela Principal — layout com sidebar fixa e dashboard central.
 * Design: sidebar brand (220px) + topbar + área de conteúdo neutral.
 */
public class TelaPrincipal extends JFrame {

    private final Usuario usuarioLogado;

    // Botões da sidebar (guardados para marcar ativo)
    private JButton navHome, navPac, navMed, navAge, navPron, navUsr;

    public TelaPrincipal(Usuario usuario) {
        super("ClinicaFácil");
        this.usuarioLogado = usuario;
        construirUI();
    }

    private void construirUI() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 660);
        setMinimumSize(new Dimension(800, 560));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel shell = new JPanel(new BorderLayout());
        shell.add(criarSidebar(), BorderLayout.WEST);
        shell.add(criarMain(),    BorderLayout.CENTER);
        add(shell);
    }

    // ── Sidebar ──────────────────────────────────────────────────
    private JPanel criarSidebar() {
        JPanel sidebar = DS.criarSidebar();

        // Marca d'água / brand
        JPanel brand = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 14));
        brand.setOpaque(false);
        brand.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(255,255,255,25)));

        JLabel icon = new JLabel("CF");
        icon.setFont(new Font("Segoe UI", Font.BOLD, 14));
        icon.setForeground(DS.WHITE); icon.setOpaque(true);
        icon.setBackground(new Color(255,255,255,38));
        icon.setBorder(DS.emptyBorder(6,10,6,10));

        JPanel nomeVer = new JPanel(new GridLayout(2,1));
        nomeVer.setOpaque(false);
        JLabel nome = new JLabel("ClinicaFácil");
        nome.setFont(new Font("Segoe UI", Font.BOLD, 15)); nome.setForeground(DS.WHITE);
        JLabel ver  = new JLabel("v1.0");
        ver.setFont(DS.F_SMALL); ver.setForeground(new Color(255,255,255,128));
        nomeVer.add(nome); nomeVer.add(ver);

        brand.add(icon); brand.add(nomeVer);
        sidebar.add(brand);

        // Info do usuário
        String iniciais = iniciais(usuarioLogado.getNome());
        JPanel userBox = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        userBox.setOpaque(false);
        userBox.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(255,255,255,25)));

        JLabel avatar = new JLabel(iniciais, SwingConstants.CENTER);
        avatar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        avatar.setForeground(DS.WHITE); avatar.setOpaque(true);
        avatar.setBackground(DS.ACCENT);
        avatar.setPreferredSize(new Dimension(30, 30));

        JPanel uInfo = new JPanel(new GridLayout(2,1));
        uInfo.setOpaque(false);
        JLabel uNome = new JLabel(usuarioLogado.getNome());
        uNome.setFont(new Font("Segoe UI", Font.PLAIN, 12)); uNome.setForeground(new Color(255,255,255,230));
        JLabel uRole = new JLabel(usuarioLogado.getPerfil().getDescricao());
        uRole.setFont(DS.F_SMALL); uRole.setForeground(new Color(255,255,255,128));
        uInfo.add(uNome); uInfo.add(uRole);

        userBox.add(avatar); userBox.add(uInfo);
        sidebar.add(userBox);

        // Navegação
        JPanel nav = new JPanel();
        nav.setLayout(new BoxLayout(nav, BoxLayout.Y_AXIS));
        nav.setOpaque(false);
        nav.setBorder(DS.emptyBorder(8, 8, 8, 8));

        nav.add(secaoNav("Principal"));

        navHome = DS.navItem("⊞", "Dashboard");
        navPac  = DS.navItem("👥", "Pacientes");
        navMed  = DS.navItem("🩺", "Médicos");
        navAge  = DS.navItem("📅", "Agenda");

        DS.setNavAtivo(navHome);

        navPac.addActionListener(e -> { ativarNav(navPac); new TelaPaciente(this).setVisible(true); });
        navMed.addActionListener(e -> { ativarNav(navMed); new TelaMedico(this).setVisible(true); });
        navAge.addActionListener(e -> { ativarNav(navAge); new TelaAgenda(this).setVisible(true); });

        nav.add(navHome); nav.add(navPac); nav.add(navMed); nav.add(navAge);

        nav.add(secaoNav("Clínica"));

        navPron = DS.navItem("📋", "Prontuários");
        navPron.addActionListener(e -> { ativarNav(navAge); new TelaAgenda(this).setVisible(true); });
        if (usuarioLogado.getPerfil() == PerfilUsuario.RECEPCAO) {
            navPron.setEnabled(false);
            navPron.setToolTipText("Acesso restrito a médicos e administradores");
        }
        nav.add(navPron);

        nav.add(secaoNav("Administração"));

        navUsr = DS.navItem("👤", "Usuários");
        if (usuarioLogado.getPerfil() != PerfilUsuario.ADMIN) {
            navUsr.setEnabled(false);
            navUsr.setToolTipText("Acesso exclusivo do administrador");
        }
        nav.add(navUsr);

        sidebar.add(nav);
        sidebar.add(Box.createVerticalGlue());

        // Botão sair
        JPanel footer = new JPanel(new BorderLayout());
        footer.setOpaque(false);
        footer.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(255,255,255,25)));
        JButton btnSair = DS.navItem("🚪", "Sair");
        btnSair.setForeground(new Color(255,255,255,153));
        btnSair.addActionListener(e -> fazerLogout());
        footer.add(btnSair);
        sidebar.add(footer);

        return sidebar;
    }

    private JLabel secaoNav(String texto) {
        JLabel l = new JLabel(texto.toUpperCase());
        l.setFont(new Font("Segoe UI", Font.BOLD, 10));
        l.setForeground(new Color(255,255,255,102));
        l.setBorder(DS.emptyBorder(12, 12, 4, 8));
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    private void ativarNav(JButton ativo) {
        for (JButton b : new JButton[]{navHome, navPac, navMed, navAge, navPron, navUsr}) {
            if (b == null) continue;
            b.setBackground(DS.BRAND);
            b.setForeground(new Color(255,255,255,204));
            b.setFont(DS.F_BODY);
        }
        DS.setNavAtivo(ativo);
    }

    // ── Área principal ───────────────────────────────────────────
    private JPanel criarMain() {
        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(DS.NEUTRAL);

        // Topbar
        String hoje = LocalDate.now().getDayOfWeek()
                .getDisplayName(TextStyle.FULL, new Locale("pt","BR"))
                + ", " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy", new Locale("pt","BR")));
        JPanel topbar = DS.topbar("Dashboard", "Hoje: " + hoje);

        // Botões à direita do topbar
        JPanel topRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 12));
        topRight.setOpaque(false);
        topRight.add(iconeBtn("🔔")); topRight.add(iconeBtn("⚙"));
        topbar.add(topRight, BorderLayout.EAST);

        main.add(topbar, BorderLayout.NORTH);
        main.add(criarDashboard(), BorderLayout.CENTER);
        return main;
    }

    // ── Dashboard ────────────────────────────────────────────────
    private JScrollPane criarDashboard() {
        JPanel dash = new JPanel(new BorderLayout(0, 16));
        dash.setBackground(DS.NEUTRAL);
        dash.setBorder(DS.emptyBorder(20, 20, 20, 20));

        dash.add(criarStatsRow(), BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(1, 2, 16, 0));
        grid.setOpaque(false);
        grid.add(criarCardAgenda());
        grid.add(criarCardAcoes());
        dash.add(grid, BorderLayout.CENTER);

        return new JScrollPane(dash, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    }

    private JPanel criarStatsRow() {
        JPanel row = new JPanel(new GridLayout(1, 4, 12, 0));
        row.setOpaque(false);
        row.add(statCard("Consultas hoje",  "12", "↑ 3 vs ontem",  DS.BRAND_MID, 70));
        row.add(statCard("Pacientes ativos","248","↑ 5 esta semana",DS.ACCENT,   85));
        row.add(statCard("Médicos ativos",  "8",  "→ sem mudança",  DS.WARN,     40));
        row.add(statCard("Cancelamentos",   "2",  "↓ 1 vs ontem",  DS.DANGER,   20));
        return row;
    }

    private JPanel statCard(String label, String valor, String delta, Color barColor, int pct) {
        JPanel c = DS.card();
        c.setLayout(new BoxLayout(c, BoxLayout.Y_AXIS));

        JLabel lbl = DS.labelMuted(label.toUpperCase());
        lbl.setFont(DS.F_LABEL); lbl.setAlignmentX(LEFT_ALIGNMENT);

        JLabel val = new JLabel(valor);
        val.setFont(DS.F_STAT); val.setForeground(DS.TEXT); val.setAlignmentX(LEFT_ALIGNMENT);

        JLabel dlt = new JLabel(delta);
        dlt.setFont(DS.F_SMALL); dlt.setForeground(DS.TEXT_MUTED); dlt.setAlignmentX(LEFT_ALIGNMENT);

        // Barra de progresso
        JPanel barBg = new JPanel(null); barBg.setBackground(DS.BORDER);
        barBg.setPreferredSize(new Dimension(0, 3)); barBg.setMaximumSize(new Dimension(Integer.MAX_VALUE, 3));
        JPanel barFill = new JPanel(); barFill.setBackground(barColor);
        barFill.setBounds(0, 0, pct * 2, 3); barBg.add(barFill);

        c.add(lbl); c.add(Box.createVerticalStrut(6));
        c.add(val); c.add(Box.createVerticalStrut(2));
        c.add(dlt); c.add(Box.createVerticalStrut(8));
        c.add(barBg);
        return c;
    }

    private JPanel criarCardAgenda() {
        JPanel c = DS.card();
        c.setLayout(new BorderLayout(0, 10));

        JPanel titulo = new JPanel(new BorderLayout());
        titulo.setOpaque(false);
        JLabel t = new JLabel("Agenda de hoje");
        t.setFont(DS.F_HEADING); t.setForeground(DS.TEXT);
        JLabel link = new JLabel("<html><u style='color:#2471A3'>Ver tudo →</u></html>");
        link.setFont(DS.F_SMALL); link.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        link.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                ativarNav(navAge); new TelaAgenda(TelaPrincipal.this).setVisible(true);
            }
        });
        titulo.add(t, BorderLayout.WEST); titulo.add(link, BorderLayout.EAST);

        JPanel lista = new JPanel();
        lista.setOpaque(false);
        lista.setLayout(new BoxLayout(lista, BoxLayout.Y_AXIS));

        lista.add(agendaItem("08:00", "Carlos Silva",  "Dr. João — Clínica Geral", "realizada"));
        lista.add(Box.createVerticalStrut(6));
        lista.add(agendaItem("09:30", "Ana Souza",     "Dra. Maria — Cardiologia", "agendada"));
        lista.add(Box.createVerticalStrut(6));
        lista.add(agendaItem("10:30", "Pedro Costa",   "Dr. João — Clínica Geral", "agendada"));
        lista.add(Box.createVerticalStrut(6));
        lista.add(agendaItem("14:00", "Maria Oliveira","Dra. Maria — Cardiologia", "cancelada"));

        c.add(titulo, BorderLayout.NORTH);
        c.add(lista,  BorderLayout.CENTER);
        return c;
    }

    private JPanel agendaItem(String hora, String paciente, String medico, String status) {
        JPanel item = new JPanel(new BorderLayout(10, 0));
        item.setBackground(DS.NEUTRAL);
        item.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(DS.BORDER, 1, true),
            DS.emptyBorder(8, 10, 8, 10)));
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        JLabel lHora = new JLabel(hora);
        lHora.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lHora.setForeground(DS.BRAND);
        lHora.setPreferredSize(new Dimension(46, 20));

        JPanel info = new JPanel(new GridLayout(2, 1));
        info.setOpaque(false);
        JLabel lPac = new JLabel(paciente);
        lPac.setFont(new Font("Segoe UI", Font.BOLD, 12)); lPac.setForeground(DS.TEXT);
        JLabel lMed = DS.labelMuted(medico);
        info.add(lPac); info.add(lMed);

        JLabel badge = switch (status) {
            case "realizada" -> DS.badgeRealizada();
            case "cancelada" -> DS.badgeCancelada();
            default          -> DS.badgeAgendada();
        };

        item.add(lHora,  BorderLayout.WEST);
        item.add(info,   BorderLayout.CENTER);
        item.add(badge,  BorderLayout.EAST);
        return item;
    }

    private JPanel criarCardAcoes() {
        JPanel c = DS.card();
        c.setLayout(new BorderLayout(0, 12));

        JLabel t = new JLabel("Ações rápidas");
        t.setFont(DS.F_HEADING); t.setForeground(DS.TEXT);

        JPanel grid = new JPanel(new GridLayout(2, 2, 8, 8));
        grid.setOpaque(false);

        grid.add(quickBtn("📅", "Nova consulta",  "Agendar agora",   () -> { ativarNav(navAge); new TelaAgenda(this).setVisible(true); }));
        grid.add(quickBtn("👤", "Novo paciente",  "Cadastrar",       () -> { ativarNav(navPac); new TelaPaciente(this).setVisible(true); }));
        grid.add(quickBtn("🩺", "Médicos",        "Gerenciar",       () -> { ativarNav(navMed); new TelaMedico(this).setVisible(true); }));
        grid.add(quickBtn("🔍", "Buscar",         "Localizar paciente", () -> { ativarNav(navPac); new TelaPaciente(this).setVisible(true); }));

        c.add(t, BorderLayout.NORTH); c.add(grid, BorderLayout.CENTER);
        return c;
    }

    private JPanel quickBtn(String icone, String titulo, String sub, Runnable acao) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(DS.WHITE);
        p.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(DS.BORDER, 1, true),
            DS.emptyBorder(10, 12, 10, 12)));
        p.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel lIcon = new JLabel(icone); lIcon.setFont(new Font("Segoe UI", Font.PLAIN, 20)); lIcon.setAlignmentX(LEFT_ALIGNMENT);
        JLabel lTit  = new JLabel(titulo); lTit.setFont(new Font("Segoe UI", Font.BOLD, 12)); lTit.setForeground(DS.TEXT); lTit.setAlignmentX(LEFT_ALIGNMENT);
        JLabel lSub  = DS.labelMuted(sub); lSub.setAlignmentX(LEFT_ALIGNMENT);

        p.add(lIcon); p.add(Box.createVerticalStrut(4)); p.add(lTit); p.add(lSub);
        p.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) { acao.run(); }
            public void mouseEntered(java.awt.event.MouseEvent e) { p.setBackground(DS.BRAND_LIGHT); }
            public void mouseExited (java.awt.event.MouseEvent e) { p.setBackground(DS.WHITE); }
        });
        return p;
    }

    private JButton iconeBtn(String texto) {
        JButton b = new JButton(texto);
        b.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        b.setBackground(DS.NEUTRAL); b.setForeground(DS.TEXT_MUTED);
        b.setFocusPainted(false); b.setBorderPainted(true);
        b.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(DS.BORDER, 1, true),
            DS.emptyBorder(4, 8, 4, 8)));
        return b;
    }

    private String iniciais(String nome) {
        String[] p = nome.trim().split("\\s+");
        if (p.length >= 2) return ("" + p[0].charAt(0) + p[1].charAt(0)).toUpperCase();
        return nome.substring(0, Math.min(2, nome.length())).toUpperCase();
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
