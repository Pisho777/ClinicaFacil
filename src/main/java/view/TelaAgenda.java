package view;

import Controller.AgendaController;
import Controller.MedicoController;
import Controller.PacienteController;
import model.*;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class TelaAgenda extends JPanel {

    // Formulário novo agendamento
    private final JComboBox<Paciente> cmbPaciente = new JComboBox<>();
    private final JComboBox<Medico>   cmbMedico   = new JComboBox<>();
    private final JTextField txtData  = DS.campo(0);
    private final JTextField txtHora  = DS.campo(0);

    private JPanel painelForm;

    // Filtro
    private final JTextField txtFiltro = DS.campo(12);

    // Tabela
    private final DefaultTableModel modeloTabela = new DefaultTableModel(
            new String[]{"#", "Data / Hora", "Paciente", "Médico", "Status"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable tabela = new JTable(modeloTabela);

    private final AgendaController   agendaCtrl = new AgendaController();
    private final PacienteController pacCtrl    = new PacienteController();
    private final MedicoController   medCtrl    = new MedicoController();

    private static final DateTimeFormatter FMT_DT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final DateTimeFormatter FMT_D  = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public TelaAgenda() {
        carregarCombos();
        construirUI();
        carregarTodasConsultas();
    }

    private void carregarCombos() {
        for (Paciente p : pacCtrl.listarTodos(this)) cmbPaciente.addItem(p);
        for (Medico m   : medCtrl.listarAtivos(this)) cmbMedico.addItem(m);
    }

    private void construirUI() {
        this.setLayout(new BorderLayout());
        this.add(criarMain(), BorderLayout.CENTER);
    }

    private JPanel criarMain() {
        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(DS.NEUTRAL);

        // Topbar com data de hoje
        JPanel topbar = DS.topbar("Agenda de Consultas",
                "Hoje: " + LocalDate.now().format(FMT_D));
        JButton btnHoje = DS.btnOutline("Hoje");
        btnHoje.addActionListener(e -> { txtFiltro.setText(LocalDate.now().format(FMT_D)); filtrarPorData(txtFiltro.getText()); });
        JPanel topRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 12));
        topRight.setOpaque(false);
        topRight.add(btnHoje);
        topbar.add(topRight, BorderLayout.EAST);

        main.add(topbar, BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout(0, 14));
        content.setBackground(DS.NEUTRAL);
        content.setBorder(DS.emptyBorder(16,16,16,16));

        painelForm = criarPainelFormulario();
        painelForm.setVisible(false);

        JPanel corpo = new JPanel(new BorderLayout(0,10));
        corpo.setOpaque(false);
        corpo.add(criarToolbar(),      BorderLayout.NORTH);
        corpo.add(criarPainelTabela(), BorderLayout.CENTER);

        content.add(painelForm, BorderLayout.NORTH);
        content.add(corpo,      BorderLayout.CENTER);
        main.add(content, BorderLayout.CENTER);
        return main;
    }

    private JPanel criarPainelFormulario() {
        JPanel outer = new JPanel(new BorderLayout(0, 12));
        outer.setBackground(DS.WHITE);
        outer.setBorder(BorderFactory.createCompoundBorder(DS.borderCard(), DS.emptyBorder(16,18,16,18)));

        JLabel titulo = new JLabel("Nova Consulta");
        titulo.setFont(DS.F_HEADING); titulo.setForeground(DS.TEXT);
        titulo.setBorder(BorderFactory.createMatteBorder(0,0,1,0,DS.BORDER));

        JPanel grid = new JPanel(new GridBagLayout());
        grid.setOpaque(false);
        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL; g.insets = new Insets(6,6,6,6);

        DS.estilizarCampo(cmbPaciente); DS.estilizarCampo(cmbMedico);

        g.gridy=0; g.gridx=0; g.gridwidth=1; grid.add(DS.labelCampo("Paciente *"), g);
        g.gridx=1; g.gridwidth=2; grid.add(cmbPaciente, g); g.gridwidth=1;

        g.gridx=3; grid.add(DS.labelCampo("Médico *"), g);
        g.gridx=4; g.gridwidth=2; grid.add(cmbMedico, g); g.gridwidth=1;

        g.gridy=1; g.gridx=0; grid.add(DS.labelCampo("Data * (dd/MM/yyyy)"), g);
        g.gridx=1; grid.add(txtData, g);
        g.gridx=2; grid.add(DS.labelCampo("Hora * (HH:mm)"), g);
        g.gridx=3; grid.add(txtHora, g);

        // Hint
        JLabel hint = DS.labelMuted("⚠  Consultas não podem ser agendadas no passado | Somente médicos ativos");
        g.gridy=2; g.gridx=0; g.gridwidth=6; grid.add(hint, g);

        JPanel acoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        acoes.setOpaque(false);
        JButton btnCancelar = DS.btnOutline("Cancelar");
        JButton btnSalvar   = DS.btnBrand("Agendar consulta");
        btnCancelar.addActionListener(e -> { painelForm.setVisible(false); limparForm(); });
        btnSalvar.addActionListener(e -> agendar());
        acoes.add(btnCancelar); acoes.add(btnSalvar);

        outer.add(titulo, BorderLayout.NORTH);
        outer.add(grid,   BorderLayout.CENTER);
        outer.add(acoes,  BorderLayout.SOUTH);
        return outer;
    }

    private JPanel criarToolbar() {
        JPanel p = new JPanel(new BorderLayout(8, 0));
        p.setOpaque(false);

        JPanel esq = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        esq.setOpaque(false);
        JButton btnFiltrar = DS.btnOutline("Filtrar");
        JButton btnTodas   = DS.btnOutline("Todas");
        btnFiltrar.addActionListener(e -> filtrarPorData(txtFiltro.getText()));
        btnTodas.addActionListener(e -> { txtFiltro.setText(""); carregarTodasConsultas(); });
        esq.add(txtFiltro); esq.add(btnFiltrar); esq.add(btnTodas);

        JButton btnNova = DS.btnBrand("+ Nova Consulta");
        btnNova.addActionListener(e -> { limparForm(); painelForm.setVisible(true); });

        p.add(esq,    BorderLayout.WEST);
        p.add(btnNova, BorderLayout.EAST);
        return p;
    }

    private JPanel criarPainelTabela() {
        DS.estilizarTabela(tabela);
        tabela.getColumnModel().getColumn(0).setMaxWidth(50);
        tabela.getColumnModel().getColumn(4).setPreferredWidth(100);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Renderer status colorido
        tabela.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean foc, int r, int c) {
                JLabel l = (JLabel) super.getTableCellRendererComponent(t, v, sel, foc, r, c);
                l.setBorder(DS.emptyBorder(0,12,0,12)); l.setOpaque(true);
                String txt = v == null ? "" : v.toString();
                if ("Realizada".equals(txt))  { l.setBackground(DS.ACCENT_LIGHT); l.setForeground(new Color(0x0E6655)); }
                else if ("Cancelada".equals(txt)) { l.setBackground(DS.DANGER_LIGHT); l.setForeground(DS.DANGER); }
                else { l.setBackground(DS.BRAND_LIGHT); l.setForeground(DS.BRAND); }
                if (sel) { l.setBackground(DS.BRAND_LIGHT); l.setForeground(DS.TEXT); }
                return l;
            }
        });

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBorder(DS.borderCard());
        scroll.getViewport().setBackground(DS.WHITE);

        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setOpaque(false);

        // Rodapé de ações
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 6));
        footer.setOpaque(false);
        JButton btnCancelar  = DS.btnDanger("Cancelar Consulta");
        JButton btnProntuario = DS.btnSuccess("Abrir Prontuário");
        btnCancelar.addActionListener(e -> cancelar());
        btnProntuario.addActionListener(e -> abrirProntuario());
        footer.add(btnCancelar); footer.add(btnProntuario);

        wrap.add(scroll,  BorderLayout.CENTER);
        wrap.add(footer,  BorderLayout.SOUTH);
        return wrap;
    }

    // ── Lógica ──────────────────────────────────────────────────
    private void agendar() {
        Paciente pac = (Paciente) cmbPaciente.getSelectedItem();
        Medico med   = (Medico)   cmbMedico.getSelectedItem();
        if (pac == null || med == null) {
            JOptionPane.showMessageDialog(this, "Selecione paciente e médico."); return;
        }
        LocalDateTime dt;
        try {
            dt = LocalDateTime.parse(txtData.getText().trim() + " " + txtHora.getText().trim(), FMT_DT);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Data/hora inválidas. Use dd/MM/yyyy e HH:mm.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Consulta c = new Consulta();
        c.setPaciente(pac); c.setMedico(med); c.setDataHora(dt);
        if (agendaCtrl.agendarConsulta(c, this)) {
            painelForm.setVisible(false); limparForm(); carregarTodasConsultas();
        }
    }

    private void cancelar() {
        int row = tabela.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Selecione uma consulta."); return; }
        int id = (int) modeloTabela.getValueAt(row, 0);
        String motivo = JOptionPane.showInputDialog(this, "Informe o motivo do cancelamento:");
        if (motivo == null) return;
        Consulta c = new Consulta(); c.setId(id);
        if (agendaCtrl.cancelarConsulta(c, motivo, this)) carregarTodasConsultas();
    }

    private void abrirProntuario() {
        int row = tabela.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Selecione uma consulta."); return; }
        int idConsulta = (int) modeloTabela.getValueAt(row, 0);
        Consulta c = new Consulta(); c.setId(idConsulta);
        Paciente pac = new Paciente(); pac.setNome((String) modeloTabela.getValueAt(row, 2));
        Medico med = new Medico();   med.setNome((String) modeloTabela.getValueAt(row, 3));
        c.setPaciente(pac); c.setMedico(med);
        Frame frame = (Frame) SwingUtilities.getWindowAncestor(this);
        new TelaProntuario(frame, c, agendaCtrl).setVisible(true);
        carregarTodasConsultas();
    }

    private void carregarTodasConsultas() { preencherTabela(agendaCtrl.listarTodas(this)); }

    private void filtrarPorData(String s) {
        if (s == null || s.isBlank()) { carregarTodasConsultas(); return; }
        try { preencherTabela(agendaCtrl.listarPorDia(LocalDate.parse(s.trim(), FMT_D), this)); }
        catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Data inválida. Use dd/MM/yyyy.", "Atenção", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void preencherTabela(List<Consulta> lista) {
        modeloTabela.setRowCount(0);
        for (Consulta c : lista) {
            modeloTabela.addRow(new Object[]{
                c.getId(), c.getDataHora().format(FMT_DT),
                c.getPaciente().getNome(), c.getMedico().getNome(),
                c.getStatus().getDescricao()
            });
        }
    }

    private void limparForm() {
        txtData.setText(""); txtHora.setText("");
        if (cmbPaciente.getItemCount() > 0) cmbPaciente.setSelectedIndex(0);
        if (cmbMedico.getItemCount()   > 0) cmbMedico.setSelectedIndex(0);
    }
}
