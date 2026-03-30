package model;

public class Medico {

    private int id;
    private String nome;
    private String crm;
    private String ufCrm;
    private Especialidade especialidade;
    private String telefone;
    private boolean ativo;

    public Medico() { this.ativo = true; }

    public Medico(int id, String nome, String crm, String ufCrm,
                  Especialidade especialidade, String telefone, boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.crm = crm;
        this.ufCrm = ufCrm;
        this.especialidade = especialidade;
        this.telefone = telefone;
        this.ativo = ativo;
    }

    public int getId()                             { return id; }
    public void setId(int id)                      { this.id = id; }

    public String getNome()                        { return nome; }
    public void setNome(String nome)               { this.nome = nome; }

    public String getCrm()                         { return crm; }
    public void setCrm(String crm)                 { this.crm = crm; }

    public String getUfCrm()                       { return ufCrm; }
    public void setUfCrm(String ufCrm)             { this.ufCrm = ufCrm; }

    public Especialidade getEspecialidade()        { return especialidade; }
    public void setEspecialidade(Especialidade e)  { this.especialidade = e; }

    public String getTelefone()                    { return telefone; }
    public void setTelefone(String telefone)       { this.telefone = telefone; }

    public boolean isAtivo()                       { return ativo; }
    public void setAtivo(boolean ativo)            { this.ativo = ativo; }

    @Override
    public String toString() {
        return "Dr(a). " + nome + " — CRM: " + crm + "/" + ufCrm;
    }
}
