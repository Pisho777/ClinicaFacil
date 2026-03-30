package model;

public enum PerfilUsuario {
    ADMIN("Administrador"),
    RECEPCAO("Recepcionista"),
    MEDICO("Médico");

    private final String descricao;

    PerfilUsuario(String descricao) { this.descricao = descricao; }

    public String getDescricao() { return descricao; }

    @Override
    public String toString() { return descricao; }
}
