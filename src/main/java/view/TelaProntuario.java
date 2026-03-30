package view;

import Controller.AgendaController;
import model.Consulta;
import model.Prontuario;

import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;

public class TelaProntuario extends JDialog {

    private final JTextArea txtQueixa      = new JTextArea(4, 40);
    private final JTextArea txtDiagnostico = new JTextArea(4, 40);
    private final JTextArea txtPrescricao  = new JTextArea(4, 40);

    private final Consulta consulta;
    private final AgendaController ctrl;
    private Prontuario prontuario;

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public TelaProntuario(Dialog owner, Consulta consulta, AgendaController ctrl) {
        super(owner, "Prontuário da Consulta", true);
        this.consulta = consulta;
        this.ctrl     = ctrl;
        carregarProntuario();
        construirUI();
    }

    private void carregarProntuario() {
        prontuario = ctrl.buscarProntuario(consulta.getId(), this);
    }

    private void construirUI() {
        setSize(600, 540);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout(10, 10));

        // Cabeçalho info consulta
        JPanel info = new JPanel(new GridLayout(2, 2, 6, 4));
        info.setBorder(BorderFactory.createTitledBorder("Consulta"));
        info.add(new JLabel("Paciente:"));
        info.add(new JLabel(consulta.getPaciente().getNome()));
        info.add(new JLabel("Médico:"));
        info.add(new JLabel(consulta.getMedico().getNome()));
        add(info, BorderLayout.NORTH);

        // Formulário
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("Dados do Prontuário"));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6, 8, 6, 8);
        g.anchor = GridBagConstraints.NORTHWEST;
        g.fill   = GridBagConstraints.BOTH;
        g.weightx = 1;

        g.gridx=0; g.gridy=0; g.weighty=0.3; form.add(rotularArea("Queixa *:", txtQueixa), g);
        g.gridy=1;                             form.add(rotularArea("Diagnóstico:", txtDiagnostico), g);
        g.gridy=2;                             form.add(rotularArea("Prescrição:", txtPrescricao), g);

        add(form, BorderLayout.CENTER);

        // Preenche se já existir
        if (prontuario != null) {
            txtQueixa.setText(prontuario.getQueixa());
            txtDiagnostico.setText(prontuario.getDiagnostico());
            txtPrescricao.setText(prontuario.getPrescricao());
        }

        // Botões
        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 8));
        JButton btnSalvar = new JButton("Salvar Prontuário");
        JButton btnFechar = new JButton("Fechar");
        btnSalvar.setBackground(new Color(40, 130, 70)); btnSalvar.setForeground(Color.WHITE);
        btnSalvar.addActionListener(e -> salvar());
        btnFechar.addActionListener(e -> dispose());
        botoes.add(btnSalvar); botoes.add(btnFechar);
        add(botoes, BorderLayout.SOUTH);
    }

    private JPanel rotularArea(String label, JTextArea area) {
        JPanel p = new JPanel(new BorderLayout(4, 4));
        area.setLineWrap(true); area.setWrapStyleWord(true);
        p.add(new JLabel(label), BorderLayout.NORTH);
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
