package model;

import java.time.LocalDateTime;

public class Consulta {

    private int id;
    private Paciente paciente;
    private Medico medico;
    private LocalDateTime dataHora;
    private StatusConsulta status;
    private String motivoCancelamento;
    private LocalDateTime createdAt;

    public Consulta() { this.status = StatusConsulta.AGENDADA; }

    public Consulta(int id, Paciente paciente, Medico medico,
                    LocalDateTime dataHora, StatusConsulta status,
                    String motivoCancelamento, LocalDateTime createdAt) {
        this.id = id;
        this.paciente = paciente;
        this.medico = medico;
        this.dataHora = dataHora;
        this.status = status;
        this.motivoCancelamento = motivoCancelamento;
        this.createdAt = createdAt;
    }

    public int getId()                              { return id; }
    public void setId(int id)                       { this.id = id; }

    public Paciente getPaciente()                   { return paciente; }
    public void setPaciente(Paciente p)             { this.paciente = p; }

    public Medico getMedico()                       { return medico; }
    public void setMedico(Medico m)                 { this.medico = m; }

    public LocalDateTime getDataHora()              { return dataHora; }
    public void setDataHora(LocalDateTime d)        { this.dataHora = d; }

    public StatusConsulta getStatus()               { return status; }
    public void setStatus(StatusConsulta s)         { this.status = s; }

    public String getMotivoCancelamento()           { return motivoCancelamento; }
    public void setMotivoCancelamento(String m)     { this.motivoCancelamento = m; }

    public LocalDateTime getCreatedAt()             { return createdAt; }
    public void setCreatedAt(LocalDateTime c)       { this.createdAt = c; }
}
