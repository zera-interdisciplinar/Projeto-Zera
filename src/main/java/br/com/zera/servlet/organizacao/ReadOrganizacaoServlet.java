
package br.com.zera.servlet.organizacao;

import br.com.zera.dao.OrganizacaoDAO;
import br.com.zera.exception.ConnectionFailedException;
import br.com.zera.exception.NotFoundException;
import br.com.zera.model.Admin;
import br.com.zera.model.Organizacao;
import br.com.zera.model.Unidade;
import br.com.zera.util.Sessao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static br.com.zera.exception.ErroServlet.exibirErro;
import static br.com.zera.regex.Constants.ERROR_PAGE;

/**
 * Servlet responsável por mostrar uma organização.
 *
 * O que aparece depende de quem está logado:
 * - Admin com o parâmetro codigo: abre o formulário de edição da organização escolhida na lista.
 * - Admin sem código: abre o formulário de nova organização.
 * - Gestor: mostra a organização da própria unidade.
 *
 * @author Mayte B
 * @since 2026-10-06
 */
@WebServlet(name = "ReadOrganizacao", value = "/organizacao/perfil")
public class ReadOrganizacaoServlet extends HttpServlet {

    /**
     * Apresenta o formulário de cadastro, edição ou o perfil da organização.
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
            Admin admin = Sessao.admin(request);
            Unidade unidadeLogada = Sessao.unidade(request);
            OrganizacaoDAO dao = new OrganizacaoDAO();
            String pagina;

            if (admin != null) {
                String codigoTexto = request.getParameter("codigo");
                String cnpj = request.getParameter("cnpj");
                Organizacao organizacao = null;

                if (codigoTexto != null && !codigoTexto.isBlank()) {
                    int codigo = Integer.parseInt(codigoTexto.trim());
                    organizacao = dao.findByCodigo(codigo);
                } else if (cnpj != null && !cnpj.isBlank()) {
                    organizacao = dao.findByCnpj(cnpj.trim());
                }

                if (organizacao == null) {
                    pagina = "/WEB-INF/admin/organizacao/nova-organizacao.jsp";
                } else {
                    request.setAttribute("organizacao", organizacao);
                    pagina = "/WEB-INF/admin/organizacao/editar-organizacao.jsp";
                }
            } else if (unidadeLogada != null) {
                request.setAttribute("organizacao", dao.findByCodigo(unidadeLogada.getCodOrganizacao()));
                pagina = "/WEB-INF/gestor/organizacao.jsp";
            } else {
                exibirErro(request, response, "Sessão expirada. Faça login novamente.", ERROR_PAGE);
                return;
            }

            request.getRequestDispatcher(pagina).forward(request, response);
        } catch (NotFoundException e) {
            exibirErro(request, response, "Organização não encontrada.", ERROR_PAGE);
        } catch (NumberFormatException e) {
            exibirErro(request, response, "Código inválido.", ERROR_PAGE);
        } catch (ConnectionFailedException e) {
            e.printStackTrace();
            exibirErro(request, response, e, ERROR_PAGE);
        } catch (Exception e) {
            e.printStackTrace();
            exibirErro(request, response, "Erro inesperado encontrado.", ERROR_PAGE);
        }
    }
}
