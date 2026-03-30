package Controller;

import dao.PacienteDAO;
import model.Paciente;
import util.Mensagem;
import util.ValidadorCPF;

import java.awt.Component;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

public class PacienteController {

    private final PacienteDAO dao = new PacienteDAO();

    public boolean salvar(Paciente p, Component pai) {
        if (!validar(p, pai)) return false;
        try {
            if (p.getId() == 0) {
                if (dao.buscarPorCpf(ValidadorCPF.somenteDigitos(p.getCpf())) != null) {
                    Mensagem.erro(pai, "CPF já cadastrado para outro paciente.");
                    return false;
                }
                p.setCpf(ValidadorCPF.somenteDigitos(p.getCpf()));
                dao.inserir(p);
                Mensagem.info(pai, "Paciente cadastrado com sucesso!");
            } else {
                p.setCpf(ValidadorCPF.somenteDigitos(p.getCpf()));
                dao.atualizar(p);
                Mensagem.info(pai, "Paciente atualizado com sucesso!");
            }
            return true;
        } catch (SQLException e) {
            Mensagem.erro(pai, "Erro ao salvar paciente: " + e.getMessage());
            return false;
        }
    }

    public boolean excluir(int id, Component pai) {
        if (!Mensagem.confirmar(pai, "Deseja realmente excluir este paciente?")) return false;
        try {
            dao.deletar(id);
            Mensagem.info(pai, "Paciente excluído.");
            return true;
        } catch (SQLException e) {
            Mensagem.erro(pai, "Não foi possível excluir: " + e.getMessage());
            return false;
        }
    }

    public List<Paciente> listarTodos(Component pai) {
        try { return dao.listarTodos(); }
        catch (SQLException e) { Mensagem.erro(pai, "Erro ao listar: " + e.getMessage()); return List.of(); }
    }

    public List<Paciente> buscarPorNome(String nome, Component pai) {
        try { return dao.buscarPorNome(nome); }
        catch (SQLException e) { Mensagem.erro(pai, "Erro na busca: " + e.getMessage()); return List.of(); }
    }

    private boolean validar(Paciente p, Component pai) {
        if (p.getNome() == null || p.getNome().isBlank()) {
            Mensagem.aviso(pai, "Nome é obrigatório."); return false;
        }
        if (!ValidadorCPF.validar(p.getCpf())) {
            Mensagem.aviso(pai, "CPF inválido."); return false;
        }
        if (p.getDataNasc() == null || p.getDataNasc().isAfter(LocalDate.now())) {
            Mensagem.aviso(pai, "Data de nascimento inválida."); return false;
        }
        int idade = Period.between(p.getDataNasc(), LocalDate.now()).getYears();
        if (idade < 18 && (p.getResponsavel() == null || p.getResponsavel().isBlank())) {
            Mensagem.aviso(pai, "Responsável é obrigatório para menores de 18 anos."); return false;
        }
        return true;
    }
}
