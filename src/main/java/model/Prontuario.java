package model;

import java.time.LocalDateTime;

public class Prontuario {

    private int id;
    private Consulta consulta;
    private String queixa;
    private String diagnostico;
    private String prescricao;
    private LocalDateTime dataRegistro;

    public Prontuario() {}

    public Prontuario(int id, Consulta consulta, String queixa,
                      String diagnostico, String prescricao,
                      LocalDateTime dataRegistro) {
        this.id = id;
        this.consulta = consulta;
        this.queixa = queixa;
        this.diagnostico = diagnostico;
        this.prescricao = prescricao;
        this.dataRegistro = dataRegistro;
    }

    public int getId()                          { return id; }
    public void setId(int id)                   { this.id = id; }

    public Consulta getConsulta()               { return consulta; }
    public void setConsulta(Consulta c)         { this.consulta = c; }

    public String getQueixa()                   { return queixa; }
    public void setQueixa(String q)             { this.queixa = q; }

    public String getDiagnostico()              { return diagnostico; }
    public void setDiagnostico(String d)        { this.diagnostico = d; }

    public String getPrescricao()               { return prescricao; }
    public void setPrescricao(String p)         { this.prescricao = p; }

    public LocalDateTime getDataRegistro()      { return dataRegistro; }
    public void setDataRegistro(LocalDateTime d){ this.dataRegistro = d; }
}
