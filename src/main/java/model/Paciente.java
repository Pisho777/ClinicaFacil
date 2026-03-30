package model;

import java.time.LocalDate;

public class Paciente {

    private int id;
    private String nome;
    private String cpf;
    private LocalDate dataNasc;
    private String telefone;
    private String email;
    private String endereco;
    private String responsavel;

    public Paciente() {}

    public Paciente(int id, String nome, String cpf, LocalDate dataNasc,
                    String telefone, String email, String endereco, String responsavel) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.dataNasc = dataNasc;
        this.telefone = telefone;
        this.email = email;
        this.endereco = endereco;
        this.responsavel = responsavel;
    }

    public int getId()                         { return id; }
    public void setId(int id)                  { this.id = id; }

    public String getNome()                    { return nome; }
    public void setNome(String nome)           { this.nome = nome; }

    public String getCpf()                     { return cpf; }
    public void setCpf(String cpf)             { this.cpf = cpf; }

    public LocalDate getDataNasc()             { return dataNasc; }
    public void setDataNasc(LocalDate d)       { this.dataNasc = d; }

    public String getTelefone()                { return telefone; }
    public void setTelefone(String t)          { this.telefone = t; }

    public String getEmail()                   { return email; }
    public void setEmail(String e)             { this.email = e; }

    public String getEndereco()                { return endereco; }
    public void setEndereco(String e)          { this.endereco = e; }

    public String getResponsavel()             { return responsavel; }
    public void setResponsavel(String r)       { this.responsavel = r; }

    @Override
    public String toString() { return nome + " — CPF: " + cpf; }
}
