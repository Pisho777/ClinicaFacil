package view;

import Controller.AgendaController;
import model.Consulta;
import model.Prontuario;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TelaProntuario extends JDialog {

    private final JTextArea txtQueixa      = new JTextArea(4, 0);
    private final JTextArea txtDiagnostico = new JTextArea(4, 0);
    private final JTextArea txtPrescricao  = new JTextArea(4, 0);

    private final Consulta consulta;
    private final AgendaController ctrl;
    private Prontuario prontuario;

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public TelaProntuario(Dialog owner, Consulta consulta, AgendaController ctrl) {
        super(owner, "Prontuário", true);
        this.consulta = consulta;
        this.ctrl     = ctrl;
        carregarProntuario();
        construirUI();
    }

    private void carregarProntuario() {
        prontuario = ctrl.buscarProntuario(consulta.getId(), this);
    }

    private void construirUI() {
        setSize(680, 580);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout());
        getContentPane().setBackground(DS.NEUTRAL);

        // Topbar
        JPanel topbar = DS.topbar("Prontuário",
                "Consulta #" + consulta.getId());
        add(topbar, BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout(0, 14));
        content.setBackground(DS.NEUTRAL);
        content.setBorder(DS.emptyBorder(16, 16, 16, 16));

        // Card info da consulta
        content.add(criarCardInfo(), BorderLayout.NORTH);
        content.add(criarCardForm(), BorderLayout.CENTER);
        add(content, BorderLayout.CENTER);

        // Botões
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        footer.setBackground(DS.WHITE);
        footer.setBorder(BorderFactory.createMatteBorder(1,0,0,0,DS.BORDER));
        JButton btnCancelar = DS.btnOutline("Fechar");
        JButton btnSalvar   = DS.btnSuccess("Salvar Prontuário");
        btnCancelar.addActionListener(e -> dispose());
        btnSalvar.addActionListener(e -> salvar());
        footer.add(btnCancelar); footer.add(btnSalvar);
        add(footer, BorderLayout.SOUTH);
    }

    private JPanel criarCardInfo() {
        JPanel c = DS.card();
        c.setLayout(new GridLayout(1, 3, 16, 0));

        c.add(infoItem("Paciente", consulta.getPaciente().getNome()));
        c.add(infoItem("Médico",   "Dr(a). " + consulta.getMedico().getNome()));
        c.add(infoItem("Data/Hora",
                consulta.getDataHora() != null ? consulta.getDataHora().format(FMT)
                        : LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
        return c;
    }

    private JPanel infoItem(String label, String valor) {
        JPanel p = new JPanel(new GridLayout(2, 1, 0, 4));
        p.setOpaque(false);
        JLabel lbl = DS.labelCampo(label);
        JLabel val = new JLabel(valor);
        val.setFont(new Font("Segoe UI", Font.BOLD, 13)); val.setForeground(DS.TEXT);
        p.add(lbl); p.add(val);
        return p;
    }

    private JPanel criarCardForm() {
        JPanel outer = DS.card();
        outer.setLayout(new BorderLayout(0, 14));

        JLabel titulo = new JLabel("Registro Clínico");
        titulo.setFont(DS.F_HEADING); titulo.setForeground(DS.TEXT);
        titulo.setBorder(BorderFactory.createMatteBorder(0,0,1,0,DS.BORDER));

        JPanel campos = new JPanel(new GridBagLayout());
        campos.setOpaque(false);
        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.BOTH; g.insets = new Insets(6,0,6,0);
        g.weightx = 1;

        for (JTextArea area : new JTextArea[]{txtQueixa, txtDiagnostico, txtPrescricao}) {
            area.setFont(DS.F_BODY);
            area.setBackground(DS.NEUTRAL);
            area.setForeground(DS.TEXT);
            area.setLineWrap(true); area.setWrapStyleWord(true);
            area.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(DS.BORDER, 1, true),
                DS.emptyBorder(8, 10, 8, 10)));
        }

        g.gridy=0; g.gridx=0; g.weighty=0.33; campos.add(areaComLabel("Queixa Principal *", txtQueixa), g);
        g.gridy=1;                              campos.add(areaComLabel("Diagnóstico",        txtDiagnostico), g);
        g.gridy=2;                              campos.add(areaComLabel("Prescrição",          txtPrescricao), g);

        // Preenche se já existe
        if (prontuario != null) {
            txtQueixa.setText(prontuario.getQueixa());
            txtDiagnostico.setText(prontuario.getDiagnostico());
            txtPrescricao.setText(prontuario.getPrescricao());
        }

        outer.add(titulo, BorderLayout.NORTH);
        outer.add(campos, BorderLayout.CENTER);
        return outer;
    }

    private JPanel areaComLabel(String label, JTextArea area) {
        JPanel p = new JPanel(new BorderLayout(0, 4));
        p.setOpaque(false);
        p.add(DS.labelCampo(label), BorderLayout.NORTH);
        p.add(new JScrollPane(area), BorderLayout.CENTER);
        return p;
    }

    private void salvar() {
        if (prontuario == null) prontuario = new Prontuario();
        prontuario.setConsulta(consulta);
        prontuario.setQueixa(txtQueixa.getText().trim());
        prontuario.setDiagnostico(txtDiagnostico.getText().trim());
        prontuario.setPrescricao(txtPrescricao.getText().trim());
        if (ctrl.salvarProntuario(prontuario, this)) dispose();
    }
}
