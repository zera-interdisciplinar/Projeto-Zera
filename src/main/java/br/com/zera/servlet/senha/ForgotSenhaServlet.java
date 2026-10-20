package br.com.zera.servlet.senha;

import br.com.zera.dao.GestorDAO;
import br.com.zera.exception.ConnectionFailedException;
import br.com.zera.exception.NotFoundException;
import br.com.zera.model.Gestor;
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
import static br.com.zera.regex.Constants.RECUPERACAO_EMAIL;
import static br.com.zera.regex.Constants.RECUPERACAO_VALIDADA;

/**
 * Primeiro passo do "Esqueci minha senha": o gestor informa o e-mail e o
 * telefone cadastrados.
 *
 * Se os dois conferirem com o cadastro, a sessão é liberada e o gestor segue
 * para a tela de nova senha. A mensagem de erro é a mesma para e-mail
 * inexistente e telefone errado, para não revelar quais e-mails estão cadastrados.
 *
 * @author Pedro Rufino
 * @since 2026-10-16
 */
@WebServlet(name = "ForgotSenha", value = "/senha/esqueci")
public class ForgotSenhaServlet extends HttpServlet {

    /** Página do formulário de e-mail e telefone */
    private static final String PAGINA = "/WEB-INF/senha/email.jsp";

    /**
     * Processa GET para exibir o formulário de e-mail e telefone.
     *
     * @param request objeto HttpServletRequest da requisição
     * @param response objeto HttpServletResponse usado para exibir a página
     * @throws ServletException caso ocorra erro ao encaminhar para a página
     * @throws IOException caso ocorra erro de entrada/saída ao responder
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher(PAGINA).forward(request, response);
    }

    /**
     * Processa POST para conferir o e-mail e o telefone do gestor e liberar a troca de senha.
     *
     * @param request objeto HttpServletRequest com os parâmetros email e telefone
     * @param response objeto HttpServletResponse para redirecionamento ou erro
     * @throws ServletException caso ocorra erro ao voltar para o formulário
     * @throws IOException caso ocorra erro de entrada/saída ao responder
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // garante que caracteres especiais cheguem corretos
        request.setCharacterEncoding("UTF-8");

        String email = request.getParameter("email");
        String telefone = request.getParameter("telefone");
        request.setAttribute("email", email);
        request.setAttribute("telefone", telefone);

        try {
            if (email == null || !Regex.validarEmail(email.trim())) {
                voltarAoFormulario(request, response, "Email inválido, ele precisa conter @ e o domínio.", PAGINA);
                return;
            }
            if (telefone == null || telefone.trim().isEmpty()) {
                voltarAoFormulario(request, response, "Informe o telefone cadastrado.", PAGINA);
                return;
            }

            Gestor gestor;
            try {
                gestor = new GestorDAO().findByEmail(email.trim());
            } catch (NotFoundException nfe) {
                // mesma mensagem do telefone errado, para não revelar quais e-mails existem
                voltarAoFormulario(request, response, "E-mail ou telefone não conferem.", PAGINA);
                return;
            }

            // compara só os números
            String telDigitado = telefone.replaceAll("\\D", "");
            String telCadastrado = gestor.getTelefone() == null ? "" : gestor.getTelefone().replaceAll("\\D", "");

            if (!telDigitado.equals(telCadastrado)) {
                voltarAoFormulario(request, response, "E-mail ou telefone não conferem.", PAGINA);
                return;
            }

            // dados conferem, libera a tela de nova senha
            HttpSession session = request.getSession();
            session.setAttribute(RECUPERACAO_EMAIL, gestor.getEmail());
            session.setAttribute(RECUPERACAO_VALIDADA, Boolean.TRUE);

            response.sendRedirect(request.getContextPath() + "/senha/nova");

        } catch (ConnectionFailedException cfe) {
            cfe.printStackTrace();
            exibirErro(request, response, cfe, ERROR_PAGE);
        } catch (Exception e) {
            e.printStackTrace();
            exibirErro(request, response, "Erro inesperado encontrado", ERROR_PAGE);
        }
    }
}