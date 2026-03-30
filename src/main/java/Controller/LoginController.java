package Controller;

import dao.UsuarioDAO;
import model.Usuario;
import util.Mensagem;

import java.awt.Component;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class LoginController {

    private final UsuarioDAO dao = new UsuarioDAO();
    private static Usuario usuarioLogado;

    /** Tenta autenticar e retorna o Usuario ou null. */
    public Usuario autenticar(String login, String senha, Component pai) {
        if (login == null || login.isBlank() || senha == null || senha.isBlank()) {
            Mensagem.aviso(pai, "Informe login e senha.");
            return null;
        }
        try {
            String hash = sha256(senha);
            Usuario u = dao.autenticar(login.trim(), hash);
            if (u == null) {
                Mensagem.erro(pai, "Login ou senha incorretos.");
                return null;
            }
            usuarioLogado = u;
            return u;
        } catch (SQLException e) {
            Mensagem.erro(pai, "Erro ao acessar o banco: " + e.getMessage());
            return null;
        }
    }

    public static Usuario getUsuarioLogado() { return usuarioLogado; }

    public static void logout() { usuarioLogado = null; }

    public static String sha256(String texto) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(texto.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
