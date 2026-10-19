package br.com.zera.servlet.admin;

import br.com.zera.config.HashSenha;
import br.com.zera.dao.AdminDAO;
import br.com.zera.exception.ConnectionFailedException;
import br.com.zera.model.Admin;
import br.com.zera.regex.Regex;
import br.com.zera.util.Sessao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static br.com.zera.exception.ErroServlet.exibirErro;
import static br.com.zera.exception.ErroServlet.voltarAoFormulario;
import static br.com.zera.regex.Constants.ERROR_PAGE;

/**
 * Servlet responsável por cadastrar um novo admin.
 *
 * Apenas um admin logado pode cadastrar outro admin.
 * A senha é obrigatória e é salva com hash.
 *
 * @author Pedro Rufino
 * @since 2026-10-19
 */
@WebServlet(name = "InsertAdmin", value = "/admin/inserir")
public class InsertAdminServlet extends HttpServlet {

    /** Página do formulário de novo admin */
    private static final String PAGINA = "/WEB-INF/admin/novo-admin.jsp";

    /**
     * Processa GET para exibir o formulário de novo admin.
     *
     * @param request objeto HttpServletRequest da requisição
     * @param response objeto HttpServletResponse usado para exibir a página ou o erro
     * @throws ServletException caso ocorra erro ao encaminhar para a página
     * @throws IOException caso ocorra erro de entrada/saída ao responder
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // só o admin pode fazer isso
        if (Sessao.admin(request) == null) {
            exibirErro(request, response, "Acesso permitido apenas ao administrador.", ERROR_PAGE);
            return;
        }
        request.getRequestDispatcher(PAGINA).forward(request, response);
    }

    /**
     * Processa POST para inserir um novo admin.
     *
     * @param request objeto HttpServletRequest com nome, email, senha e confirmacao
     * @param response objeto HttpServletResponse para redirecionamento ou erro
     * @throws ServletException caso ocorra erro ao voltar para o formulário
     * @throws IOException caso haja um erro de input/output (entrada/saída)
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        try {
            // só o admin pode fazer isso
            if (Sessao.admin(request) == null) {
                exibirErro(request, response, "Acesso permitido apenas ao administrador.", ERROR_PAGE);
                return;
            }

            // lê o nome
            String nomeTexto = request.getParameter("nome");
            String nome = null;
            if (nomeTexto != null && !nomeTexto.isBlank()) {
                nome = nomeTexto.trim();
            }
            // lê o e-mail
            String emailTexto = request.getParameter("email");
            String email = null;
            if (emailTexto != null && !emailTexto.isBlank()) {
                email = emailTexto.trim();
            }
            // lê a senha
            String senha = request.getParameter("senha");
            String confirmacao = request.getParameter("confirmacao");

            // devolve nome e e-mail para a tela, caso precise voltar ao formulário
            request.setAttribute("nome", nome);
            request.setAttribute("email", email);

            if (nome == null || !Regex.validarNome(nome)) {
                voltarAoFormulario(request, response, "Informe o nome completo.", PAGINA);
                return;
            }
            if (email == null || !Regex.validarEmail(email)) {
                voltarAoFormulario(request, response, "Email inválido, ele precisa conter @ e o domínio.", PAGINA);
                return;
            }
            // a senha é obrigatória
            if (!Regex.validarSenha(senha)) {
                voltarAoFormulario(request, response,
                        "A senha precisa ter pelo menos 8 caracteres, um número e um caractere especial.", PAGINA);
                return;
            }
            if (!senha.equals(confirmacao)) {
                voltarAoFormulario(request, response, "A confirmação está diferente da senha informada.", PAGINA);
                return;
            }

            AdminDAO dao = new AdminDAO();
            // o e-mail é o login, então não pode repetir
            if (dao.existsByEmail(email)) {
                voltarAoFormulario(request, response, "Já existe um admin com esse e-mail.", PAGINA);
                return;
            }

            // a senha é salva com hash
            dao.insert(new Admin(nome, email, HashSenha.gerarHash(senha)));

            // redirect
            response.sendRedirect(request.getContextPath() + "/admin/lista");

        } catch (ConnectionFailedException cfe) {
            cfe.printStackTrace();
            exibirErro(request, response, cfe, ERROR_PAGE);
        } catch (Exception e) {
            e.printStackTrace();
            exibirErro(request, response, "Erro inesperado encontrado", ERROR_PAGE);
        }
    }
}
