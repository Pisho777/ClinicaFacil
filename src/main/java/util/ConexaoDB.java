package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Gerencia a conexão JDBC com o banco MySQL.
 * Altere URL, USER e PASSWORD conforme seu ambiente.
 */
public class ConexaoDB {

    private static final String URL    = "jdbc:mysql://localhost:3306/clinicafacil?useSSL=false&serverTimezone=America/Sao_Paulo&allowPublicKeyRetrieval=true";
    private static final String USER   = "root";
    private static final String PASS   = "root";

    private ConexaoDB() {}

    public static Connection obterConexao() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    public static void fechar(AutoCloseable... recursos) {
        for (AutoCloseable r : recursos) {
            if (r != null) {
                try { r.close(); } catch (Exception ignored) {}
            }
        }
    }
}
