package br.com.zera.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

import static br.com.zera.regex.Constants.SESSAO_ADMIN;

/**
 * Servlet responsável por encerrar a sessão do usuário logado,
 * seja ele admin ou gestor.
 *
 * Antes de encerrar a sessão, verifica quem estava logado para
 * redirecionar à página de login correta.
 *
 * @author Pedro Rufino
 * @since 2026-10-11
 */
@WebServlet(name = "Logout", value = "/logout")
public class LogoutServlet extends HttpServlet {

    /** Página de login do gestor */
    private static final String LOGIN_GESTOR = "/login.jsp";

    /** Página de login do admin */
    private static final String LOGIN_ADMIN = "/adminLogin.jsp";

    /**
     * Processa POST para sair do sistema, encerrando a sessão atual.
     *
     * @param request objeto HttpServletRequest da requisição
     * @param response objeto HttpServletResponse para redirecionamento
     * @throws IOException caso haja um erro de input/output (entrada/saída)
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        // Se não houver ninguém logado, manda para o login do gestor
        String destino = LOGIN_GESTOR;

        // getSession(false) não cria uma sessão nova se não existir nenhuma
        HttpSession session = request.getSession(false);
        if (session != null) {
            // Lê ANTES do invalidate: depois dele, a sessão não pode mais ser usada
            if (session.getAttribute(SESSAO_ADMIN) != null) {
                destino = LOGIN_ADMIN;
            }
            session.invalidate();
        }

        response.sendRedirect(request.getContextPath() + destino);
    }
}