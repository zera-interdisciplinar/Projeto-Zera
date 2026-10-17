package br.com.zera.util;

import br.com.zera.model.Admin;
import br.com.zera.model.Gestor;
import br.com.zera.model.Unidade;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import static br.com.zera.regex.Constants.SESSAO_ADMIN;
import static br.com.zera.regex.Constants.SESSAO_GESTOR;
import static br.com.zera.regex.Constants.SESSAO_UNIDADE;

/**
 * Classe utilitária para ler da sessão quem está logado.
 *
 * Centraliza a leitura das chaves {@code SESSAO_ADMIN}, {@code SESSAO_GESTOR}
 * e {@code SESSAO_UNIDADE}, para que os servlets não repitam o mesmo
 * {@code getSession().getAttribute(...)} com cast em todo lugar.
 * Esta classe não deve ser instanciada.
 *
 * @author Pedro Rufino
 * @since 2026-10-16
 */
public final class Sessao {

    /**
     * Construtor privado que proíbe a instanciação da classe utilitária.
     */
    private Sessao() {
        throw new UnsupportedOperationException("Classe Sessao não deve ser instanciada");
    }

    /**
     * Devolve o admin logado.
     *
     * @param request requisição HTTP atual
     * @return o {@link Admin} da sessão, ou null se não houver admin logado
     */
    public static Admin admin(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session == null ? null : (Admin) session.getAttribute(SESSAO_ADMIN);
    }

    /**
     * Devolve o gestor logado.
     *
     * @param request requisição HTTP atual
     * @return o {@link Gestor} da sessão, ou null se não houver gestor logado
     */
    public static Gestor gestor(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session == null ? null : (Gestor) session.getAttribute(SESSAO_GESTOR);
    }

    /**
     * Devolve a unidade do gestor logado.
     *
     * @param request requisição HTTP atual
     * @return a {@link Unidade} da sessão, ou null se não houver gestor logado
     */
    public static Unidade unidade(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session == null ? null : (Unidade) session.getAttribute(SESSAO_UNIDADE);
    }
}
