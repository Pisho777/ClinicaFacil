package view;

import Controller.PacienteController;
import model.Paciente;
import util.ValidadorCPF;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class TelaPaciente extends JDialog {

    // Formulário
    private final JTextField txtNome        = new JTextField(30);
    private final JTextField txtCpf         = new JTextField(15);
    private final JTextField txtDataNasc    = new JTextField(10);
    private final JTextField txtTelefone    = new JTextField(15);
    private final JTextField txtEmail       = new JTextField(25);
    private final JTextField txtEndereco    = new JTextField(35);
    private final JTextField txtResponsavel = new JTextField(30);
    private final JTextField txtBusca       = new JTextField(20);

    // Tabela
    private final DefaultTableModel modeloTabela = new DefaultTableModel(
            new String[]{"ID", "Nome", "CPF", "Nascimento", "Telefone"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable tabela = new JTable(modeloTabela);

    // Estado
    private int idSelecionado = 0;
    private final PacienteController ctrl = new PacienteController();
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public TelaPaciente(Frame owner) {
        super(owner, "Cadastro de Pacientes", true);
        construirUI();
        carregarTabela("");
    }

    private void construirUI() {
        setSize(820, 620);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout(10, 10));

        add(painelFormulario(), BorderLayout.NORTH);
        add(painelTabela(),     BorderLayout.CENTER);
        add(painelBotoes(),     BorderLayout.SOUTH);
    }

    // ---- Painel Formulário ----
    private JPanel painelFormulario() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBorder(BorderFactory.createTitledBorder("Dados do Paciente"));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(4, 6, 4, 6);
        g.anchor = GridBagConstraints.WEST;

        adicionarCampo(p, g, "Nome *:",       txtNome,        0, 0, 3);
        adicionarCampo(p, g, "CPF *:",        txtCpf,         0, 1, 1);
        adicionarCampo(p, g, "Nascimento *:", txtDataNasc,    2, 1, 1);
        adicionarCampo(p, g, "Telefone:",     txtTelefone,    0, 2, 1);
        adicionarCampo(p, g, "E-mail:",       txtEmail,       2, 2, 1);
        adicionarCampo(p, g, "Endereço:",     txtEndereco,    0, 3, 3);
        adicionarCampo(p, g, "Responsável:",  txtResponsavel, 0, 4, 3);

        return p;
    }

    private void adicionarCampo(JPanel p, GridBagConstraints g,
                                 String label, JTextField campo,
                                 int col, int row, int span) {
        g.gridx = col; g.gridy = row; g.gridwidth = 1;
        p.add(new JLabel(label), g);
        g.gridx = col + 1; g.gridwidth = span;
        g.fill = GridBagConstraints.HORIZONTAL;
        p.add(campo, g);
        g.fill = GridBagConstraints.NONE;
    }

    // ---- Painel Tabela ----
    private JPanel painelTabela() {
        JPanel p = new JPanel(new BorderLayout(5, 5));
        p.setBorder(BorderFactory.createTitledBorder("Lista de Pacientes"));

        JPanel buscaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buscaPanel.add(new JLabel("Buscar por nome:"));
        buscaPanel.add(txtBusca);
        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.addActionListener(e -> carregarTabela(txtBusca.getText()));
        buscaPanel.add(btnBuscar);

        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.setRowHeight(22);
        tabela.getColumnModel().getColumn(0).setMaxWidth(50);
        tabela.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) preencherFormulario();
        });

        p.add(buscaPanel, BorderLayout.NORTH);
        p.add(new JScrollPane(tabela), BorderLayout.CENTER);
        return p;
    }

    // ---- Painel Botões ----
    private JPanel painelBotoes() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 8));

        JButton btnNovo    = new JButton("Novo");
        JButton btnSalvar  = new JButton("Salvar");
        JButton btnExcluir = new JButton("Excluir");
        JButton btnFechar  = new JButton("Fechar");

        btnSalvar.setBackground(new Color(33, 97, 140));
        btnSalvar.setForeground(Color.WHITE);
        btnExcluir.setBackground(new Color(180, 50, 40));
        btnExcluir.setForeground(Color.WHITE);

        btnNovo.addActionListener(e -> limparFormulario());
        btnSalvar.addActionListener(e -> salvar());
        btnExcluir.addActionListener(e -> excluir());
        btnFechar.addActionListener(e -> dispose());

        p.add(btnNovo); p.add(btnSalvar); p.add(btnExcluir); p.add(btnFechar);
        return p;
    }

    // ---- Lógica ----
    private void salvar() {
        Paciente pac = new Paciente();
        pac.setId(idSelecionado);
        pac.setNome(txtNome.getText().trim());
        pac.setCpf(txtCpf.getText().trim());
        pac.setTelefone(txtTelefone.getText().trim());
        pac.setEmail(txtEmail.getText().trim());
        pac.setEndereco(txtEndereco.getText().trim());
        pac.setResponsavel(txtResponsavel.getText().trim());

        try {
            pac.setDataNasc(LocalDate.parse(txtDataNasc.getText().trim(), FMT));
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Data de nascimento inválida. Use dd/MM/yyyy.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (ctrl.salvar(pac, this)) {
            limparFormulario();
            carregarTabela("");
        }
    }

    private void excluir() {
        if (idSelecionado == 0) {
            JOptionPane.showMessageDialog(this, "Selecione um paciente na tabela.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (ctrl.excluir(idSelecionado, this)) {
            limparFormulario();
            carregarTabela("");
        }
    }

    private void carregarTabela(String filtro) {
        modeloTabela.setRowCount(0);
        List<Paciente> lista = filtro.isBlank()
                ? ctrl.listarTodos(this)
                : ctrl.buscarPorNome(filtro, this);
        for (Paciente p : lista) {
            modeloTabela.addRow(new Object[]{
                p.getId(),
                p.getNome(),
                ValidadorCPF.formatar(p.getCpf()),
                p.getDataNasc() != null ? p.getDataNasc().format(FMT) : "",
                p.getTelefone()
            });
        }
    }

    private void preencherFormulario() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) return;
        idSelecionado = (int) modeloTabela.getValueAt(linha, 0);
        txtNome.setText((String) modeloTabela.getValueAt(linha, 1));
        txtCpf.setText((String) modeloTabela.getValueAt(linha, 2));
        txtDataNasc.setText((String) modeloTabela.getValueAt(linha, 3));
        txtTelefone.setText((String) modeloTabela.getValueAt(linha, 4));
    }

    private void limparFormulario() {
        idSelecionado = 0;
        for (JTextField f : new JTextField[]{txtNome, txtCpf, txtDataNasc, txtTelefone,
                                              txtEmail, txtEndereco, txtResponsavel}) {
            f.setText("");
        }
        tabela.clearSelection();
    }
}
