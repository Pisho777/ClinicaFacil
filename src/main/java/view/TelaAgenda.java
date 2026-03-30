package view;

import Controller.AgendaController;
import Controller.PacienteController;
import Controller.MedicoController;
import model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class TelaAgenda extends JDialog {

    private final JTextField txtData     = new JTextField(10);
    private final JComboBox<Paciente> cmbPaciente = new JComboBox<>();
    private final JComboBox<Medico>   cmbMedico   = new JComboBox<>();
    private final JTextField txtHora     = new JTextField(6);

    private final DefaultTableModel modeloTabela = new DefaultTableModel(
            new String[]{"ID", "Data/Hora", "Paciente", "Médico", "Status"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable tabela = new JTable(modeloTabela);

    private final AgendaController   agendaCtrl   = new AgendaController();
    private final PacienteController pacCtrl      = new PacienteController();
    private final MedicoController   medCtrl      = new MedicoController();

    private static final DateTimeFormatter FMT_DT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final DateTimeFormatter FMT_D  = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public TelaAgenda(Frame owner) {
        super(owner, "Agenda de Consultas", true);
        carregarCombos();
        construirUI();
        carregarTodasConsultas();
    }

    private void carregarCombos() {
        for (Paciente p : pacCtrl.listarTodos(this)) cmbPaciente.addItem(p);
        for (Medico m   : medCtrl.listarAtivos(this)) cmbMedico.addItem(m);
    }

    private void construirUI() {
        setSize(880, 620);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout(10, 10));

        // Painel filtro/agendamento
        JPanel painelTopo = new JPanel(new GridBagLayout());
        painelTopo.setBorder(BorderFactory.createTitledBorder("Novo Agendamento"));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(5, 8, 5, 8);
        g.anchor = GridBagConstraints.WEST;

        g.gridx=0; g.gridy=0; painelTopo.add(new JLabel("Paciente *:"), g);
        g.gridx=1; g.fill=GridBagConstraints.HORIZONTAL; g.weightx=1;
        painelTopo.add(cmbPaciente, g);
        g.fill=GridBagConstraints.NONE; g.weightx=0;

        g.gridx=2; painelTopo.add(new JLabel("Médico *:"), g);
        g.gridx=3; g.fill=GridBagConstraints.HORIZONTAL; g.weightx=1;
        painelTopo.add(cmbMedico, g);
        g.fill=GridBagConstraints.NONE; g.weightx=0;

        g.gridx=0; g.gridy=1; painelTopo.add(new JLabel("Data (dd/MM/yyyy) *:"), g);
        g.gridx=1; painelTopo.add(txtData, g);

        g.gridx=2; painelTopo.add(new JLabel("Hora (HH:mm) *:"), g);
        g.gridx=3; painelTopo.add(txtHora, g);

        add(painelTopo, BorderLayout.NORTH);

        // Tabela
        JPanel painelTabela = new JPanel(new BorderLayout(5, 5));
        painelTabela.setBorder(BorderFactory.createTitledBorder("Consultas"));

        JPanel filtroPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField txtFiltroData = new JTextField(10);
        JButton btnFiltrar = new JButton("Filtrar por data");
        JButton btnTodas   = new JButton("Todas");
        filtroPanel.add(new JLabel("Filtrar (dd/MM/yyyy):"));
        filtroPanel.add(txtFiltroData);
        filtroPanel.add(btnFiltrar);
        filtroPanel.add(btnTodas);
        btnFiltrar.addActionListener(e -> filtrarPorData(txtFiltroData.getText()));
        btnTodas.addActionListener(e -> carregarTodasConsultas());

        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.setRowHeight(22);
        tabela.getColumnModel().getColumn(0).setMaxWidth(50);

        painelTabela.add(filtroPanel, BorderLayout.NORTH);
        painelTabela.add(new JScrollPane(tabela), BorderLayout.CENTER);
        add(painelTabela, BorderLayout.CENTER);

        // Botões
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 8));
        JButton btnAgendar   = new JButton("Agendar");
        JButton btnCancelar  = new JButton("Cancelar Consulta");
        JButton btnProntuario = new JButton("Prontuário");
        JButton btnFechar    = new JButton("Fechar");

        btnAgendar.setBackground(new Color(33, 97, 140));   btnAgendar.setForeground(Color.WHITE);
        btnCancelar.setBackground(new Color(180, 50, 40));  btnCancelar.setForeground(Color.WHITE);
        btnProntuario.setBackground(new Color(40, 130, 70)); btnProntuario.setForeground(Color.WHITE);

        btnAgendar.addActionListener(e -> agendar());
        btnCancelar.addActionListener(e -> cancelar());
        btnProntuario.addActionListener(e -> abrirProntuario());
        btnFechar.addActionListener(e -> dispose());

        painelBotoes.add(btnAgendar); painelBotoes.add(btnCancelar);
        painelBotoes.add(btnProntuario); painelBotoes.add(btnFechar);
        add(painelBotoes, BorderLayout.SOUTH);
    }

    private void agendar() {
        Paciente pac = (Paciente) cmbPaciente.getSelectedItem();
        Medico med   = (Medico)   cmbMedico.getSelectedItem();
        if (pac == null || med == null) {
            JOptionPane.showMessageDialog(this, "Selecione paciente e médico.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }
        LocalDateTime dt;
        try {
            String dataHoraStr = txtData.getText().trim() + " " + txtHora.getText().trim();
            dt = LocalDateTime.parse(dataHoraStr, FMT_DT);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Data/hora inválidas. Use dd/MM/yyyy e HH:mm.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Consulta c = new Consulta();
        c.setPaciente(pac);
        c.setMedico(med);
        c.setDataHora(dt);

        if (agendaCtrl.agendarConsulta(c, this)) {
            txtData.setText(""); txtHora.setText("");
            carregarTodasConsultas();
        }
    }

    private void cancelar() {
        int row = tabela.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Selecione uma consulta."); return; }
        int id = (int) modeloTabela.getValueAt(row, 0);
        String motivo = JOptionPane.showInputDialog(this, "Informe o motivo do cancelamento:");
        if (motivo == null) return;

        Consulta c = new Consulta();
        c.setId(id);
        if (agendaCtrl.cancelarConsulta(c, motivo, this)) carregarTodasConsultas();
    }

    private void abrirProntuario() {
        int row = tabela.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Selecione uma consulta."); return; }
        int idConsulta = (int) modeloTabela.getValueAt(row, 0);

        // Monta objeto consulta completo a partir dos dados da tabela
        Consulta c = new Consulta();
        c.setId(idConsulta);
        Paciente pac = new Paciente(); pac.setNome((String) modeloTabela.getValueAt(row, 2));
        Medico med = new Medico();   med.setNome((String) modeloTabela.getValueAt(row, 3));
        c.setPaciente(pac); c.setMedico(med);

        new TelaProntuario(this, c, agendaCtrl).setVisible(true);
        carregarTodasConsultas();
    }

    private void carregarTodasConsultas() {
        preencherTabela(agendaCtrl.listarTodas(this));
    }

    private void filtrarPorData(String dataStr) {
        try {
            LocalDate dia = LocalDate.parse(dataStr.trim(), FMT_D);
            preencherTabela(agendaCtrl.listarPorDia(dia, this));
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Data inválida. Use dd/MM/yyyy.", "Atenção", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void preencherTabela(List<Consulta> lista) {
        modeloTabela.setRowCount(0);
        for (Consulta c : lista) {
            modeloTabela.addRow(new Object[]{
                c.getId(),
                c.getDataHora().format(FMT_DT),
                c.getPaciente().getNome(),
                c.getMedico().getNome(),
                c.getStatus().getDescricao()
            });
        }
    }
}
