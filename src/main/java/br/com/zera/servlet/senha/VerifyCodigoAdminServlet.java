
package br.com.zera.servlet.senha;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDateTime;

import static br.com.zera.exception.ErroServlet.voltarAoFormulario;
import static br.com.zera.servlet.senha.ForgotSenhaAdminServlet.*;

/**
 * Servlet responsável por validar o código de recuperação do administrador.
 * Verifica a validade do código e controla as tentativas.
 *
 * @author Pedro Rufino
 * @since 2026-10-30
 *
 */
@WebServlet(name = "VerifyCodigoAdmin", value = "/admin/senha/codigo")
public class VerifyCodigoAdminServlet extends HttpServlet {

    private static final String PAGINA = "/WEB-INF/login-restrita/codigo.jsp";
    private static final String PROXIMO = "/admin/senha/nova";
    private static final String INICIO = "/admin/senha/esqueci";
    private static final int MAX_TENTATIVAS = 5;

    /**
     * Exibe a página de verificação quando existe recuperação em andamento.
     *
     * @param request requisição HTTP
     * @param response resposta HTTP
     * @throws ServletException se ocorrer erro no encaminhamento
     * @throws IOException se ocorrer erro de entrada ou saída
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        // garante que caracteres especiais cheguem corretos
        request.setCharacterEncoding("UTF-8");

        if (session == null || session.getAttribute(EMAIL) == null) {
            response.sendRedirect(request.getContextPath() + INICIO);
            return;
        }

        request.setAttribute("email", session.getAttribute(EMAIL));
        request.getRequestDispatcher(PAGINA).forward(request, response);
    }

    /**
     * Confere o código informado e libera a criação de uma nova senha.
     *
     * @param request requisição contendo o código
     * @param response resposta HTTP
     * @throws ServletException se ocorrer erro no encaminhamento
     * @throws IOException se ocorrer erro de entrada ou saída
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute(EMAIL) == null) {
            response.sendRedirect(request.getContextPath() + INICIO);
            return;
        }

        request.setAttribute("email", session.getAttribute(EMAIL));

        String codigoCerto = (String) session.getAttribute(CODIGO);
        LocalDateTime expira = (LocalDateTime) session.getAttribute(EXPIRA);
        Integer tentativas = (Integer) session.getAttribute(TENTATIVAS);

        if (tentativas == null) tentativas = 0;

        if (tentativas >= MAX_TENTATIVAS) {
            voltarAoFormulario(request, response, "Muitas tentativas erradas. Peça um novo código.", PAGINA);
            return;
        }

        if (codigoCerto == null || expira == null || !LocalDateTime.now().isBefore(expira)) {
            session.removeAttribute(CODIGO);
            session.removeAttribute(EXPIRA);
            voltarAoFormulario(request, response, "O código está incorreto ou expirou.", PAGINA);
            return;
        }

        String codigoDigitado = request.getParameter("codigo");

        if (codigoDigitado == null || !codigoDigitado.trim().equals(codigoCerto)) {
            session.setAttribute(TENTATIVAS, tentativas + 1);
            voltarAoFormulario(request, response, "O código está incorreto.", PAGINA);
            return;
        }

        session.removeAttribute(CODIGO);
        session.removeAttribute(EXPIRA);
        session.removeAttribute(TENTATIVAS);
        session.setAttribute(VALIDADA, Boolean.TRUE);

        response.sendRedirect(request.getContextPath() + PROXIMO);
    }
}
