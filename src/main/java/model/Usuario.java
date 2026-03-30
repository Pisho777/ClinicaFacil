package model;

public class Usuario {

    private int id;
    private String nome;
    private String login;
    private String senhaHash;
    private PerfilUsuario perfil;
    private boolean ativo;

    public Usuario() { this.ativo = true; }

    public Usuario(int id, String nome, String login, String senhaHash,
                   PerfilUsuario perfil, boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.login = login;
        this.senhaHash = senhaHash;
        this.perfil = perfil;
        this.ativo = ativo;
    }

    public int getId()                         { return id; }
    public void setId(int id)                  { this.id = id; }

    public String getNome()                    { return nome; }
    public void setNome(String nome)           { this.nome = nome; }

    public String getLogin()                   { return login; }
    public void setLogin(String login)         { this.login = login; }

    public String getSenhaHash()               { return senhaHash; }
    public void setSenhaHash(String s)         { this.senhaHash = s; }

    public PerfilUsuario getPerfil()           { return perfil; }
    public void setPerfil(PerfilUsuario p)     { this.perfil = p; }

    public boolean isAtivo()                   { return ativo; }
    public void setAtivo(boolean ativo)        { this.ativo = ativo; }

    @Override
    public String toString() { return nome + " (" + perfil.getDescricao() + ")"; }
}
