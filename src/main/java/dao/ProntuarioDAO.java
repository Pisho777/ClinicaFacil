package dao;

import model.Consulta;
import model.Prontuario;
import util.ConexaoDB;

import java.sql.*;

public class ProntuarioDAO {

    public void inserir(Prontuario p) throws SQLException {
        String sql = "INSERT INTO prontuarios (id_consulta, queixa, diagnostico, prescricao) VALUES (?,?,?,?)";
        try (Connection con = ConexaoDB.obterConexao();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, p.getConsulta().getId());
            ps.setString(2, p.getQueixa());
            ps.setString(3, p.getDiagnostico());
            ps.setString(4, p.getPrescricao());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) p.setId(rs.getInt(1));
            }
        }
    }

    public void atualizar(Prontuario p) throws SQLException {
        String sql = "UPDATE prontuarios SET queixa=?, diagnostico=?, prescricao=? WHERE id=?";
        try (Connection con = ConexaoDB.obterConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getQueixa());
            ps.setString(2, p.getDiagnostico());
            ps.setString(3, p.getPrescricao());
            ps.setInt(4, p.getId());
            ps.executeUpdate();
        }
    }

    public void deletar(int id) throws SQLException {
        String sql = "DELETE FROM prontuarios WHERE id=?";
        try (Connection con = ConexaoDB.obterConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public Prontuario buscarPorConsulta(int idConsulta) throws SQLException {
        String sql = "SELECT pr.*, c.id_paciente, c.id_medico, c.data_hora, c.status " +
                     "FROM prontuarios pr " +
                     "JOIN consultas c ON c.id = pr.id_consulta " +
                     "WHERE pr.id_consulta = ?";
        try (Connection con = ConexaoDB.obterConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idConsulta);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Consulta c = new Consulta();
                    c.setId(idConsulta);
                    Prontuario p = new Prontuario();
                    p.setId(rs.getInt("id"));
                    p.setConsulta(c);
                    p.setQueixa(rs.getString("queixa"));
                    p.setDiagnostico(rs.getString("diagnostico"));
                    p.setPrescricao(rs.getString("prescricao"));
                    Timestamp dt = rs.getTimestamp("data_registro");
                    if (dt != null) p.setDataRegistro(dt.toLocalDateTime());
                    return p;
                }
            }
        }
        return null;
    }
}
