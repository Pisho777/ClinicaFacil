package view;

import Controller.MedicoController;
import dao.EspecialidadeDAO;
import model.Especialidade;
import model.Medico;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class TelaMedico extends JDialog {

    private final JTextField txtNome      = new JTextField(30);
    private final JTextField txtCrm       = new JTextField(15);
    private final JTextField txtUf        = new JTextField(3);
    private final JTextField txtTelefone  = new JTextField(15);
    private final JComboBox<Especialidade> cmbEsp = new JComboBox<>();
    private final JCheckBox chkAtivo      = new JCheckBox("Ativo", true);

    private final DefaultTableModel modeloTabela = new DefaultTableModel(
            new String[]{"ID", "Nome", "CRM", "UF", "Especialidade", "Ativo"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable tabela = new JTable(modeloTabela);

    private int idSelecionado = 0;
    private final MedicoController ctrl = new MedicoController();
    private final EspecialidadeDAO espDao = new EspecialidadeDAO();

    public TelaMedico(Frame owner) {
        super(owner, "Cadastro de Médicos", true);
        carregarEspecialidades();
        construirUI();
        carregarTabela();
    }

    private void carregarEspecialidades() {
        try {
            for (Especialidade e : espDao.listarTodas()) cmbEsp.addItem(e);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar especialidades: " + e.getMessage());
        }
    }

    private void construirUI() {
        setSize(820, 580);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout(10, 10));
        add(painelFormulario(), BorderLayout.NORTH);
        add(painelTabela(),     BorderLayout.CENTER);
        add(painelBotoes(),     BorderLayout.SOUTH);
    }

    private JPanel painelFormulario() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBorder(BorderFactory.createTitledBorder("Dados do Médico"));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(5, 6, 5, 6);
        g.anchor = GridBagConstraints.WEST;

        g.gridx=0; g.gridy=0; p.add(new JLabel("Nome *:"), g);
        g.gridx=1; g.gridwidth=3; g.fill=GridBagConstraints.HORIZONTAL;
        p.add(txtNome, g); g.fill=GridBagConstraints.NONE; g.gridwidth=1;

        g.gridx=0; g.gridy=1; p.add(new JLabel("CRM *:"), g);
        g.gridx=1; p.add(txtCrm, g);
        g.gridx=2; p.add(new JLabel("UF *:"), g);
        g.gridx=3; p.add(txtUf, g);

        g.gridx=0; g.gridy=2; p.add(new JLabel("Especialidade *:"), g);
        g.gridx=1; g.gridwidth=3; g.fill=GridBagConstraints.HORIZONTAL;
        p.add(cmbEsp, g); g.fill=GridBagConstraints.NONE; g.gridwidth=1;

        g.gridx=0; g.gridy=3; p.add(new JLabel("Telefone:"), g);
        g.gridx=1; p.add(txtTelefone, g);
        g.gridx=2; p.add(chkAtivo, g);

        return p;
    }

    private JPanel painelTabela() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(BorderFactory.createTitledBorder("Médicos Cadastrados"));
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.setRowHeight(22);
        tabela.getColumnModel().getColumn(0).setMaxWidth(50);
        tabela.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) preencherFormulario();
        });
        p.add(new JScrollPane(tabela), BorderLayout.CENTER);
        return p;
    }

    private JPanel painelBotoes() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 8));
        JButton btnNovo    = new JButton("Novo");
        JButton btnSalvar  = new JButton("Salvar");
        JButton btnExcluir = new JButton("Excluir");
        JButton btnFechar  = new JButton("Fechar");

        btnSalvar.setBackground(new Color(33, 97, 140)); btnSalvar.setForeground(Color.WHITE);
        btnExcluir.setBackground(new Color(180, 50, 40)); btnExcluir.setForeground(Color.WHITE);

        btnNovo.addActionListener(e -> limpar());
        btnSalvar.addActionListener(e -> salvar());
        btnExcluir.addActionListener(e -> excluir());
        btnFechar.addActionListener(e -> dispose());

        p.add(btnNovo); p.add(btnSalvar); p.add(btnExcluir); p.add(btnFechar);
        return p;
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

        if (ctrl.salvar(m, this)) { limpar(); carregarTabela(); }
    }

    private void excluir() {
        if (idSelecionado == 0) {
            JOptionPane.showMessageDialog(this, "Selecione um médico.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (ctrl.excluir(idSelecionado, this)) { limpar(); carregarTabela(); }
    }

    private void carregarTabela() {
        modeloTabela.setRowCount(0);
        for (Medico m : ctrl.listarTodos(this)) {
            modeloTabela.addRow(new Object[]{
                m.getId(), m.getNome(), m.getCrm(), m.getUfCrm(),
                m.getEspecialidade().getNome(), m.isAtivo() ? "Sim" : "Não"
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
        chkAtivo.setSelected("Sim".equals(modeloTabela.getValueAt(row, 5)));
        String espNome = (String) modeloTabela.getValueAt(row, 4);
        for (int i = 0; i < cmbEsp.getItemCount(); i++) {
            if (cmbEsp.getItemAt(i).getNome().equals(espNome)) {
                cmbEsp.setSelectedIndex(i); break;
            }
        }
    }

    private void limpar() {
        idSelecionado = 0;
        txtNome.setText(""); txtCrm.setText(""); txtUf.setText("");
        txtTelefone.setText(""); chkAtivo.setSelected(true);
        if (cmbEsp.getItemCount() > 0) cmbEsp.setSelectedIndex(0);
        tabela.clearSelection();
    }
}
