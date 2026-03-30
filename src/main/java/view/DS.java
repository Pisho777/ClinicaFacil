package view;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;

/**
 * Design System centralizado do ClinicaFácil.
 * Contém todas as cores, fontes e helpers de componentes Swing
 * mapeados a partir do arquivo clinicafacil_design_system.html.
 */
public final class DS {

    private DS() {}

    // ── Paleta de cores ──────────────────────────────────────────
    public static final Color BRAND        = new Color(0x1A5276);
    public static final Color BRAND_MID    = new Color(0x2471A3);
    public static final Color BRAND_LIGHT  = new Color(0xD6EAF8);
    public static final Color ACCENT       = new Color(0x1ABC9C);
    public static final Color ACCENT_LIGHT = new Color(0xD1F2EB);
    public static final Color DANGER       = new Color(0xC0392B);
    public static final Color DANGER_LIGHT = new Color(0xFADBD8);
    public static final Color WARN         = new Color(0xE67E22);
    public static final Color WARN_LIGHT   = new Color(0xFDEBD0);
    public static final Color NEUTRAL      = new Color(0xF4F6F9);
    public static final Color BORDER       = new Color(0xDDE3EE);
    public static final Color TEXT         = new Color(0x1A2B45);
    public static final Color TEXT_MUTED   = new Color(0x7F8C9A);
    public static final Color WHITE        = Color.WHITE;

    // ── Fontes ───────────────────────────────────────────────────
    public static final Font F_TITLE    = new Font("Segoe UI", Font.BOLD,   20);
    public static final Font F_HEADING  = new Font("Segoe UI", Font.BOLD,   15);
    public static final Font F_BODY     = new Font("Segoe UI", Font.PLAIN,  13);
    public static final Font F_SMALL    = new Font("Segoe UI", Font.PLAIN,  12);
    public static final Font F_LABEL    = new Font("Segoe UI", Font.BOLD,   11);
    public static final Font F_STAT     = new Font("Segoe UI", Font.BOLD,   26);

    // ── Bordas ───────────────────────────────────────────────────
    public static Border borderCard()   { return BorderFactory.createLineBorder(BORDER, 1, true); }
    public static Border emptyBorder(int top, int left, int bottom, int right) {
        return BorderFactory.createEmptyBorder(top, left, bottom, right);
    }

