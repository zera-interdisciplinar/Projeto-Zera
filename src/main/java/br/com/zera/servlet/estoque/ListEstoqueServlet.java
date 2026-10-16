package br.com.zera.servlet.estoque;

import br.com.zera.dao.EstoqueDAO;
import br.com.zera.exception.ConnectionFailedException;
import br.com.zera.model.Admin;
import br.com.zera.model.Estoque;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

import static br.com.zera.exception.ErroServlet.exibirErro;
import static br.com.zera.regex.Constants.ERROR_PAGE;
import static br.com.zera.regex.Constants.SESSAO_ADMIN;

/**
 * Servlet responsável por listar todos os estoques cadastrados.
 *
 * Usado apenas pelo admin, na tela de tabela do CRUD de estoques.
 * Busca todos os registros por meio do {@link EstoqueDAO} e os envia
 * para a página no atributo "estoques".
 *
 * @author Pedro Rufino
 * @since 2026-10-15
 */
@WebServlet(name = "ListEstoque", value = "/estoque/lista")
public class ListEstoqueServlet extends HttpServlet {

    /**
     * Processa requisições GET para exibir a lista de estoques.
     *
     * @param request objeto HttpServletRequest que recebe a lista de estoques como atributo
     * @param response objeto HttpServletResponse usado para exibir a página ou o erro
     * @throws ServletException caso ocorra erro ao encaminhar para a página
     * @throws IOException caso ocorra erro de entrada/saída ao responder
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // somente o admin vê os estoques de todas as empresas
            HttpSession session = request.getSession();
            Admin adminLogado = (Admin) session.getAttribute(SESSAO_ADMIN);
            if (adminLogado == null) {
                exibirErro(request, response, "Acesso permitido apenas ao administrador.", ERROR_PAGE);
                return;
            }

            // busca todos os estoques (lista vazia se não houver nenhum registro)
            EstoqueDAO dao = new EstoqueDAO();
            List<Estoque> estoques = dao.findAll();

            // envia a lista para a página de tabela
            request.setAttribute("estoques", estoques);
            request.getRequestDispatcher("/WEB-INF/admin/lista-estoques.jsp").forward(request, response);

        } catch (ConnectionFailedException cfe) {
            cfe.printStackTrace();
            exibirErro(request, response, cfe, ERROR_PAGE);
        } catch (ServletException se) {
            se.printStackTrace();
            exibirErro(request, response, se, ERROR_PAGE);
        } catch (Exception e) {
            e.printStackTrace();
            exibirErro(request, response, "Erro inesperado encontrado", ERROR_PAGE);
        }
    }
}
