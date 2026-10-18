package br.com.zera.servlet.cadastro;

import br.com.zera.dao.OrganizacaoDAO;
import br.com.zera.exception.ConnectionFailedException;
import br.com.zera.exception.NotFoundException;
import br.com.zera.model.Organizacao;
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
import static br.com.zera.regex.Constants.CADASTRO_GESTOR;
import static br.com.zera.regex.Constants.CADASTRO_ORGANIZACAO;
import static br.com.zera.regex.Constants.ERROR_PAGE;

/**
 * Passo 2 de 5 do cadastro: dados de uma organização nova.
 *
 * Valida nome, e-mail e CNPJ e guarda a organização na sessão. Se o usuário
 * já tiver uma organização cadastrada, ele usa o link "Já possui organização?",
 * que leva ao {@link SearchOrganizacaoServlet}.
 *
 * @author Pedro Rufino
 * @since 2026-10-16
 */
@WebServlet(name = "SignUpOrganizacao", value = "/cadastro/organizacao")
public class SignUpOrganizacaoServlet extends HttpServlet {

    /** Página do formulário deste passo */
    private static final String PAGINA = "/WEB-INF/cadastro/organizacao.jsp";

    /** Próximo passo do cadastro */
    private static final String PROXIMO = "/cadastro/unidade";

    /** Primeiro passo, para quem chegar aqui sem ter passado por ele */
    private static final String INICIO = "/cadastro/gestor";

    /**
     * Processa GET para exibir o formulário de dados da organização.
     *
     * @param request objeto HttpServletRequest da requisição
     * @param response objeto HttpServletResponse usado para exibir a página
     * @throws ServletException caso ocorra erro ao encaminhar para a página
     * @throws IOException caso ocorra erro de entrada/saída ao responder
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (request.getSession().getAttribute(CADASTRO_GESTOR) == null) {
            response.sendRedirect(request.getContextPath() + INICIO);
            return;
        }
        request.getRequestDispatcher(PAGINA).forward(request, response);
    }

    /**
     * Processa POST com os dados da organização e avança para o passo da unidade.
     *
     * @param request objeto HttpServletRequest com nome, email e cnpj
     * @param response objeto HttpServletResponse para redirecionamento ou erro
     * @throws ServletException caso ocorra erro ao voltar para o formulário
     * @throws IOException caso ocorra erro de entrada/saída ao responder
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // garante que caracteres especiais cheguem corretos
        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        if (session.getAttribute(CADASTRO_GESTOR) == null) {
            response.sendRedirect(request.getContextPath() + INICIO);
            return;
        }

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

        String cnpj = request.getParameter("cnpj");
        if (cnpj == null || cnpj.trim().isEmpty()) {
            exibirErro(request, response, "Informe o cnpj.", ERROR_PAGE);
            return;
        }
        cnpj = cnpj.trim();

        request.setAttribute("nome", nome);
        request.setAttribute("email", email);
        request.setAttribute("cnpj", cnpj);

        try {
            if (nome == null) {
                voltarAoFormulario(request, response, "Informe o nome da organização.", PAGINA);
                return;
            }
            if (email == null || !Regex.validarEmail(email)) {
                voltarAoFormulario(request, response, "Email inválido, ele precisa conter @ e o domínio.", PAGINA);
                return;
            }
            if (cnpj == null || !Regex.validarCNPJ(cnpj)) {
                voltarAoFormulario(request, response, "CNPJ inválido, ele precisa conter 14 números.", PAGINA);
                return;
            }
            if (cnpjJaCadastrado(cnpj)) {
                voltarAoFormulario(request, response,
                        "Já existe uma organização com esse CNPJ. Use a opção \"Já possui organização?\".", PAGINA);
                return;
            }

            // organização nova: o código fica 0 até ser gravada na confirmação
            session.setAttribute(CADASTRO_ORGANIZACAO, new Organizacao(cnpj, nome, email));

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
     * Verifica se já existe uma organização com o CNPJ informado.
     *
     * @param cnpj CNPJ digitado
     * @return true se o CNPJ já estiver cadastrado
     */
    private boolean cnpjJaCadastrado(String cnpj) {
        try {
            new OrganizacaoDAO().findByCnpj(cnpj);
            return true;
        } catch (NotFoundException nfe) {
            return false;
        }
    }
}
