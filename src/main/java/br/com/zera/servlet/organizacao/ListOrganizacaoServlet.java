package br.com.zera.servlet.organizacao;

import br.com.zera.dao.OrganizacaoDAO;
import br.com.zera.exception.ConnectionFailedException;
import br.com.zera.model.Organizacao;
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
 * Servlet responsável por listar todas as organizações cadastradas.
 *
 * Usado apenas pelo admin, na tela de tabela do CRUD de organizações.
 * Busca todos os registros por meio do {@link OrganizacaoDAO} e envia para a página
 * no atributo "organizacoes".
 *
 * @author Pedro Rufino
 * @since 2026-10-15
 */
@WebServlet(name = "ListOrganizacao", value = "/organizacao/lista")
public class ListOrganizacaoServlet extends HttpServlet {

    /**
     * Processa requisições GET para exibir a lista de organizações.
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
            List<Organizacao> organizacoes = new OrganizacaoDAO().findAll();
            request.setAttribute("organizacoes", organizacoes);

            // envia para a JSP
            request.getRequestDispatcher("/WEB-INF/admin/organizacao/lista-organizacoes.jsp").forward(request, response);

        } catch (ConnectionFailedException cfe) {
            cfe.printStackTrace();
            exibirErro(request, response, cfe, ERROR_PAGE);
        } catch (Exception e) {
            e.printStackTrace();
            exibirErro(request, response, "Erro inesperado encontrado", ERROR_PAGE);
        }
    }
}