package util;

/**
 * Valida CPF segundo o algoritmo oficial da Receita Federal.
 */
public class ValidadorCPF {

    private ValidadorCPF() {}

    /** Remove pontuação e valida os dígitos verificadores. */
    public static boolean validar(String cpf) {
        if (cpf == null) return false;
        String c = cpf.replaceAll("[^0-9]", "");
        if (c.length() != 11) return false;
        // Rejeita sequências idênticas (ex.: 111.111.111-11)
        if (c.chars().distinct().count() == 1) return false;

        int soma = 0;
        for (int i = 0; i < 9; i++) soma += (c.charAt(i) - '0') * (10 - i);
        int r1 = (soma * 10) % 11;
        if (r1 == 10) r1 = 0;
        if (r1 != (c.charAt(9) - '0')) return false;

        soma = 0;
        for (int i = 0; i < 10; i++) soma += (c.charAt(i) - '0') * (11 - i);
        int r2 = (soma * 10) % 11;
        if (r2 == 10) r2 = 0;
        return r2 == (c.charAt(10) - '0');
    }

    /** Retorna somente os 11 dígitos, sem máscara. */
    public static String somenteDigitos(String cpf) {
        return cpf == null ? "" : cpf.replaceAll("[^0-9]", "");
    }

    /** Formata CPF: 000.000.000-00 */
    public static String formatar(String cpf) {
        String c = somenteDigitos(cpf);
        if (c.length() != 11) return cpf;
        return c.substring(0, 3) + "." + c.substring(3, 6) + "."
             + c.substring(6, 9) + "-" + c.substring(9);
    }
}
