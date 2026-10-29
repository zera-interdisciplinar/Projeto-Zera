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
import static br.com.zera.regex.Constants.RECUPERACAO_CODIGO;
import static br.com.zera.regex.Constants.RECUPERACAO_EMAIL;
import static br.com.zera.regex.Constants.RECUPERACAO_EXPIRA;
import static br.com.zera.regex.Constants.RECUPERACAO_TENTATIVAS;
import static br.com.zera.regex.Constants.RECUPERACAO_VALIDADA;

/**
 * Segundo passo do "Esqueci minha senha": o gestor digita o código recebido por e-mail.
 *
 * Se o código estiver certo e dentro da validade, libera a tela de nova senha.
 * Depois de 5 tentativas erradas o código deixa de valer e é preciso pedir outro.
 *
 * @author Pedro Rufino
 * @since 2026-10-21
 */
@WebServlet(name = "VerifyCodigo", value = "/senha/codigo")
public class VerifyCodigoServlet extends HttpServlet {

    /** Página com os campos do código */
    private static final String PAGINA = "/WEB-INF/login/codigo.jsp";

    /** Próximo passo: criar a nova senha */
    private static final String PROXIMO = "/senha/nova";

    /** Primeiro passo, para quem chegar aqui sem ter informado o e-mail */
    private static final String INICIO = "/senha/esqueci";

    /** Quantidade máxima de tentativas erradas */
    private static final int MAX_TENTATIVAS = 5;

    /**
     * Processa GET para exibir a tela do código.
     *
     * @param request objeto HttpServletRequest que recebe o e-mail para mostrar na tela
     * @param response objeto HttpServletResponse usado para exibir a página
     * @throws ServletException caso ocorra erro ao encaminhar para a página
     * @throws IOException caso ocorra erro de entrada/saída ao responder
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        if (session.getAttribute(RECUPERACAO_EMAIL) == null) {
            response.sendRedirect(request.getContextPath() + INICIO);
            return;
        }
        request.setAttribute("email", session.getAttribute(RECUPERACAO_EMAIL));
        request.getRequestDispatcher(PAGINA).forward(request, response);
    }

    /**
     * Processa POST com o código digitado e, se estiver certo, libera a nova senha.
     *
     * @param request objeto HttpServletRequest com o parâmetro codigo
     * @param response objeto HttpServletResponse para redirecionamento ou erro
     * @throws ServletException caso ocorra erro ao voltar para o formulário
     * @throws IOException caso ocorra erro de entrada/saída ao responder
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        if (session.getAttribute(RECUPERACAO_EMAIL) == null) {
            response.sendRedirect(request.getContextPath() + INICIO);
            return;
        }
        // o e-mail aparece de novo na tela se precisar voltar
        request.setAttribute("email", session.getAttribute(RECUPERACAO_EMAIL));

        String codigoCerto = (String) session.getAttribute(RECUPERACAO_CODIGO);
        LocalDateTime expira = (LocalDateTime) session.getAttribute(RECUPERACAO_EXPIRA);
        Integer tentativas = (Integer) session.getAttribute(RECUPERACAO_TENTATIVAS);
        if (tentativas == null) {
            tentativas = 0;
        }

        if (tentativas >= MAX_TENTATIVAS) {
            voltarAoFormulario(request, response,
                    "Muitas tentativas erradas. Peça um novo código.", PAGINA);
            return;
        }

        // codigoCerto é null quando o e-mail não estava cadastrado: nunca vai bater
        if (codigoCerto == null || expira == null || LocalDateTime.now().isAfter(expira)) {
            session.setAttribute(RECUPERACAO_TENTATIVAS, tentativas + 1);
            voltarAoFormulario(request, response, "O código está incorreto ou expirou.", PAGINA);
            return;
        }

        String codigoDigitado = request.getParameter("codigo");
        if (codigoDigitado == null || !codigoDigitado.trim().equals(codigoCerto)) {
            session.setAttribute(RECUPERACAO_TENTATIVAS, tentativas + 1);
            voltarAoFormulario(request, response, "O código está incorreto.", PAGINA);
            return;
        }

        // código certo: o código não serve mais e a nova senha fica liberada
        session.removeAttribute(RECUPERACAO_CODIGO);
        session.removeAttribute(RECUPERACAO_EXPIRA);
        session.removeAttribute(RECUPERACAO_TENTATIVAS);
        session.setAttribute(RECUPERACAO_VALIDADA, Boolean.TRUE);

        response.sendRedirect(request.getContextPath() + PROXIMO);
    }
}