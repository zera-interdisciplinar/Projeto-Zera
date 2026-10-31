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
     * Lista estoques e aplica filtros opcionais de pesquisa.
     *
     * @param request requisição HTTP
     * @param response resposta HTTP
     * @throws ServletException caso ocorra erro no encaminhamento
     * @throws IOException caso ocorra erro de entrada ou saída
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            if (Sessao.admin(request) == null) {
                exibirErro(request, response, "Acesso permitido apenas ao administrador.", ERROR_PAGE);
                return;
            }

            String busca = request.getParameter("buscar");
            Integer codUnidade = lerCodigo(request.getParameter("codUnidade"));
            Integer codResponsavel = lerCodigo(request.getParameter("codResponsavel"));
            Integer codOrganizacao = lerCodigo(request.getParameter("codOrganizacao"));

            List<Estoque> estoques = new EstoqueDAO().findByFiltros(
                    busca, codUnidade, codResponsavel, codOrganizacao);
            request.setAttribute("estoques", estoques);

            List<Gestor> gestores = new GestorDAO().findAll();
            for (Gestor gestor : gestores) {
                gestor.setSenha(null);
            }
            request.setAttribute("gestores", gestores);
            request.setAttribute("unidades", new UnidadeDAO().findAll());
            request.setAttribute("organizacoes", new OrganizacaoDAO().findAll());
            request.setAttribute("enderecos", new EnderecoDAO().findAll());
            request.getRequestDispatcher("/WEB-INF/admin/estoque/lista-estoques.jsp")
                    .forward(request, response);

        } catch (NumberFormatException e) {
            exibirErro(request, response, "Filtro inválido.", ERROR_PAGE);
        } catch (ConnectionFailedException e) {
            e.printStackTrace();
            exibirErro(request, response, e, ERROR_PAGE);
        } catch (Exception e) {
            e.printStackTrace();
            exibirErro(request, response, "Erro inesperado encontrado.", ERROR_PAGE);
        }
    }

    /**
     * Converte um parâmetro de filtro em código positivo.
     *
     * @param valor texto do filtro
     * @return código informado ou null
     * @throws NumberFormatException se o código for inválido
     */
    private Integer lerCodigo(String valor) {
        if (valor == null || valor.isBlank()) return null;
        int codigo = Integer.parseInt(valor.trim());
        if (codigo <= 0) throw new NumberFormatException("Código inválido.");
        return codigo;
    }
}
