package br.com.zera.util;

import br.com.zera.model.Admin;
import br.com.zera.model.Gestor;
import br.com.zera.model.Unidade;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 * Classe utilitária para acessar informações dos usuários autenticados
 * a partir da requisição HTTP.
 *
 * Centraliza os métodos de acesso aos dados do administrador, gestor
 * e unidade, evitando a repetição de código nos servlets.
 *
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
        return session == null ? null : (Admin) session.getAttribute("adminLogado");
    }

    /**
     * Devolve o gestor logado.
     *
     * @param request requisição HTTP atual
     * @return o {@link Gestor} da sessão, ou null se não houver gestor logado
     */
    public static Gestor gestor(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session == null ? null : (Gestor) session.getAttribute("gestorLogado");
    }

    /**
     * Devolve a unidade do gestor logado.
     *
     * @param request requisição HTTP atual
     * @return a {@link Unidade} da sessão, ou null se não houver gestor logado
     */
    public static Unidade unidade(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session == null ? null : (Unidade) session.getAttribute("unidadeLogada");
    }
}
