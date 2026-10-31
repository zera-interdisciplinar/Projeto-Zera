package br.com.zera.servlet.unidade;

import br.com.zera.dao.OrganizacaoDAO;
import br.com.zera.dao.UnidadeDAO;
import br.com.zera.exception.ConnectionFailedException;
import br.com.zera.exception.NotFoundException;
import br.com.zera.model.Admin;
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
 * Servlet responsável por mostrar uma unidade.
 *
 * O que aparece depende de quem está logado:
 * - Admin com o parâmetro codigo: formulário de edição, com a lista de organizações para o campo de seleção.
 * - Admin sem código: formulário de nova unidade.
 * - Gestor: dados da própria unidade e da organização dela.
 *
 * @author Mayte B
 * @since 2026-10-02
 */
@WebServlet(name = "ReadUnidade", value = "/unidade/perfil")
public class ReadUnidadeServlet extends HttpServlet {

    /**
     * Processa requisições GET para exibir uma unidade ou o formulário de nova unidade.
     *
     * @param request objeto que contém os dados da requisição
     * @param response objeto usado para enviar respostas ao cliente (forward ou erro)
     * @throws ServletException se ocorrer falha no encaminhamento para a JSP
     * @throws IOException caso ocorra erro de entrada/saída ao responder
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // descobre quem está logado: admin ou gestor
            Admin admin = Sessao.admin(request);
            Unidade unidadeLogada = Sessao.unidade(request);
            UnidadeDAO dao = new UnidadeDAO();
            OrganizacaoDAO organizacaoDAO = new OrganizacaoDAO();

            String pagina;
            if (admin != null) {
                // lista de organizações para o campo de seleção
                request.setAttribute("organizacoes", organizacaoDAO.findAll());

                // lê o código
                String codigoTexto = request.getParameter("codigo");
                Integer codigo = null;
                if (codigoTexto != null && !codigoTexto.isBlank()) {
                    codigo = Integer.parseInt(codigoTexto.trim());
                }
                if (codigo == null) {
                    pagina = "/WEB-INF/admin/unidade/nova-unidade.jsp";
                } else {
                    request.setAttribute("unidade", dao.findByCodigo(codigo));
                    pagina = "/WEB-INF/admin/unidade/editar-unidade.jsp";
                }
            } else if (unidadeLogada != null) {
                Unidade unidade = dao.findByCodigo(unidadeLogada.getCodigo());
                request.setAttribute("unidade", unidade);
                request.setAttribute("organizacao", organizacaoDAO.findByCodigo(unidade.getCodOrganizacao()));
                pagina = "/WEB-INF/gestor/unidade.jsp";
            } else {
                exibirErro(request, response, "Sessão expirada. Faça login novamente.", ERROR_PAGE);
                return;
            }

            // envia para a JSP
            request.getRequestDispatcher(pagina).forward(request, response);

        } catch (NotFoundException nfe) {
            exibirErro(request, response, "Unidade não cadastrada.", ERROR_PAGE);
        } catch (NumberFormatException nmfe) {
            exibirErro(request, response, "Código da unidade inválido.", ERROR_PAGE);
        } catch (ConnectionFailedException cfe) {
            cfe.printStackTrace();
            exibirErro(request, response, cfe, ERROR_PAGE);
        } catch (Exception e) {
            e.printStackTrace();
            exibirErro(request, response, "Erro inesperado encontrado", ERROR_PAGE);
        }
    }
}
