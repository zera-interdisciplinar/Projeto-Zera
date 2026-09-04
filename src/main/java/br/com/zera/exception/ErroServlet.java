package br.com.zera.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletException;

import java.io.IOException;

public class ErroServlet extends RuntimeException {
    public ErroServlet(String message) {
        super(message);
    }

    /**
     * Exibe página de erro com mensagem definida para usuário caso haja erro.
     *
     * @param req        requisição HTTP atual, utilizada para armazenar o atributo de erro e realizar o forward.
     * @param resp       resposta HTTP atual, repassada ao destino do forward.
     * @param mensagem   texto da mensagem de erro a ser exibida na página de destino.
     * @param paginaErro caminho da página JSP responsável por exibir o erro.
     *
     * @throws RuntimeException caso o forward para {@code paginaErro} lance uma {@link ErroServlet} processamento do recurso de destino.
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
     * Exibe uma página de erro a partir de uma exceção já capturada em outra camada da aplicação.
     *
     * @param req requisição HTTP atual, utilizada para armazenar o atributo de erro e realizar o forward.
     * @param resp resposta HTTP atual, repassada ao destino do forward.
     * @param excecao exceção capturada previamente, cuja mensagem será exibida na página de erro.
     * @param paginaErro caminho da página JSP responsável por exibir o erro.
     *
     * @throws RuntimeException caso o forward para {@code paginaErro} lance uma {@link ErroServlet}, indicando falha no processamento do recurso de destino.
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