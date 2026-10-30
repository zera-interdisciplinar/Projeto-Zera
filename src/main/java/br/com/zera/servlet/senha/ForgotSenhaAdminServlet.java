
package br.com.zera.servlet.senha;

import br.com.zera.dao.AdminDAO;
import br.com.zera.email.CodigoVerificacao;
import br.com.zera.email.EnviarEmail;
import br.com.zera.exception.ConnectionFailedException;
import br.com.zera.exception.EmailFailedException;
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
import java.time.LocalDateTime;

import static br.com.zera.exception.ErroServlet.exibirErro;
import static br.com.zera.exception.ErroServlet.voltarAoFormulario;
import static br.com.zera.regex.Constants.ERROR_PAGE;

/**
 * Servlet responsável pelo início da recuperação de senha do administrador.
 * Valida o e-mail, gera o código de verificação e realiza seu envio.
 *
 * @author Pedro Rufino
 * @since 2026-10-30
 */
@WebServlet(name = "ForgotSenhaAdmin", value = "/admin/senha/esqueci")
public class ForgotSenhaAdminServlet extends HttpServlet {

    private static final String PAGINA = "/WEB-INF/login-restrita/email.jsp";
    private static final String PROXIMO = "/admin/senha/codigo";

    static final String EMAIL = "adminRecuperacaoEmail";
    static final String CODIGO = "adminRecuperacaoCodigo";
    static final String EXPIRA = "adminRecuperacaoExpira";
    static final String TENTATIVAS = "adminRecuperacaoTentativas";
    static final String VALIDADA = "adminRecuperacaoValidada";

    /**
     * Exibe o formulário de recuperação de senha.
     *
     * @param request requisição HTTP
     * @param response resposta HTTP
     * @throws ServletException se ocorrer erro no encaminhamento
     * @throws IOException se ocorrer erro de entrada ou saída
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher(PAGINA).forward(request, response);
    }

    /**
     * Valida o e-mail e envia o código de recuperação.
     *
     * @param request requisição contendo o e-mail
     * @param response resposta HTTP
     * @throws ServletException se ocorrer erro no encaminhamento
     * @throws IOException se ocorrer erro de entrada ou saída
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String email = request.getParameter("email");
        request.setAttribute("email", email);

        // garante que caracteres especiais cheguem corretos
        request.setCharacterEncoding("UTF-8");

        try {
            if (email == null || !Regex.validarEmail(email.trim())) {
                voltarAoFormulario(request, response, "Email inválido, ele precisa conter @ e o domínio.", PAGINA);
                return;
            }

            email = email.trim();
            HttpSession session = request.getSession();

            session.removeAttribute(EMAIL);
            session.removeAttribute(CODIGO);
            session.removeAttribute(EXPIRA);
            session.removeAttribute(TENTATIVAS);
            session.removeAttribute(VALIDADA);

            Admin admin;
            try {
                admin = new AdminDAO().findByEmail(email);
            } catch (NotFoundException nfe) {
                voltarAoFormulario(request, response, "Nenhuma conta encontrada com esse e-mail.", PAGINA);
                return;
            }

            String codigo = CodigoVerificacao.gerar();

            session.setAttribute(EMAIL, email);
            session.setAttribute(CODIGO, codigo);
            session.setAttribute(EXPIRA, LocalDateTime.now().plusMinutes(CodigoVerificacao.MINUTOS_VALIDADE));
            session.setAttribute(TENTATIVAS, 0);

            EnviarEmail.enviar(admin.getEmail(), "Código para redefinir sua senha - Zera",
                    CodigoVerificacao.montarEmailRecuperacao(admin.getNome(), codigo));

            response.sendRedirect(request.getContextPath() + PROXIMO);

        } catch (EmailFailedException efe) {
            efe.printStackTrace();
            exibirErro(request, response, "Não foi possível enviar o e-mail. Tente novamente mais tarde.", ERROR_PAGE);

        } catch (ConnectionFailedException cfe) {
            cfe.printStackTrace();
            exibirErro(request, response, cfe, ERROR_PAGE);

        } catch (Exception e) {
            e.printStackTrace();
            exibirErro(request, response, "Erro inesperado encontrado", ERROR_PAGE);
        }
    }
}
