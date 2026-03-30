package Controller;

import dao.ConsultaDAO;
import dao.ProntuarioDAO;
import model.Consulta;
import model.Prontuario;
import model.StatusConsulta;
import util.Mensagem;

import java.awt.Component;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class AgendaController {

    private final ConsultaDAO consultaDAO   = new ConsultaDAO();
    private final ProntuarioDAO prontuarioDAO = new ProntuarioDAO();

    public boolean agendarConsulta(Consulta c, Component pai) {
        if (!validarConsulta(c, pai)) return false;
        try {
            consultaDAO.inserir(c);
            Mensagem.info(pai, "Consulta agendada com sucesso!");
            return true;
        } catch (SQLException e) {
            Mensagem.erro(pai, "Erro ao agendar: " + e.getMessage());
            return false;
        }
    }

    public boolean cancelarConsulta(Consulta c, String motivo, Component pai) {
        if (motivo == null || motivo.isBlank()) {
            Mensagem.aviso(pai, "Informe o motivo do cancelamento."); return false;
        }
        c.setStatus(StatusConsulta.CANCELADA);
        c.setMotivoCancelamento(motivo);
        try {
            consultaDAO.atualizar(c);
            Mensagem.info(pai, "Consulta cancelada.");
            return true;
        } catch (SQLException e) {
            Mensagem.erro(pai, "Erro ao cancelar: " + e.getMessage());
            return false;
        }
    }

    public boolean realizarConsulta(Consulta c, Component pai) {
        c.setStatus(StatusConsulta.REALIZADA);
        try {
            consultaDAO.atualizar(c);
            return true;
        } catch (SQLException e) {
            Mensagem.erro(pai, "Erro ao atualizar consulta: " + e.getMessage());
            return false;
        }
    }

    public boolean salvarProntuario(Prontuario p, Component pai) {
        if (p.getQueixa() == null || p.getQueixa().isBlank()) {
            Mensagem.aviso(pai, "Queixa é obrigatória."); return false;
        }
        try {
            if (p.getId() == 0) {
                prontuarioDAO.inserir(p);
                realizarConsulta(p.getConsulta(), pai);
                Mensagem.info(pai, "Prontuário registrado com sucesso!");
            } else {
                prontuarioDAO.atualizar(p);
                Mensagem.info(pai, "Prontuário atualizado.");
            }
            return true;
        } catch (SQLException e) {
            Mensagem.erro(pai, "Erro ao salvar prontuário: " + e.getMessage());
            return false;
        }
    }

    public List<Consulta> listarPorDia(LocalDate dia, Component pai) {
        try { return consultaDAO.listarPorDia(dia); }
        catch (SQLException e) { Mensagem.erro(pai, e.getMessage()); return List.of(); }
    }

    public List<Consulta> listarTodas(Component pai) {
        try { return consultaDAO.listarTodas(); }
        catch (SQLException e) { Mensagem.erro(pai, e.getMessage()); return List.of(); }
    }

    public Prontuario buscarProntuario(int idConsulta, Component pai) {
        try { return prontuarioDAO.buscarPorConsulta(idConsulta); }
        catch (SQLException e) { Mensagem.erro(pai, e.getMessage()); return null; }
    }

    private boolean validarConsulta(Consulta c, Component pai) {
        if (c.getPaciente() == null) {
            Mensagem.aviso(pai, "Selecione o paciente."); return false;
        }
        if (c.getMedico() == null) {
            Mensagem.aviso(pai, "Selecione o médico."); return false;
        }
        if (!c.getMedico().isAtivo()) {
            Mensagem.erro(pai, "O médico selecionado está inativo."); return false;
        }
        if (c.getDataHora() == null || c.getDataHora().isBefore(LocalDateTime.now())) {
            Mensagem.aviso(pai, "A consulta deve ser agendada para uma data/hora futura."); return false;
        }
        return true;
    }
}
