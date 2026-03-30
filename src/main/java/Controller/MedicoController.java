package Controller;

import dao.MedicoDAO;
import model.Medico;
import util.Mensagem;

import java.awt.Component;
import java.sql.SQLException;
import java.util.List;

public class MedicoController {

    private final MedicoDAO dao = new MedicoDAO();

    public boolean salvar(Medico m, Component pai) {
        if (!validar(m, pai)) return false;
        try {
            if (m.getId() == 0) {
                Medico existente = dao.buscarPorCrm(m.getCrm(), m.getUfCrm());
                if (existente != null) {
                    Mensagem.erro(pai, "CRM já cadastrado para este estado.");
                    return false;
                }
                dao.inserir(m);
                Mensagem.info(pai, "Médico cadastrado com sucesso!");
            } else {
                dao.atualizar(m);
                Mensagem.info(pai, "Médico atualizado com sucesso!");
            }
            return true;
        } catch (SQLException e) {
            Mensagem.erro(pai, "Erro ao salvar médico: " + e.getMessage());
            return false;
        }
    }

    public boolean excluir(int id, Component pai) {
        if (!Mensagem.confirmar(pai, "Deseja realmente excluir este médico?")) return false;
        try {
            dao.deletar(id);
            Mensagem.info(pai, "Médico excluído.");
            return true;
        } catch (SQLException e) {
            Mensagem.erro(pai, "Não foi possível excluir: " + e.getMessage());
            return false;
        }
    }

    public List<Medico> listarTodos(Component pai) {
        try { return dao.listarTodos(); }
        catch (SQLException e) { Mensagem.erro(pai, "Erro ao listar: " + e.getMessage()); return List.of(); }
    }

    public List<Medico> listarAtivos(Component pai) {
        try { return dao.listarAtivos(); }
        catch (SQLException e) { Mensagem.erro(pai, "Erro ao listar: " + e.getMessage()); return List.of(); }
    }

    private boolean validar(Medico m, Component pai) {
        if (m.getNome() == null || m.getNome().isBlank()) {
            Mensagem.aviso(pai, "Nome é obrigatório."); return false;
        }
        if (m.getCrm() == null || m.getCrm().isBlank()) {
            Mensagem.aviso(pai, "CRM é obrigatório."); return false;
        }
        if (m.getUfCrm() == null || m.getUfCrm().length() != 2) {
            Mensagem.aviso(pai, "UF do CRM inválida (2 letras)."); return false;
        }
        if (m.getEspecialidade() == null || m.getEspecialidade().getId() == 0) {
            Mensagem.aviso(pai, "Selecione uma especialidade."); return false;
        }
        return true;
    }
}