    // ── Botões ───────────────────────────────────────────────────
    public static JButton btnBrand(String texto) {
        return estilizarBtn(new JButton(texto), BRAND, WHITE);
    }
    public static JButton btnOutline(String texto) {
        JButton b = new JButton(texto);
        b.setFont(F_BODY); b.setFocusPainted(false); b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBackground(WHITE); b.setForeground(TEXT);
        b.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER, 1, true),
            emptyBorder(6, 12, 6, 12)));
        return b;
    }
    public static JButton btnDanger(String texto)  { return estilizarBtn(new JButton(texto), DANGER, WHITE); }
    public static JButton btnSuccess(String texto) { return estilizarBtn(new JButton(texto), ACCENT, WHITE); }

    private static JButton estilizarBtn(JButton b, Color bg, Color fg) {
        b.setFont(F_BODY); b.setFocusPainted(false); b.setOpaque(true); b.setBorderPainted(false);
        b.setBackground(bg); b.setForeground(fg);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(emptyBorder(7, 14, 7, 14));
        return b;
    }

    // ── Campos ───────────────────────────────────────────────────
    public static JTextField campo(int cols) {
        JTextField f = new JTextField(cols);
        estilizarCampo(f);
        return f;
    }
    public static JPasswordField campoSenha(int cols) {
        JPasswordField f = new JPasswordField(cols);
        estilizarCampo(f);
        return f;
    }
    public static void estilizarCampo(JComponent c) {
        c.setFont(F_BODY);
        c.setBackground(NEUTRAL);
        c.setForeground(TEXT);
        c.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER, 1, true),
            emptyBorder(8, 10, 8, 10)));
    }
    public static JComboBox<?> combo() {
        JComboBox<?> cb = new JComboBox<>();
        cb.setFont(F_BODY); cb.setBackground(NEUTRAL); cb.setForeground(TEXT);
        return cb;
    }

    // ── Labels ───────────────────────────────────────────────────
    public static JLabel labelCampo(String texto) {
        JLabel l = new JLabel(texto.toUpperCase());
        l.setFont(F_LABEL); l.setForeground(TEXT_MUTED);
        return l;
    }
    public static JLabel labelMuted(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(F_SMALL); l.setForeground(TEXT_MUTED);
        return l;
    }

    // ── Badge (label colorido de status) ─────────────────────────
    public static JLabel badge(String texto, Color bg, Color fg) {
        JLabel l = new JLabel(" " + texto + " ");
        l.setFont(new Font("Segoe UI", Font.BOLD, 11));
        l.setOpaque(true); l.setBackground(bg); l.setForeground(fg);
        l.setBorder(emptyBorder(2, 6, 2, 6));
        return l;
    }
    public static JLabel badgeAgendada()  { return badge("Agendada",  BRAND_LIGHT,  BRAND); }
    public static JLabel badgeRealizada() { return badge("Realizada", ACCENT_LIGHT, new Color(0x0E6655)); }
    public static JLabel badgeCancelada() { return badge("Cancelada", DANGER_LIGHT, DANGER); }
    public static JLabel badgeAtivo()     { return badge("Ativo",     ACCENT_LIGHT, new Color(0x0E6655)); }
    public static JLabel badgeInativo()   { return badge("Inativo",   DANGER_LIGHT, DANGER); }

    // ── JTable ───────────────────────────────────────────────────
    public static void estilizarTabela(JTable t) {
        t.setFont(F_BODY);
        t.setRowHeight(36);
        t.setGridColor(BORDER);
        t.setShowVerticalLines(false);
        t.setSelectionBackground(BRAND_LIGHT);
        t.setSelectionForeground(TEXT);
        t.setForeground(TEXT);
        t.setBackground(WHITE);
        t.setIntercellSpacing(new Dimension(0, 0));

        JTableHeader header = t.getTableHeader();
        header.setFont(F_LABEL);
        header.setBackground(NEUTRAL);
        header.setForeground(TEXT_MUTED);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER));
        header.setPreferredSize(new Dimension(header.getWidth(), 36));

        // Centraliza as células
        DefaultTableCellRenderer r = new DefaultTableCellRenderer();
        r.setHorizontalAlignment(SwingConstants.LEFT);
        r.setBorder(emptyBorder(0, 12, 0, 12));
        for (int i = 0; i < t.getColumnCount(); i++) t.getColumnModel().getColumn(i).setCellRenderer(r);
    }

    // ── Sidebar ──────────────────────────────────────────────────
    public static JPanel criarSidebar() {
        JPanel p = new JPanel();
        p.setBackground(BRAND);
        p.setPreferredSize(new Dimension(220, 0));
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        return p;
    }

    /** Item de navegação da sidebar */
    public static JButton navItem(String icone, String texto) {
        JButton b = new JButton(icone + "  " + texto);
        b.setFont(F_BODY);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setOpaque(true);
        b.setContentAreaFilled(true);
        b.setBackground(BRAND);
        b.setForeground(new Color(255, 255, 255, 204));
        b.setHorizontalAlignment(SwingConstants.LEFT);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(emptyBorder(9, 12, 9, 12));
        b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        b.putClientProperty("ativo", false);   // estado inicial

        b.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                if (Boolean.FALSE.equals(b.getClientProperty("ativo"))) {
                    b.setBackground(new Color(0x1E6090)); // BRAND levemente mais claro
                    b.setForeground(Color.WHITE);
                }
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                if (Boolean.FALSE.equals(b.getClientProperty("ativo"))) {
                    b.setBackground(BRAND);
                    b.setForeground(new Color(255, 255, 255, 204));
                }
            }
        });
        return b;
    }

    /**
     * Marca ativoBtn como ativo e reseta todos os demais botões passados.
     *
     * @param ativoBtn botão que deve ficar destacado
     * @param todos    todos os botões da sidebar (inclusive o ativo)
     */
    public static void setNavAtivo(JButton ativoBtn, JButton... todos) {
        for (JButton b : todos) {
            if (b == null) continue;
            b.putClientProperty("ativo", false);
            b.setBackground(BRAND);
            b.setForeground(new Color(255, 255, 255, 204));
            b.setFont(F_BODY);
        }
        ativoBtn.putClientProperty("ativo", true);
        ativoBtn.setBackground(new Color(0x154360)); // tom escuro = ativo
        ativoBtn.setForeground(Color.WHITE);
        ativoBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
    }

    // ── Card ─────────────────────────────────────────────────────
    public static JPanel card() {
        JPanel p = new JPanel();
        p.setBackground(WHITE);
        p.setBorder(BorderFactory.createCompoundBorder(
            borderCard(), emptyBorder(14, 16, 14, 16)));
        return p;
    }

    // ── Topbar ───────────────────────────────────────────────────
    public static JPanel topbar(String titulo, String subtitulo) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(WHITE);
        p.setPreferredSize(new Dimension(0, 56));
        p.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER));

        JPanel esq = new JPanel(new GridLayout(2, 1));
        esq.setOpaque(false);
        esq.setBorder(emptyBorder(8, 18, 8, 18));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(F_HEADING); lblTitulo.setForeground(TEXT);

        JLabel lblSub = new JLabel(subtitulo);
        lblSub.setFont(F_SMALL); lblSub.setForeground(TEXT_MUTED);

        esq.add(lblTitulo); esq.add(lblSub);
        p.add(esq, BorderLayout.WEST);
        return p;
    }
}
