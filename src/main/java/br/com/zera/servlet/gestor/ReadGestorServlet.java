package br.com.zera.servlet.gestor;

import br.com.zera.dao.GestorDAO;
import br.com.zera.dao.UnidadeDAO;
import br.com.zera.exception.ConnectionFailedException;
import br.com.zera.exception.NotFoundException;
import br.com.zera.model.Admin;
import br.com.zera.model.Gestor;
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
 * Servlet responsável por mostrar as informações de um gestor.
 *
 * O que aparece depende de quem está logado:
 * - Admin com o parâmetro codigo: formulário de edição, com a lista de unidades.
 * - Admin sem código: formulário de novo gestor.
 * - Gestor: o próprio perfil.
 *
 * A senha (hash) nunca é enviada para a tela: o objeto vai para a JSP com a senha nula.
 *
 * @author Mayte B
 * @since 2026-10-06
 */
@WebServlet(name = "ReadGestor", value = "/gestor/perfil")
public class ReadGestorServlet extends HttpServlet {

    /**
     * Processa requisições GET para exibir um gestor ou o formulário de novo gestor.
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
            Gestor gestorLogado = Sessao.gestor(request);
            GestorDAO dao = new GestorDAO();

            String pagina;
            if (admin != null) {
                // lista de unidades para o campo de seleção
                request.setAttribute("unidades", new UnidadeDAO().findAll());

                // lê o código
                String codigoTexto = request.getParameter("codigo");
                Integer codigo = null;
                if (codigoTexto != null && !codigoTexto.isBlank()) {
                    codigo = Integer.parseInt(codigoTexto.trim());
                }
                if (codigo == null) {
                    pagina = "/WEB-INF/admin/gestor/novo-gestor.jsp";
                } else {
                    request.setAttribute("gestor", semSenha(dao.findByCodigo(codigo)));
                    pagina = "/WEB-INF/admin/gestor/editar-gestor.jsp";
                }
            } else if (gestorLogado != null) {
                request.setAttribute("gestor", semSenha(dao.findByCodigo(gestorLogado.getCodigo())));
                pagina = "/WEB-INF/gestor/perfil.jsp";
            } else {
                exibirErro(request, response, "Sessão expirada. Faça login novamente.", ERROR_PAGE);
                return;
            }

            // envia para a JSP
            request.getRequestDispatcher(pagina).forward(request, response);

        } catch (NotFoundException nfe) {
            exibirErro(request, response, "Gestor não cadastrado.", ERROR_PAGE);
        } catch (NumberFormatException nmfe) {
            exibirErro(request, response, "Código do gestor inválido.", ERROR_PAGE);
        } catch (ConnectionFailedException cfe) {
            cfe.printStackTrace();
            exibirErro(request, response, cfe, ERROR_PAGE);
        } catch (Exception e) {
            e.printStackTrace();
            exibirErro(request, response, "Erro inesperado encontrado", ERROR_PAGE);
        }
    }

    /**
     * Apaga o hash da senha antes de mandar o gestor para a JSP.
     *
     * @param gestor gestor buscado no banco
     * @return o mesmo gestor, com a senha nula
     */
    private Gestor semSenha(Gestor gestor) {
        // não manda o hash da senha para a JSP
        gestor.setSenha(null);
        return gestor;
    }
}
