package br.com.zera.servlet.organizacao;

import br.com.zera.dao.OrganizacaoDAO;
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
     * Processa requisições GET para exibir uma organização ou o formulário de nova organização.
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
            OrganizacaoDAO dao = new OrganizacaoDAO();

            String pagina;
            if (admin != null) {
                // lê o código
                String codigoTexto = request.getParameter("codigo");
                Integer codigo = null;
                if (codigoTexto != null && !codigoTexto.isBlank()) {
                    codigo = Integer.parseInt(codigoTexto.trim());
                }
                if (codigo == null) {
                    pagina = "/WEB-INF/admin/nova-organizacao.jsp";
                } else {
                    request.setAttribute("organizacao", dao.findByCodigo(codigo));
                    pagina = "/WEB-INF/admin/editar-organizacao.jsp";
                }
            } else if (unidadeLogada != null) {
                // a organização é a chave estrangeira da unidade do gestor
                request.setAttribute("organizacao", dao.findByCodigo(unidadeLogada.getCodOrganizacao()));
                pagina = "/WEB-INF/gestor/organizacao.jsp";
            } else {
                exibirErro(request, response, "Sessão expirada. Faça login novamente.", ERROR_PAGE);
                return;
            }

            // envia para a JSP
            request.getRequestDispatcher(pagina).forward(request, response);

        } catch (NotFoundException nfe) {
            exibirErro(request, response, "Organização não cadastrada.", ERROR_PAGE);
        } catch (NumberFormatException nmfe) {
            exibirErro(request, response, "Código da organização inválido.", ERROR_PAGE);
        } catch (ConnectionFailedException cfe) {
            cfe.printStackTrace();
            exibirErro(request, response, cfe, ERROR_PAGE);
        } catch (Exception e) {
            e.printStackTrace();
            exibirErro(request, response, "Erro inesperado encontrado", ERROR_PAGE);
        }
    }
}
