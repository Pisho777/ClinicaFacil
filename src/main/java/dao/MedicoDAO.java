package dao;

import model.Especialidade;
import model.Medico;
import util.ConexaoDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedicoDAO {

    private Medico mapear(ResultSet rs) throws SQLException {
        Especialidade esp = new Especialidade(
                rs.getInt("id_especialidade"),
                rs.getString("nome_especialidade"),
                null);
        Medico m = new Medico();
        m.setId(rs.getInt("id"));
        m.setNome(rs.getString("nome"));
        m.setCrm(rs.getString("crm"));
        m.setUfCrm(rs.getString("uf_crm"));
        m.setEspecialidade(esp);
        m.setTelefone(rs.getString("telefone"));
        m.setAtivo(rs.getBoolean("ativo"));
        return m;
    }

    private static final String SELECT_BASE =
            "SELECT m.*, e.nome AS nome_especialidade " +
            "FROM medicos m " +
            "JOIN especialidades e ON e.id = m.id_especialidade ";

    public void inserir(Medico m) throws SQLException {
        String sql = "INSERT INTO medicos (nome, crm, uf_crm, id_especialidade, telefone, ativo) VALUES (?,?,?,?,?,?)";
        try (Connection con = ConexaoDB.obterConexao();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, m.getNome());
            ps.setString(2, m.getCrm());
            ps.setString(3, m.getUfCrm());
            ps.setInt(4, m.getEspecialidade().getId());
            ps.setString(5, m.getTelefone());
            ps.setBoolean(6, m.isAtivo());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) m.setId(rs.getInt(1));
            }
        }
    }

    public void atualizar(Medico m) throws SQLException {
        String sql = "UPDATE medicos SET nome=?, crm=?, uf_crm=?, id_especialidade=?, telefone=?, ativo=? WHERE id=?";
        try (Connection con = ConexaoDB.obterConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, m.getNome());
            ps.setString(2, m.getCrm());
            ps.setString(3, m.getUfCrm());
            ps.setInt(4, m.getEspecialidade().getId());
            ps.setString(5, m.getTelefone());
            ps.setBoolean(6, m.isAtivo());
            ps.setInt(7, m.getId());
            ps.executeUpdate();
        }
    }

    public void deletar(int id) throws SQLException {
        String sql = "DELETE FROM medicos WHERE id=?";
        try (Connection con = ConexaoDB.obterConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public List<Medico> listarTodos() throws SQLException {
        List<Medico> lista = new ArrayList<>();
        try (Connection con = ConexaoDB.obterConexao();
             PreparedStatement ps = con.prepareStatement(SELECT_BASE + "ORDER BY m.nome");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    public List<Medico> listarAtivos() throws SQLException {
        List<Medico> lista = new ArrayList<>();
        try (Connection con = ConexaoDB.obterConexao();
             PreparedStatement ps = con.prepareStatement(SELECT_BASE + "WHERE m.ativo = 1 ORDER BY m.nome");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    public Medico buscarPorId(int id) throws SQLException {
        try (Connection con = ConexaoDB.obterConexao();
             PreparedStatement ps = con.prepareStatement(SELECT_BASE + "WHERE m.id=?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        }
        return null;
    }

    public Medico buscarPorCrm(String crm, String uf) throws SQLException {
        try (Connection con = ConexaoDB.obterConexao();
             PreparedStatement ps = con.prepareStatement(SELECT_BASE + "WHERE m.crm=? AND m.uf_crm=?")) {
            ps.setString(1, crm);
            ps.setString(2, uf);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        }
        return null;
    }
}
