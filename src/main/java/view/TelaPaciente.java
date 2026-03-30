package view;

import Controller.PacienteController;
import model.Paciente;
import util.ValidadorCPF;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class TelaPaciente extends JPanel {

    // Campos do formulário
    private final JTextField txtNome        = DS.campo(0);
    private final JTextField txtCpf         = DS.campo(0);
    private final JTextField txtDataNasc    = DS.campo(0);
    private final JTextField txtTelefone    = DS.campo(0);
    private final JTextField txtEmail       = DS.campo(0);
    private final JTextField txtEndereco    = DS.campo(0);
    private final JTextField txtResponsavel = DS.campo(0);
    private final JTextField txtBusca       = DS.campo(18);

    // Painel do formulário (pode ser ocultado)
    private JPanel painelForm;

    // Tabela
    private final DefaultTableModel modeloTabela = new DefaultTableModel(
            new String[]{"#", "Nome", "CPF", "Nascimento", "Telefone"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable tabela = new JTable(modeloTabela);

    private int idSelecionado = 0;
    private final PacienteController ctrl = new PacienteController();
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public TelaPaciente() {
        construirUI();
        carregarTabela("");
    }

    private void construirUI() {
        this.setLayout(new BorderLayout());

        // Shell: main apenas (sidebar removida — navegação é da TelaPrincipal)
        this.add(criarMain(), BorderLayout.CENTER);
    }

    // ── Área principal ───────────────────────────────────────────
    private JPanel criarMain() {
        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(DS.NEUTRAL);

        JPanel topbar = DS.topbar("Pacientes", "Cadastro e gerenciamento");
        main.add(topbar, BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout(0, 14));
        content.setBackground(DS.NEUTRAL);
        content.setBorder(DS.emptyBorder(16, 16, 16, 16));

        // Painel de formulário (oculto por padrão)
        painelForm = criarPainelFormulario();
        painelForm.setVisible(false);

        // Toolbar + tabela
        JPanel corpo = new JPanel(new BorderLayout(0, 10));
        corpo.setOpaque(false);
        corpo.add(criarToolbar(), BorderLayout.NORTH);
        corpo.add(criarPainelTabela(), BorderLayout.CENTER);

        content.add(painelForm, BorderLayout.NORTH);
        content.add(corpo,      BorderLayout.CENTER);
        main.add(content, BorderLayout.CENTER);

        return main;
    }

    // ── Form Panel ───────────────────────────────────────────────
    private JPanel criarPainelFormulario() {
        JPanel outer = new JPanel(new BorderLayout(0, 12));
        outer.setBackground(DS.WHITE);
        outer.setBorder(BorderFactory.createCompoundBorder(
            DS.borderCard(), DS.emptyBorder(16, 18, 16, 18)));

        JLabel titulo = new JLabel("Novo Paciente");
        titulo.setFont(DS.F_HEADING); titulo.setForeground(DS.TEXT);
        titulo.setBorder(BorderFactory.createMatteBorder(0,0,1,0,DS.BORDER));
        titulo.setName("titulo_form_pac");

        JPanel grid = new JPanel(new GridBagLayout());
        grid.setOpaque(false);
        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL; g.insets = new Insets(6, 6, 6, 6);

        // Nome — largura total
        g.gridy=0; g.gridx=0; g.gridwidth=1; grid.add(DS.labelCampo("Nome completo *"), g);
        g.gridx=1; g.gridwidth=5; grid.add(txtNome, g); g.gridwidth=1;

        // CPF e Nascimento
        g.gridy=1; g.gridx=0; grid.add(DS.labelCampo("CPF *"), g);
        g.gridx=1; grid.add(txtCpf, g);
        g.gridx=2; grid.add(DS.labelCampo("Nascimento *"), g);
        g.gridx=3; grid.add(txtDataNasc, g);

        // Telefone e E-mail
        g.gridy=2; g.gridx=0; grid.add(DS.labelCampo("Telefone"), g);
        g.gridx=1; grid.add(txtTelefone, g);
        g.gridx=2; grid.add(DS.labelCampo("E-mail"), g);
        g.gridx=3; grid.add(txtEmail, g);

        // Endereço — largura total
        g.gridy=3; g.gridx=0; g.gridwidth=1; grid.add(DS.labelCampo("Endereço"), g);
        g.gridx=1; g.gridwidth=5; grid.add(txtEndereco, g); g.gridwidth=1;

        // Responsável — largura total
        g.gridy=4; g.gridx=0; g.gridwidth=1; grid.add(DS.labelCampo("Responsável"), g);
        g.gridx=1; g.gridwidth=5; grid.add(txtResponsavel, g); g.gridwidth=1;

        // Hint responsável
        JLabel hint = new JLabel("⚠  Obrigatório para pacientes menores de 18 anos");
        hint.setFont(DS.F_SMALL); hint.setForeground(DS.WARN);
        g.gridy=5; g.gridx=1; g.gridwidth=5; grid.add(hint, g);

        // Ações
        JPanel acoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        acoes.setOpaque(false);
        JButton btnCancelar = DS.btnOutline("Cancelar");
        JButton btnSalvar   = DS.btnBrand("Salvar paciente");
        btnCancelar.addActionListener(e -> { painelForm.setVisible(false); limparFormulario(); });
        btnSalvar.addActionListener(e -> salvar());
        acoes.add(btnCancelar); acoes.add(btnSalvar);

        outer.add(titulo, BorderLayout.NORTH);
        outer.add(grid,   BorderLayout.CENTER);
        outer.add(acoes,  BorderLayout.SOUTH);
        return outer;
    }

    // ── Toolbar ─────────────────────────────────────────────────
    private JPanel criarToolbar() {
        JPanel p = new JPanel(new BorderLayout(8, 0));
        p.setOpaque(false);

        JPanel esq = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        esq.setOpaque(false);
        txtBusca.putClientProperty("JTextField.placeholderText", "Buscar por nome ou CPF...");
        JButton btnBuscar = DS.btnOutline("Filtrar");
        JButton btnTodos  = DS.btnOutline("Todos");
        btnBuscar.addActionListener(e -> carregarTabela(txtBusca.getText()));
        btnTodos.addActionListener(e -> { txtBusca.setText(""); carregarTabela(""); });
        esq.add(txtBusca); esq.add(btnBuscar); esq.add(btnTodos);

        JButton btnNovo = DS.btnBrand("+ Novo Paciente");
        btnNovo.addActionListener(e -> {
            limparFormulario();
            setTituloForm("Novo Paciente");
            painelForm.setVisible(true);
        });

        p.add(esq,    BorderLayout.WEST);
        p.add(btnNovo, BorderLayout.EAST);
        return p;
    }

    // ── Tabela ───────────────────────────────────────────────────
    private JPanel criarPainelTabela() {
        DS.estilizarTabela(tabela);
        tabela.getColumnModel().getColumn(0).setMaxWidth(50);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) preencherFormulario();
        });

        // Renderer para colorir linha selecionada
        tabela.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean foc, int r, int c) {
                Component comp = super.getTableCellRendererComponent(t, v, sel, foc, r, c);
                setBorder(DS.emptyBorder(0, 12, 0, 12));
                if (sel) { comp.setBackground(DS.BRAND_LIGHT); comp.setForeground(DS.TEXT); }
                else     { comp.setBackground(r % 2 == 0 ? DS.WHITE : DS.NEUTRAL); comp.setForeground(DS.TEXT); }
                return comp;
            }
        });

        // Coluna de ações
        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBorder(DS.borderCard());
        scroll.getViewport().setBackground(DS.WHITE);

        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setOpaque(false);
        wrap.add(scroll, BorderLayout.CENTER);

        // Rodapé contador
        JLabel contador = DS.labelMuted("— pacientes carregados");
        contador.setName("contador_pac");
        contador.setBorder(DS.emptyBorder(6, 0, 0, 0));
        wrap.add(contador, BorderLayout.SOUTH);

        return wrap;
    }

    // ── Lógica ──────────────────────────────────────────────────
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
            painelForm.setVisible(false);
            limparFormulario();
            carregarTabela("");
        }
    }

    private void excluirSelecionado() {
        if (idSelecionado == 0) {
            JOptionPane.showMessageDialog(this, "Selecione um paciente na tabela.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (ctrl.excluir(idSelecionado, this)) {
            painelForm.setVisible(false);
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
            String nasc = p.getDataNasc() != null ? p.getDataNasc().format(FMT) : "";
            int idade = p.getDataNasc() != null ? Period.between(p.getDataNasc(), LocalDate.now()).getYears() : -1;
            String nascExib = idade >= 0 ? nasc + " (" + idade + " anos)" : nasc;
            if (idade >= 0 && idade < 18) nascExib += " ⚠";
            modeloTabela.addRow(new Object[]{
                p.getId(), p.getNome(), ValidadorCPF.formatar(p.getCpf()), nascExib, p.getTelefone()
            });
        }
        // Atualiza contador
        for (Component c : ((JPanel) ((JScrollPane) tabela.getParent().getParent()).getParent()).getComponents()) {
            if (c instanceof JLabel l && "contador_pac".equals(l.getName()))
                l.setText(lista.size() + " paciente(s) encontrado(s)");
        }
    }

    private void preencherFormulario() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) return;
        idSelecionado = (int) modeloTabela.getValueAt(linha, 0);
        txtNome.setText((String) modeloTabela.getValueAt(linha, 1));
        txtCpf.setText((String) modeloTabela.getValueAt(linha, 2));
        // Extrai só a data (dd/MM/yyyy) sem a parte " (X anos)"
        String nascRaw = (String) modeloTabela.getValueAt(linha, 3);
        txtDataNasc.setText(nascRaw.length() >= 10 ? nascRaw.substring(0, 10) : nascRaw);
        txtTelefone.setText((String) modeloTabela.getValueAt(linha, 4));
        setTituloForm("Editar Paciente");
        painelForm.setVisible(true);
    }

    private void limparFormulario() {
        idSelecionado = 0;
        for (JTextField f : new JTextField[]{txtNome, txtCpf, txtDataNasc,
                txtTelefone, txtEmail, txtEndereco, txtResponsavel}) f.setText("");
        tabela.clearSelection();
    }

    private void setTituloForm(String texto) {
        for (Component c : painelForm.getComponents()) {
            if (c instanceof JLabel l && "titulo_form_pac".equals(l.getName()))
                l.setText(texto);
        }
    }
}
