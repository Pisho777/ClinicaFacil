package dao;

import model.Especialidade;
import util.ConexaoDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EspecialidadeDAO {

    public void inserir(Especialidade e) throws SQLException {
        String sql = "INSERT INTO especialidades (nome, descricao) VALUES (?, ?)";
        try (Connection con = ConexaoDB.obterConexao();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, e.getNome());
            ps.setString(2, e.getDescricao());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) e.setId(rs.getInt(1));
            }
        }
    }

    public void atualizar(Especialidade e) throws SQLException {
        String sql = "UPDATE especialidades SET nome=?, descricao=? WHERE id=?";
        try (Connection con = ConexaoDB.obterConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, e.getNome());
            ps.setString(2, e.getDescricao());
            ps.setInt(3, e.getId());
            ps.executeUpdate();
        }
    }

    public void deletar(int id) throws SQLException {
        String sql = "DELETE FROM especialidades WHERE id=?";
        try (Connection con = ConexaoDB.obterConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public List<Especialidade> listarTodas() throws SQLException {
        List<Especialidade> lista = new ArrayList<>();
        String sql = "SELECT id, nome, descricao FROM especialidades ORDER BY nome";
        try (Connection con = ConexaoDB.obterConexao();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Especialidade(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("descricao")));
            }
        }
        return lista;
    }

    public Especialidade buscarPorId(int id) throws SQLException {
        String sql = "SELECT id, nome, descricao FROM especialidades WHERE id=?";
        try (Connection con = ConexaoDB.obterConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Especialidade(
                            rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getString("descricao"));
                }
            }
        }
        return null;
    }
}
