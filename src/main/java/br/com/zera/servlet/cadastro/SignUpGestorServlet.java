package br.com.zera.servlet.cadastro;

import br.com.zera.config.HashSenha;
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

import java.io.IOException;

import static br.com.zera.exception.ErroServlet.exibirErro;
import static br.com.zera.exception.ErroServlet.voltarAoFormulario;
import static br.com.zera.regex.Constants.CADASTRO_GESTOR;
import static br.com.zera.regex.Constants.ERROR_PAGE;

/**
 * Passo 1 de 5 do cadastro: dados do gestor.
 *
 * Valida nome, e-mail, telefone e senha e guarda o gestor na sessão, com a
 * senha já em hash. Nada é gravado no banco neste passo: o cadastro só é
 * salvo no {@link ConfirmCadastroServlet}.
 *
 * @author Pedro Rufino
 * @since 2026-10-16
 */
@WebServlet(name = "SignUpGestor", value = "/cadastro/gestor")
public class SignUpGestorServlet extends HttpServlet {

    /** Página do formulário deste passo */
    private static final String PAGINA = "/WEB-INF/cadastro/gestor.jsp";

    /** Próximo passo do cadastro */
    private static final String PROXIMO = "/cadastro/organizacao";

    /**
     * Processa GET para exibir o formulário de dados do gestor.
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
     * Processa POST com os dados do gestor e avança para o passo da organização.
     *
     * @param request objeto HttpServletRequest com nome, email, telefone, senha e confirmacao
     * @param response objeto HttpServletResponse para redirecionamento ou erro
     * @throws ServletException caso ocorra erro ao voltar para o formulário
     * @throws IOException caso ocorra erro de entrada/saída ao responder
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // garante que caracteres especiais cheguem corretos
        request.setCharacterEncoding("UTF-8");

        String nome = request.getParameter("nome");
        if (nome == null || nome.trim().isEmpty()) {
            exibirErro(request, response, "Informe o nome.", ERROR_PAGE);
            return;
        }
        nome = nome.trim();

        String email = request.getParameter("email");
        if (email == null || email.trim().isEmpty()) {
            exibirErro(request, response, "Informe o email.", ERROR_PAGE);
            return;
        }
        email = email.trim();

        String telefone = request.getParameter("telefone");
        if (telefone == null || telefone.trim().isEmpty()) {
            exibirErro(request, response, "Informe o telefone.", ERROR_PAGE);
            return;
        }
        telefone = telefone.trim();

        String senha = request.getParameter("senha");
        String confirmacao = request.getParameter("confirmacao");

        // devolve o que foi digitado para o formulário, caso precise voltar
        request.setAttribute("nome", nome);
        request.setAttribute("email", email);
        request.setAttribute("telefone", telefone);

        try {
            if (nome == null) {
                voltarAoFormulario(request, response, "Informe o nome completo.", PAGINA);
                return;
            }
            if (email == null || !Regex.validarEmail(email)) {
                voltarAoFormulario(request, response, "Email inválido, ele precisa conter @ e o domínio.", PAGINA);
                return;
            }
            if (telefone == null || !Regex.validarTel(telefone)) {
                voltarAoFormulario(request, response, "Telefone inválido, ele precisa conter o DDD + nove números.", PAGINA);
                return;
            }
            if (!Regex.validarSenha(senha)) {
                voltarAoFormulario(request, response, "A senha precisa ter pelo menos 8 caracteres, um número e um caractere especial.", PAGINA);
                return;
            }
            if (!senha.equals(confirmacao)) {
                voltarAoFormulario(request, response, "A confirmação está diferente da senha informada.", PAGINA);
                return;
            }
            if (emailJaCadastrado(email)) {
                voltarAoFormulario(request, response, "Já existe um cadastro com esse e-mail.", PAGINA);
                return;
            }

            // a unidade ainda não existe, então o código da unidade fica 0 até a confirmação
            Gestor gestor = new Gestor(nome, email, HashSenha.gerarHash(senha), telefone, 0);
            request.getSession().setAttribute(CADASTRO_GESTOR, gestor);

            response.sendRedirect(request.getContextPath() + PROXIMO);

        } catch (ConnectionFailedException cfe) {
            cfe.printStackTrace();
            exibirErro(request, response, cfe, ERROR_PAGE);
        } catch (Exception e) {
            e.printStackTrace();
            exibirErro(request, response, "Erro inesperado encontrado", ERROR_PAGE);
        }
    }

    /**
     * Verifica se já existe um gestor com o e-mail informado.
     *
     * @param email e-mail digitado
     * @return true se o e-mail já estiver em uso
     */
    private boolean emailJaCadastrado(String email) {
        try {
            new GestorDAO().findByEmail(email);
            return true;
        } catch (NotFoundException nfe) {
            return false;
        }
    }
}
