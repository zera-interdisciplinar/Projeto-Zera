package br.com.zera.servlet.admin;

import br.com.zera.dao.AdminDAO;
import br.com.zera.exception.ConnectionFailedException;
import br.com.zera.exception.NotFoundException;
import br.com.zera.model.Admin;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

import static br.com.zera.exception.ErroServlet.exibirErro;
import static br.com.zera.regex.Constants.ERROR_PAGE;
import static br.com.zera.regex.Constants.SESSAO_ADMIN;

/**
 * Servlet responsável por exibir o perfil do admin logado.
 *
 * Busca os dados atualizados do admin no banco e os envia para a tela
 * "Meu perfil", onde ficam os formulários de alterar dados e alterar senha.
 *
 * @author Pedro Rufino
 * @since 2026-10-13
 */
@WebServlet(name = "ReadAdmin", value = "/admin/perfil")
public class ReadAdminServlet extends HttpServlet {

    /**
     * Processa GET para exibir o perfil do admin logado.
     *
     * @param request objeto HttpServletRequest da requisição
     * @param response objeto HttpServletResponse para forward ou erro
     * @throws ServletException se ocorrer falha no encaminhamento para a JSP
     * @throws IOException caso haja um erro de input/output (entrada/saída)
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // resgata o admin logado na sessão
            HttpSession session = request.getSession();
            Admin adminLogado = (Admin) session.getAttribute(SESSAO_ADMIN);
            if (adminLogado == null) {
                exibirErro(request, response, "Sessão expirada. Faça login novamente.", ERROR_PAGE);
                return;
            }

            // busca no banco para mostrar os dados mais recentes
            AdminDAO dao = new AdminDAO();
            Admin admin = dao.findByCodigo(adminLogado.getCodigo());

            // a senha (hash) não é enviada para a tela
            request.setAttribute("nome", admin.getNome());
            request.setAttribute("email", admin.getEmail());

            request.getRequestDispatcher("/WEB-INF/admin/perfil-admin.jsp").forward(request, response);

        } catch (NotFoundException nfe) {
            nfe.printStackTrace();
            exibirErro(request, response, "Admin não encontrado. Faça login novamente.", ERROR_PAGE);
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
