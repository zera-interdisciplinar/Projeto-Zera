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
 * Servlet responsável por trocar a senha do admin logado.
 *
 * Exige a senha atual, para que alguém com acesso a uma sessão
 * aberta não consiga trocar a senha sem conhecê-la.
 *
 * @author Pedro Rufino
 * @since 2026-10-11
 */
@WebServlet(name = "UpdateSenhaAdmin", value = "/admin/AlterarSenha")
public class UpdateSenhaAdminServlet extends HttpServlet {

    /**
     * Processa POST para trocar a senha do admin logado.
     *
     * @param request objeto HttpServletRequest com senhaAtual, novaSenha e confirmacao
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

            String senhaAtual = request.getParameter("senhaAtual");
            String novaSenha = request.getParameter("novaSenha");
            String confirmacao = request.getParameter("confirmacao");

            if (senhaAtual == null || senhaAtual.isEmpty()
                    || novaSenha == null || novaSenha.isEmpty()) {
                exibirErro(request, response, "Preencha a senha atual e a nova senha.", ERROR_PAGE);
                return;
            }
            if (!novaSenha.equals(confirmacao)) {
                exibirErro(request, response, "A confirmação não confere com a nova senha.", ERROR_PAGE);
                return;
            }

            // busca a do banco para comparar
            Admin admin = dao.findByCodigo(adminLogado.getCodigo());

            if (!senhaAtual.equals(admin.getSenha())) {
                exibirErro(request, response, "Senha atual incorreta.", ERROR_PAGE);
                return;
            }

            dao.updateSenha(admin.getCodigo(), novaSenha);

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