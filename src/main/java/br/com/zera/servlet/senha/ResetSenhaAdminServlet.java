
package br.com.zera.servlet.senha;

import br.com.zera.config.HashSenha;
import br.com.zera.dao.AdminDAO;
import br.com.zera.exception.ConnectionFailedException;
import br.com.zera.exception.NotFoundException;
import br.com.zera.model.Admin;
import br.com.zera.regex.Regex;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import static br.com.zera.exception.ErroServlet.exibirErro;
import static br.com.zera.exception.ErroServlet.voltarAoFormulario;
import static br.com.zera.regex.Constants.ERROR_PAGE;
import static br.com.zera.servlet.senha.ForgotSenhaAdminServlet.*;

/**
 * Servlet responsável pela redefinição da senha do administrador.
 * Valida os dados e atualiza a senha no banco de dados.
 *
 * @author Projeto Zera
 */
@WebServlet(name = "ResetSenhaAdmin", value = "/admin/senha/nova")
public class ResetSenhaAdminServlet extends HttpServlet {

    private static final String PAGINA = "/WEB-INF/login-restrita/nova-senha.jsp";
    private static final String PAGINA_SUCESSO = "/WEB-INF/login-restrita/senha-alterada.jsp";
    private static final String INICIO = "/admin/senha/esqueci";

    /**
     * Exibe o formulário de nova senha após a validação do código.
     *
     * @param request requisição HTTP
     * @param response resposta HTTP
     * @throws ServletException se ocorrer erro no encaminhamento
     * @throws IOException se ocorrer erro de entrada ou saída
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        if (!dadosConfirmados(session)) {
            response.sendRedirect(request.getContextPath() + INICIO);
            return;
        }

        request.getRequestDispatcher(PAGINA).forward(request, response);
    }

    /**
     * Valida a nova senha e atualiza seu hash no banco de dados.
     *
     * @param request requisição contendo senha e confirmação
     * @param response resposta HTTP
     * @throws ServletException se ocorrer erro no encaminhamento
     * @throws IOException se ocorrer erro de entrada ou saída
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);

        // garante que caracteres especiais cheguem corretos
        request.setCharacterEncoding("UTF-8");

        if (!dadosConfirmados(session)) {
            response.sendRedirect(request.getContextPath() + INICIO);
            return;
        }

        String senha = request.getParameter("senha");
        String confirmacao = request.getParameter("confirmacao");

        try {
            if (senha == null || !Regex.validarSenha(senha)) {
                voltarAoFormulario(request, response, "A senha precisa ter pelo menos 8 caracteres, um número e um caractere especial.", PAGINA);
                return;
            }

            if (!senha.equals(confirmacao)) {
                voltarAoFormulario(request, response, "A confirmação está diferente da senha informada.", PAGINA);
                return;
            }

            String email = (String) session.getAttribute(EMAIL);
            AdminDAO dao = new AdminDAO();
            Admin admin = dao.findByEmail(email);

            String hash = HashSenha.gerarHash(senha);
            dao.updateSenha(admin.getCodigo(), hash);

            session.removeAttribute(EMAIL);
            session.removeAttribute(CODIGO);
            session.removeAttribute(EXPIRA);
            session.removeAttribute(TENTATIVAS);
            session.removeAttribute(VALIDADA);

            request.getRequestDispatcher(PAGINA_SUCESSO).forward(request, response);

        } catch (NotFoundException nfe) {
            nfe.printStackTrace();
            exibirErro(request, response, "Cadastro não encontrado.", ERROR_PAGE);

        } catch (ConnectionFailedException cfe) {
            cfe.printStackTrace();
            exibirErro(request, response, cfe, ERROR_PAGE);

        } catch (Exception e) {
            e.printStackTrace();
            exibirErro(request, response, "Erro inesperado encontrado", ERROR_PAGE);
        }
    }

    /**
     * Verifica se o código de recuperação foi validado.
     *
     * @param session sessão HTTP atual
     * @return true se a recuperação estiver autorizada
     */
    private boolean dadosConfirmados(HttpSession session) {
        return session != null && Boolean.TRUE.equals(session.getAttribute(VALIDADA))
                && session.getAttribute(EMAIL) != null;
    }
}
