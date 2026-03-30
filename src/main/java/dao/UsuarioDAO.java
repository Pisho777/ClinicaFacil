package dao;

import model.PerfilUsuario;
import model.Usuario;
import util.ConexaoDB;

import java.sql.*;

public class UsuarioDAO {

    private Usuario mapear(ResultSet rs) throws SQLException {
        Usuario u = new Usuario();
        u.setId(rs.getInt("id"));
        u.setNome(rs.getString("nome"));
        u.setLogin(rs.getString("login"));
        u.setSenhaHash(rs.getString("senha_hash"));
        u.setPerfil(PerfilUsuario.valueOf(rs.getString("perfil").toUpperCase()));
        u.setAtivo(rs.getBoolean("ativo"));
        return u;
    }

    public void inserir(Usuario u) throws SQLException {
        String sql = "INSERT INTO usuarios (nome, login, senha_hash, perfil, ativo) VALUES (?,?,?,?,?)";
        try (Connection con = ConexaoDB.obterConexao();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, u.getNome());
            ps.setString(2, u.getLogin());
            ps.setString(3, u.getSenhaHash());
            ps.setString(4, u.getPerfil().name().toLowerCase());
            ps.setBoolean(5, u.isAtivo());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) u.setId(rs.getInt(1));
            }
        }
    }

    public void atualizar(Usuario u) throws SQLException {
        String sql = "UPDATE usuarios SET nome=?, login=?, senha_hash=?, perfil=?, ativo=? WHERE id=?";
        try (Connection con = ConexaoDB.obterConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, u.getNome());
            ps.setString(2, u.getLogin());
            ps.setString(3, u.getSenhaHash());
            ps.setString(4, u.getPerfil().name().toLowerCase());
            ps.setBoolean(5, u.isAtivo());
            ps.setInt(6, u.getId());
            ps.executeUpdate();
        }
    }

    /** Autentica pelo login e senha (comparação de hash simples SHA-256). */
    public Usuario autenticar(String login, String senhaHash) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE login=? AND senha_hash=? AND ativo=1";
        try (Connection con = ConexaoDB.obterConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, login);
            ps.setString(2, senhaHash);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        }
        return null;
    }

    public boolean loginExiste(String login) throws SQLException {
        String sql = "SELECT COUNT(*) FROM usuarios WHERE login=?";
        try (Connection con = ConexaoDB.obterConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, login);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        }
        return false;
    }
}
