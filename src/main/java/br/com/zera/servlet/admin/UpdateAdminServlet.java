package br.com.zera.servlet.admin;

import br.com.zera.dao.AdminDAO;
import br.com.zera.exception.ConnectionFailedException;
import br.com.zera.exception.NotFoundException;
import br.com.zera.model.Admin;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

import static br.com.zera.exception.ErroServlet.exibirErro;
import static br.com.zera.regex.Constants.ERROR_PAGE;
import static br.com.zera.regex.Constants.SESSAO_ADMIN;

/**
 * Servlet responsável por atualizar o nome e o email do admin logado.
 *
 * A troca de senha é feita separadamente pelo {@link UpdateSenhaAdminServlet}.
 *
 * @author Pedro Rufino
 * @since 2026-10-11
 */
@WebServlet(name = "UpdateAdmin", value = "/admin/AtualizarAdmin")
public class UpdateAdminServlet extends HttpServlet {

    /**
     * Processa POST para atualizar os dados do admin logado.
     *
     * @param request objeto HttpServletRequest contendo os parâmetros da página
     * @param response objeto HttpServletResponse para redirecionamento ou forward
     * @throws IOException caso haja um erro de input/output (entrada/saída)
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        AdminDAO dao = new AdminDAO();

        try {
            HttpSession session = request.getSession();
            Admin adminLogado = (Admin) session.getAttribute(SESSAO_ADMIN);
            if (adminLogado == null) {
                exibirErro(request, response, "Sessão expirada. Faça login novamente.", ERROR_PAGE);
                return;
            }

            String nome = request.getParameter("nome");
            String email = request.getParameter("email");

            if (nome == null || nome.trim().isEmpty()) {
                exibirErro(request, response, "Nome não informado.", ERROR_PAGE);
                return;
            }
            if (email == null || email.trim().isEmpty()) {
                exibirErro(request, response, "Email não informado.", ERROR_PAGE);
                return;
            }

            Admin admin = new Admin(adminLogado.getCodigo(), nome.trim(), email.trim(), null);
            dao.update(admin);

            // Mantém a sessão com os dados novos
            adminLogado.setNome(admin.getNome());
            adminLogado.setEmail(admin.getEmail());

            response.sendRedirect(request.getContextPath() + "/admin/perfil");
        } catch (ConnectionFailedException cfe) {
            cfe.printStackTrace();
            exibirErro(request, response, cfe, ERROR_PAGE);
        } catch (NotFoundException nfe) {
            nfe.printStackTrace();
            exibirErro(request, response, nfe, ERROR_PAGE);
        } catch (Exception e) {
            e.printStackTrace();
            exibirErro(request, response, e, ERROR_PAGE);
        }
    }
}