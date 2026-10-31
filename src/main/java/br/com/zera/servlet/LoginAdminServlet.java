package br.com.zera.servlet;

import br.com.zera.config.HashSenha;
import br.com.zera.dao.AdminDAO;
import br.com.zera.exception.ConnectionFailedException;
import br.com.zera.exception.NotFoundException;
import br.com.zera.exception.ZeraException;
import br.com.zera.model.Admin;
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
import static br.com.zera.regex.Constants.SESSAO_ADMIN;

/**
 * Servlet responsável pelo login do admin.
 *
 * É acessado pelo botão de área restrita da landing page, separado do login
 * do gestor. Procura o e-mail na tabela Admin, confere a senha com o hash
 * salvo no banco e, se estiver correta, guarda o admin na sessão.
 *
 * @author Pedro Rufino
 * @since 2026-10-16
 */
@WebServlet(name = "LoginAdmin", value = "/admin/login")
public class LoginAdminServlet extends HttpServlet {

    /** Página com o formulário de login da área restrita */
    private static final String PAGINA_LOGIN = "/WEB-INF/login-restrita/login.jsp";

    /** Primeira tela do admin depois de entrar */
    private static final String HOME_ADMIN = "/admin/home";

    /** Mensagem única para e-mail inexistente e senha errada */
    private static final String MSG_INVALIDO = "E-mail ou senha inválidos.";

    /**
     * Processa GET para exibir a tela de login da área restrita.
     *
     * @param request objeto HttpServletRequest da requisição
     * @param response objeto HttpServletResponse usado para exibir a página
     * @throws ServletException caso ocorra erro ao encaminhar para a página
     * @throws IOException caso ocorra erro de entrada/saída ao responder
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher(PAGINA_LOGIN).forward(request, response);
    }

    /**
     * Processa POST para autenticar o admin com e-mail e senha.
     * Se estiverem corretos, guarda o {@link Admin} na sessão.
     *
     * @param request objeto HttpServletRequest com os parâmetros email e senha
     * @param response objeto HttpServletResponse para redirecionamento ou erro
     * @throws ServletException caso ocorra erro ao encaminhar para a página
     * @throws IOException caso ocorra erro de entrada/saída ao responder
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // garante que caracteres especiais da senha cheguem corretos
        request.setCharacterEncoding("UTF-8");

        String email = request.getParameter("email");
        String senha = request.getParameter("senha");

        try {
            // mantém o e-mail digitado no campo caso precise voltar ao formulário
            request.setAttribute("email", email);

            if (email == null || email.isBlank() || senha == null || senha.isBlank()) {
                voltarAoFormulario(request, response, "Preencha o e-mail e a senha.", PAGINA_LOGIN);
                return;
            }

            Admin admin = buscarAdmin(email.trim());
            if (admin == null || !HashSenha.verificar(senha, admin.getSenha())) {
                voltarAoFormulario(request, response, MSG_INVALIDO, PAGINA_LOGIN);
                return;
            }

            HttpSession session = novaSessao(request);
            session.setAttribute(SESSAO_ADMIN, admin);
            response.sendRedirect(request.getContextPath() + HOME_ADMIN);

        } catch (ConnectionFailedException cfe) {
            cfe.printStackTrace();
            exibirErro(request, response, "Não foi possível conectar ao banco de dados.", ERROR_PAGE);
        } catch (ZeraException ze) {
            ze.printStackTrace();
            exibirErro(request, response, "Cadastro com senha em formato inválido.", ERROR_PAGE);
        } catch (Exception e) {
            e.printStackTrace();
            exibirErro(request, response, "Erro inesperado encontrado", ERROR_PAGE);
        }
    }

    /**
     * Procura um admin pelo e-mail.
     *
     * @param email e-mail digitado
     * @return o {@link Admin} encontrado, ou null se não existir admin com esse e-mail
     */
    private Admin buscarAdmin(String email) {
        try {
            return new AdminDAO().findByEmail(email);
        } catch (NotFoundException nfe) {
            return null;
        }
    }

    /**
     * Descarta a sessão antiga (se houver) e cria uma nova,
     * para que o identificador de sessão mude a cada login.
     *
     * @param request objeto HttpServletRequest da requisição
     * @return a sessão nova
     */
    private HttpSession novaSessao(HttpServletRequest request) {
        HttpSession antiga = request.getSession(false);
        if (antiga != null) {
            antiga.invalidate();
        }
        return request.getSession(true);
    }
}
