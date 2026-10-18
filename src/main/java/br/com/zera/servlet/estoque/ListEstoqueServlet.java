package br.com.zera.servlet.estoque;

import br.com.zera.dao.EnderecoDAO;
import br.com.zera.dao.EstoqueDAO;
import br.com.zera.dao.GestorDAO;
import br.com.zera.dao.OrganizacaoDAO;
import br.com.zera.dao.UnidadeDAO;
import br.com.zera.exception.ConnectionFailedException;
import br.com.zera.model.Estoque;
import br.com.zera.model.Gestor;
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
 * Servlet responsável por listar todos os estoques cadastrados.
 *
 * Usado apenas pelo admin, na tela de tabela do CRUD de estoques.
 * Busca todos os registros por meio do {@link EstoqueDAO} e envia para a página
 * no atributo "estoques".
 *
 * Também envia as listas "gestores" (responsável), "unidades",
 * "organizacoes" e "enderecos" para a JSP mostrar os nomes na tabela.
 *
 * @author Pedro Rufino
 * @since 2026-10-15
 */
@WebServlet(name = "ListEstoque", value = "/estoque/lista")
public class ListEstoqueServlet extends HttpServlet {

    /**
     * Processa requisições GET para exibir a lista de estoques.
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
            List<Estoque> estoques = new EstoqueDAO().findAll();
            request.setAttribute("estoques", estoques);

            // gestores para mostrar o nome do responsável
            List<Gestor> gestores = new GestorDAO().findAll();
            for (Gestor gestor : gestores) {
                gestor.setSenha(null);
            }
            request.setAttribute("gestores", gestores);

            // listas para a JSP trocar os códigos pelos nomes
            request.setAttribute("unidades", new UnidadeDAO().findAll());
            request.setAttribute("organizacoes", new OrganizacaoDAO().findAll());
            request.setAttribute("enderecos", new EnderecoDAO().findAll());

            // envia para a JSP
            request.getRequestDispatcher("/WEB-INF/admin/lista-estoques.jsp").forward(request, response);

        } catch (ConnectionFailedException cfe) {
            cfe.printStackTrace();
            exibirErro(request, response, cfe, ERROR_PAGE);
        } catch (Exception e) {
            e.printStackTrace();
            exibirErro(request, response, "Erro inesperado encontrado", ERROR_PAGE);
        }
    }
}