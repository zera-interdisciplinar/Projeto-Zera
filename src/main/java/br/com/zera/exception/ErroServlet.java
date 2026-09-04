package br.com.zera.exception;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Classe método utilitário para definição de erro e exibição ao usuário
 *
 * @author Maytê B
 */
public class ErroServlet {

    /**
     * Captura erros com resposta de mensagem padrão pré-definida
     *
     * @param req requisição HTTP atual, utilizada para armazenar o atributo de erro e realizar o forward.
     * @param resp resposta HTTP atual, repassada ao destino do forward.
     * @param mensagem pré-definida de exibição de erro do sistema caso encontre uma exceção não pré-definida
     * @param paginaErro caminho (relativo à aplicação) da página JSP responsável por exibir o erro.
     *
     * @throws IOException caso o forward para {@code paginaErro} lance uma {@link ServletException}, indicando falha no processamento do recurso de destino.
     */
    public static void exibirErro(HttpServletRequest req, HttpServletResponse resp, String mensagem, String paginaErro) throws IOException {
        try {
            req.setAttribute("mensagemErro", mensagem);
            req.getRequestDispatcher(paginaErro).forward(req, resp);
        } catch (ServletException se) {
            throw new RuntimeException("Erro na comunicação HTTP (ServletException)");
        }
    }

/**
 * Captura erros de exceção durante a execução e exibe uma página javaScript de erro padrão
 *
 * @param req requisição HTTP atual, utilizada para armazenar o atributo de erro e realizar o forward.
 * @param resp resposta HTTP atual, repassada ao destino do forward.
 * @param excecao exceção capturada previamente, cuja mensagem será exibida na página de erro.
 * @param paginaErro caminho (relativo à aplicação) da página JSP responsável por exibir o erro.
 *
 * @throws RuntimeException caso o forward para {@code paginaErro} lance uma {@link ServletException}, indicando falha no processamento do recurso de destino.
 */
    public static void exibirErro(HttpServletRequest req, HttpServletResponse resp, Exception excecao, String paginaErro) throws IOException {
        try {
            req.setAttribute("mensagemErro", excecao.getMessage());
            req.getRequestDispatcher(paginaErro).forward(req, resp);
        } catch (ServletException se) {
            throw new RuntimeException("Erro na comunicação HTTP (ServletException)");
        }
    }
}