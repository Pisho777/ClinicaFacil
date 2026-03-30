package dao;

import model.*;
import util.ConexaoDB;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ConsultaDAO {

    private Consulta mapear(ResultSet rs) throws SQLException {
        Paciente pac = new Paciente();
        pac.setId(rs.getInt("id_paciente"));
        pac.setNome(rs.getString("nome_paciente"));

        Especialidade esp = new Especialidade(rs.getInt("id_especialidade"), rs.getString("nome_especialidade"), null);
        Medico med = new Medico();
        med.setId(rs.getInt("id_medico"));
        med.setNome(rs.getString("nome_medico"));
        med.setEspecialidade(esp);

        Consulta c = new Consulta();
        c.setId(rs.getInt("id"));
        c.setPaciente(pac);
        c.setMedico(med);
        c.setDataHora(rs.getTimestamp("data_hora").toLocalDateTime());
        c.setStatus(StatusConsulta.fromString(rs.getString("status")));
        c.setMotivoCancelamento(rs.getString("motivo_cancelamento"));
        Timestamp cr = rs.getTimestamp("created_at");
        if (cr != null) c.setCreatedAt(cr.toLocalDateTime());
        return c;
    }

    private static final String SELECT_BASE =
            "SELECT c.*, " +
            "p.nome AS nome_paciente, " +
            "m.nome AS nome_medico, " +
            "m.id_especialidade, " +
            "e.nome AS nome_especialidade " +
            "FROM consultas c " +
            "JOIN pacientes p ON p.id = c.id_paciente " +
            "JOIN medicos m   ON m.id = c.id_medico " +
            "JOIN especialidades e ON e.id = m.id_especialidade ";

    public void inserir(Consulta c) throws SQLException {
        String sql = "INSERT INTO consultas (id_paciente, id_medico, data_hora, status, motivo_cancelamento) VALUES (?,?,?,?,?)";
        try (Connection con = ConexaoDB.obterConexao();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, c.getPaciente().getId());
            ps.setInt(2, c.getMedico().getId());
            ps.setTimestamp(3, Timestamp.valueOf(c.getDataHora()));
            ps.setString(4, c.getStatus().name().toLowerCase());
            ps.setString(5, c.getMotivoCancelamento());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) c.setId(rs.getInt(1));
            }
        }
    }

    public void atualizar(Consulta c) throws SQLException {
        String sql = "UPDATE consultas SET id_paciente=?, id_medico=?, data_hora=?, status=?, motivo_cancelamento=? WHERE id=?";
        try (Connection con = ConexaoDB.obterConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, c.getPaciente().getId());
            ps.setInt(2, c.getMedico().getId());
            ps.setTimestamp(3, Timestamp.valueOf(c.getDataHora()));
            ps.setString(4, c.getStatus().name().toLowerCase());
            ps.setString(5, c.getMotivoCancelamento());
            ps.setInt(6, c.getId());
            ps.executeUpdate();
        }
    }

    public void deletar(int id) throws SQLException {
        String sql = "DELETE FROM consultas WHERE id=?";
        try (Connection con = ConexaoDB.obterConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public List<Consulta> listarTodas() throws SQLException {
        List<Consulta> lista = new ArrayList<>();
        try (Connection con = ConexaoDB.obterConexao();
             PreparedStatement ps = con.prepareStatement(SELECT_BASE + "ORDER BY c.data_hora DESC");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    public List<Consulta> listarPorDia(LocalDate dia) throws SQLException {
        List<Consulta> lista = new ArrayList<>();
        String sql = SELECT_BASE + "WHERE DATE(c.data_hora) = ? ORDER BY c.data_hora";
        try (Connection con = ConexaoDB.obterConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(dia));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        }
        return lista;
    }

    public List<Consulta> listarPorPaciente(int idPaciente) throws SQLException {
        List<Consulta> lista = new ArrayList<>();
        String sql = SELECT_BASE + "WHERE c.id_paciente = ? ORDER BY c.data_hora DESC";
        try (Connection con = ConexaoDB.obterConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPaciente);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        }
        return lista;
    }

    public Consulta buscarPorId(int id) throws SQLException {
        try (Connection con = ConexaoDB.obterConexao();
             PreparedStatement ps = con.prepareStatement(SELECT_BASE + "WHERE c.id=?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        }
        return null;
    }
}
