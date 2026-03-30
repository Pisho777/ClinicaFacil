package model;

public enum StatusConsulta {
    AGENDADA("Agendada"),
    REALIZADA("Realizada"),
    CANCELADA("Cancelada");

    private final String descricao;

    StatusConsulta(String descricao) { this.descricao = descricao; }

    public String getDescricao() { return descricao; }

    @Override
    public String toString() { return descricao; }

    public static StatusConsulta fromString(String valor) {
        for (StatusConsulta s : values()) {
            if (s.name().equalsIgnoreCase(valor) || s.descricao.equalsIgnoreCase(valor)) {
                return s;
            }
        }
        throw new IllegalArgumentException("Status desconhecido: " + valor);
    }
}
