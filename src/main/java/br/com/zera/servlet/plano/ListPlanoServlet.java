package br.com.zera.servlet.plano;

import br.com.zera.dao.PlanoDAO;
import br.com.zera.exception.ConnectionFailedException;
import br.com.zera.model.Plano;
import br.com.zera.util.Sessao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

import static br.com.zera.exception.ErroServlet.exibirErro;
import static br.com.zera.regex.Constants.ERROR_PAGE;

/**
 * Servlet responsável por listar todos os planos cadastrados.
 *
 * Usado apenas pelo admin, na tela de tabela do CRUD de planos.
 * Busca todos os registros por meio do {@link PlanoDAO} e envia para a página
 * no atributo "planos".
 *
 * @author Pedro Rufino
 * @since 2026-10-15
 */
@WebServlet(name = "ListPlano", value = "/plano/lista")
public class ListPlanoServlet extends HttpServlet {

    /**
     * Processa requisições GET para exibir a lista de planos.
     *
     * @param request objeto HttpServletRequest que recebe a lista como atributo
     * @param response objeto HttpServletResponse usado para exibir a página ou o erro
     * @throws ServletException caso ocorra erro ao encaminhar para a página
     * @throws IOException caso ocorra erro de entrada/saída ao responder
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // só o admin vê os registros de todas as organizações
            if (Sessao.admin(request) == null) {
                exibirErro(request, response, "Acesso permitido apenas ao administrador.", ERROR_PAGE);
                return;
            }

            // busca todos os registros
            List<Plano> planos = new PlanoDAO().findAll();
            request.setAttribute("planos", planos);

            // envia para a JSP
            request.getRequestDispatcher("/WEB-INF/admin/plano/lista-planos.jsp").forward(request, response);

        } catch (ConnectionFailedException cfe) {
            cfe.printStackTrace();
            exibirErro(request, response, cfe, ERROR_PAGE);
        } catch (Exception e) {
            e.printStackTrace();
            exibirErro(request, response, "Erro inesperado encontrado", ERROR_PAGE);
        }
    }
}