package dao;

import model.Paciente;
import util.ConexaoDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PacienteDAO {

    private Paciente mapear(ResultSet rs) throws SQLException {
        Paciente p = new Paciente();
        p.setId(rs.getInt("id"));
        p.setNome(rs.getString("nome"));
        p.setCpf(rs.getString("cpf"));
        p.setDataNasc(rs.getDate("data_nasc").toLocalDate());
        p.setTelefone(rs.getString("telefone"));
        p.setEmail(rs.getString("email"));
        p.setEndereco(rs.getString("endereco"));
        p.setResponsavel(rs.getString("responsavel"));
        return p;
    }

    public void inserir(Paciente p) throws SQLException {
        String sql = "INSERT INTO pacientes (nome, cpf, data_nasc, telefone, email, endereco, responsavel) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = ConexaoDB.obterConexao();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, p.getNome());
            ps.setString(2, p.getCpf());
            ps.setDate(3, Date.valueOf(p.getDataNasc()));
            ps.setString(4, p.getTelefone());
            ps.setString(5, p.getEmail());
            ps.setString(6, p.getEndereco());
            ps.setString(7, p.getResponsavel());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) p.setId(rs.getInt(1));
            }
        }
    }

    public void atualizar(Paciente p) throws SQLException {
        String sql = "UPDATE pacientes SET nome=?, cpf=?, data_nasc=?, telefone=?, email=?, endereco=?, responsavel=? WHERE id=?";
        try (Connection con = ConexaoDB.obterConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getNome());
            ps.setString(2, p.getCpf());
            ps.setDate(3, Date.valueOf(p.getDataNasc()));
            ps.setString(4, p.getTelefone());
            ps.setString(5, p.getEmail());
            ps.setString(6, p.getEndereco());
            ps.setString(7, p.getResponsavel());
            ps.setInt(8, p.getId());
            ps.executeUpdate();
        }
    }

    public void deletar(int id) throws SQLException {
        String sql = "DELETE FROM pacientes WHERE id=?";
        try (Connection con = ConexaoDB.obterConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public List<Paciente> listarTodos() throws SQLException {
        List<Paciente> lista = new ArrayList<>();
        String sql = "SELECT * FROM pacientes ORDER BY nome";
        try (Connection con = ConexaoDB.obterConexao();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    public List<Paciente> buscarPorNome(String nome) throws SQLException {
        List<Paciente> lista = new ArrayList<>();
        String sql = "SELECT * FROM pacientes WHERE nome LIKE ? ORDER BY nome";
        try (Connection con = ConexaoDB.obterConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + nome + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        }
        return lista;
    }

    public Paciente buscarPorCpf(String cpf) throws SQLException {
        String sql = "SELECT * FROM pacientes WHERE cpf=?";
        try (Connection con = ConexaoDB.obterConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, cpf);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        }
        return null;
    }

    public Paciente buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM pacientes WHERE id=?";
        try (Connection con = ConexaoDB.obterConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        }
        return null;
    }
}
