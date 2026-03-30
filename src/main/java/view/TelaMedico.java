package view;

import Controller.MedicoController;
import dao.EspecialidadeDAO;
import model.Especialidade;
import model.Medico;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class TelaMedico extends JDialog {

    private final JTextField txtNome     = DS.campo(0);
    private final JTextField txtCrm      = DS.campo(0);
    private final JTextField txtUf       = DS.campo(0);
    private final JTextField txtTelefone = DS.campo(0);
    private final JComboBox<Especialidade> cmbEsp = new JComboBox<>();
    private final JCheckBox chkAtivo     = new JCheckBox("Médico ativo", true);

    private JPanel painelForm;
    private final DefaultTableModel modeloTabela = new DefaultTableModel(
            new String[]{"#", "Nome", "CRM", "UF", "Especialidade", "Status"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable tabela = new JTable(modeloTabela);

    private int idSelecionado = 0;
    private final MedicoController ctrl   = new MedicoController();
    private final EspecialidadeDAO espDao = new EspecialidadeDAO();

    public TelaMedico(Frame owner) {
        super(owner, "Médicos", true);
        carregarEspecialidades();
        construirUI();
        carregarTabela();
    }

    private void carregarEspecialidades() {
        try { for (Especialidade e : espDao.listarTodas()) cmbEsp.addItem(e); }
        catch (SQLException e) { JOptionPane.showMessageDialog(this, "Erro ao carregar especialidades: " + e.getMessage()); }
    }

    private void construirUI() {
        setSize(920, 660);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout());

        JPanel shell = new JPanel(new BorderLayout());
        shell.add(criarSidebar(), BorderLayout.WEST);
        shell.add(criarMain(),    BorderLayout.CENTER);
        add(shell);
    }

    private JPanel criarSidebar() {
        JPanel sb = DS.criarSidebar();
        JPanel brand = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 14));
        brand.setOpaque(false);
        brand.setBorder(BorderFactory.createMatteBorder(0,0,1,0,new Color(255,255,255,25)));
        JLabel ico = new JLabel("CF"); ico.setFont(new Font("Segoe UI",Font.BOLD,14));
        ico.setForeground(DS.WHITE); ico.setOpaque(true); ico.setBackground(new Color(255,255,255,38));
        ico.setBorder(DS.emptyBorder(6,10,6,10));
        JLabel nm = new JLabel("ClinicaFácil"); nm.setFont(new Font("Segoe UI",Font.BOLD,15)); nm.setForeground(DS.WHITE);
        brand.add(ico); brand.add(nm); sb.add(brand);

        JPanel nav = new JPanel(); nav.setLayout(new BoxLayout(nav, BoxLayout.Y_AXIS)); nav.setOpaque(false);
        nav.setBorder(DS.emptyBorder(8,8,8,8));

        JButton bHome = DS.navItem("⊞","Dashboard"); bHome.addActionListener(e -> dispose());
        JButton bPac  = DS.navItem("👥","Pacientes");
        JButton bMed  = DS.navItem("🩺","Médicos"); DS.setNavAtivo(bMed);
        JButton bAge  = DS.navItem("📅","Agenda");

        nav.add(bHome); nav.add(bPac); nav.add(bMed); nav.add(bAge);
        sb.add(nav); sb.add(Box.createVerticalGlue());

        JPanel footer = new JPanel(new BorderLayout()); footer.setOpaque(false);
        footer.setBorder(BorderFactory.createMatteBorder(1,0,0,0,new Color(255,255,255,25)));
        JButton btnSair = DS.navItem("🚪","Fechar"); btnSair.addActionListener(e -> dispose());
        footer.add(btnSair); sb.add(footer);
        return sb;
    }

    private JPanel criarMain() {
        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(DS.NEUTRAL);
        main.add(DS.topbar("Médicos", "Cadastro e gerenciamento de médicos"), BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout(0, 14));
        content.setBackground(DS.NEUTRAL);
        content.setBorder(DS.emptyBorder(16, 16, 16, 16));

        painelForm = criarPainelFormulario();
        painelForm.setVisible(false);

        JPanel corpo = new JPanel(new BorderLayout(0, 10));
        corpo.setOpaque(false);
        corpo.add(criarToolbar(),  BorderLayout.NORTH);
        corpo.add(criarPainelTabela(), BorderLayout.CENTER);

        content.add(painelForm, BorderLayout.NORTH);
        content.add(corpo,      BorderLayout.CENTER);
        main.add(content, BorderLayout.CENTER);
        return main;
    }

    private JPanel criarPainelFormulario() {
        JPanel outer = new JPanel(new BorderLayout(0, 12));
        outer.setBackground(DS.WHITE);
        outer.setBorder(BorderFactory.createCompoundBorder(
            DS.borderCard(), DS.emptyBorder(16, 18, 16, 18)));

        JLabel titulo = new JLabel("Novo Médico");
        titulo.setFont(DS.F_HEADING); titulo.setForeground(DS.TEXT);
        titulo.setBorder(BorderFactory.createMatteBorder(0,0,1,0,DS.BORDER));
        titulo.setName("titulo_form_med");

        JPanel grid = new JPanel(new GridBagLayout());
        grid.setOpaque(false);
        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL; g.insets = new Insets(6,6,6,6);

        // Nome
        g.gridy=0; g.gridx=0; g.gridwidth=1; grid.add(DS.labelCampo("Nome completo *"), g);
        g.gridx=1; g.gridwidth=5; grid.add(txtNome, g); g.gridwidth=1;

        // CRM, UF
        g.gridy=1; g.gridx=0; grid.add(DS.labelCampo("CRM *"), g);
        g.gridx=1; grid.add(txtCrm, g);
        g.gridx=2; grid.add(DS.labelCampo("UF *"), g);
        txtUf.setPreferredSize(new Dimension(50, 34));
        g.gridx=3; grid.add(txtUf, g);

        // Especialidade
        g.gridy=2; g.gridx=0; grid.add(DS.labelCampo("Especialidade *"), g);
        DS.estilizarCampo(cmbEsp);
        g.gridx=1; g.gridwidth=3; grid.add(cmbEsp, g); g.gridwidth=1;

        // Telefone + ativo
        g.gridy=3; g.gridx=0; grid.add(DS.labelCampo("Telefone"), g);
        g.gridx=1; grid.add(txtTelefone, g);
        chkAtivo.setFont(DS.F_BODY); chkAtivo.setOpaque(false);
        g.gridx=3; grid.add(chkAtivo, g);

        // Ações
        JPanel acoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        acoes.setOpaque(false);
        JButton btnCancelar = DS.btnOutline("Cancelar");
        JButton btnSalvar   = DS.btnBrand("Salvar médico");
        btnCancelar.addActionListener(e -> { painelForm.setVisible(false); limpar(); });
        btnSalvar.addActionListener(e -> salvar());
        acoes.add(btnCancelar); acoes.add(btnSalvar);

        outer.add(titulo, BorderLayout.NORTH);
        outer.add(grid,   BorderLayout.CENTER);
        outer.add(acoes,  BorderLayout.SOUTH);
        return outer;
    }

    private JPanel criarToolbar() {
        JPanel p = new JPanel(new BorderLayout(8, 0));
        p.setOpaque(false);
        JButton btnNovo = DS.btnBrand("+ Novo Médico");
        btnNovo.addActionListener(e -> {
            limpar();
            setTituloForm("Novo Médico");
            painelForm.setVisible(true);
        });
        p.add(btnNovo, BorderLayout.EAST);
        return p;
    }

    private JPanel criarPainelTabela() {
        DS.estilizarTabela(tabela);
        tabela.getColumnModel().getColumn(0).setMaxWidth(50);
        tabela.getColumnModel().getColumn(5).setPreferredWidth(90);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) preencherFormulario();
        });

        // Renderer com badge de status na coluna 5
        tabela.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean foc, int r, int c) {
                JLabel l = (JLabel) super.getTableCellRendererComponent(t, v, sel, foc, r, c);
                l.setBorder(DS.emptyBorder(0, 12, 0, 12));
                String txt = v == null ? "" : v.toString();
                if ("Ativo".equals(txt))  { l.setForeground(new Color(0x0E6655)); l.setBackground(DS.ACCENT_LIGHT); }
                else                      { l.setForeground(DS.DANGER);           l.setBackground(DS.DANGER_LIGHT); }
                if (!sel) setBackground(l.getBackground());
                l.setOpaque(true);
                return l;
            }
        });

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBorder(DS.borderCard());
        scroll.getViewport().setBackground(DS.WHITE);

        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setOpaque(false);

        // Botão excluir abaixo da tabela
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 6));
        footer.setOpaque(false);
        JButton btnExcluir = DS.btnDanger("Excluir selecionado");
        btnExcluir.addActionListener(e -> excluir());
        footer.add(btnExcluir);

        wrap.add(scroll,    BorderLayout.CENTER);
        wrap.add(footer,    BorderLayout.SOUTH);
        return wrap;
    }

    private void salvar() {
        Medico m = new Medico();
        m.setId(idSelecionado);
        m.setNome(txtNome.getText().trim());
        m.setCrm(txtCrm.getText().trim());
        m.setUfCrm(txtUf.getText().trim().toUpperCase());
        m.setTelefone(txtTelefone.getText().trim());
        m.setAtivo(chkAtivo.isSelected());
        m.setEspecialidade((Especialidade) cmbEsp.getSelectedItem());
        if (ctrl.salvar(m, this)) { painelForm.setVisible(false); limpar(); carregarTabela(); }
    }

    private void excluir() {
        if (idSelecionado == 0) { JOptionPane.showMessageDialog(this, "Selecione um médico.", "Atenção", JOptionPane.WARNING_MESSAGE); return; }
        if (ctrl.excluir(idSelecionado, this)) { painelForm.setVisible(false); limpar(); carregarTabela(); }
    }

    private void carregarTabela() {
        modeloTabela.setRowCount(0);
        for (Medico m : ctrl.listarTodos(this)) {
            modeloTabela.addRow(new Object[]{
                m.getId(), m.getNome(), m.getCrm(), m.getUfCrm(),
                m.getEspecialidade().getNome(), m.isAtivo() ? "Ativo" : "Inativo"
            });
        }
    }

    private void preencherFormulario() {
        int row = tabela.getSelectedRow();
        if (row < 0) return;
        idSelecionado = (int) modeloTabela.getValueAt(row, 0);
        txtNome.setText((String) modeloTabela.getValueAt(row, 1));
        txtCrm.setText((String) modeloTabela.getValueAt(row, 2));
        txtUf.setText((String) modeloTabela.getValueAt(row, 3));
        chkAtivo.setSelected("Ativo".equals(modeloTabela.getValueAt(row, 5)));
        String espNome = (String) modeloTabela.getValueAt(row, 4);
        for (int i = 0; i < cmbEsp.getItemCount(); i++) {
            if (cmbEsp.getItemAt(i).getNome().equals(espNome)) { cmbEsp.setSelectedIndex(i); break; }
        }
        setTituloForm("Editar Médico");
        painelForm.setVisible(true);
    }

    private void limpar() {
        idSelecionado = 0;
        txtNome.setText(""); txtCrm.setText(""); txtUf.setText(""); txtTelefone.setText("");
        chkAtivo.setSelected(true);
        if (cmbEsp.getItemCount() > 0) cmbEsp.setSelectedIndex(0);
        tabela.clearSelection();
    }

    private void setTituloForm(String texto) {
        for (Component c : painelForm.getComponents()) {
            if (c instanceof JLabel l && "titulo_form_med".equals(l.getName())) l.setText(texto);
        }
    }
}
