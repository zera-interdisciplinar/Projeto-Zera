package br.com.zera.servlet.cadastro;

import br.com.zera.dao.UnidadeDAO;
import br.com.zera.exception.ConnectionFailedException;
import br.com.zera.exception.NotFoundException;
import br.com.zera.model.Organizacao;
import br.com.zera.model.Unidade;
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
import static br.com.zera.regex.Constants.CADASTRO_UNIDADE;
import static br.com.zera.regex.Constants.ERROR_PAGE;

/**
 * Passo 3 de 5 do cadastro: dados da unidade.
 *
 * Valida o e-mail e o CNPJ da unidade e guarda a unidade na sessão.
 *
 * @author Pedro Rufino
 * @since 2026-10-16
 */
@WebServlet(name = "SignUpUnidade", value = "/cadastro/unidade")
public class SignUpUnidadeServlet extends HttpServlet {

    /** Página do formulário deste passo */
    private static final String PAGINA = "/WEB-INF/cadastro/unidade.jsp";

    /** Próximo passo do cadastro */
    private static final String PROXIMO = "/cadastro/endereco";

    /** Primeiro passo, para quem chegar aqui sem ter passado pelos anteriores */
    private static final String INICIO = "/cadastro/gestor";

    /**
     * Processa GET para exibir o formulário de dados da unidade.
     *
     * @param request objeto HttpServletRequest da requisição
     * @param response objeto HttpServletResponse usado para exibir a página
     * @throws ServletException caso ocorra erro ao encaminhar para a página
     * @throws IOException caso ocorra erro de entrada/saída ao responder
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (faltaPassoAnterior(request.getSession())) {
            response.sendRedirect(request.getContextPath() + INICIO);
            return;
        }
        Organizacao organizacao = (Organizacao) request.getSession().getAttribute(CADASTRO_ORGANIZACAO);
        request.setAttribute("nomeOrganizacao", organizacao.getNome());
        request.getRequestDispatcher(PAGINA).forward(request, response);
    }

    /**
     * Processa POST com os dados da unidade e avança para o passo do endereço.
     *
     * @param request objeto HttpServletRequest com email e cnpj
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
        if (faltaPassoAnterior(session)) {
            response.sendRedirect(request.getContextPath() + INICIO);
            return;
        }

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

        request.setAttribute("email", email);
        request.setAttribute("cnpj", cnpj);

        try {
            if (email == null || !Regex.validarEmail(email)) {
                voltarAoFormulario(request, response, "Email inválido, ele precisa conter @ e o domínio.", PAGINA);
                return;
            }
            if (cnpj == null || !Regex.validarCNPJ(cnpj)) {
                voltarAoFormulario(request, response, "CNPJ inválido, ele precisa conter 14 números.", PAGINA);
                return;
            }
            if (cnpjJaCadastrado(cnpj)) {
                voltarAoFormulario(request, response, "Já existe uma unidade com esse CNPJ.", PAGINA);
                return;
            }

            // a organização ainda pode não existir no banco, então o código fica 0 até a confirmação
            session.setAttribute(CADASTRO_UNIDADE, new Unidade(cnpj, email, 0));

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
     * Confere se os passos anteriores (gestor e organização) já foram preenchidos.
     *
     * @param session sessão atual
     * @return true se faltar algum passo anterior
     */
    private boolean faltaPassoAnterior(HttpSession session) {
        return session.getAttribute(CADASTRO_GESTOR) == null
                || session.getAttribute(CADASTRO_ORGANIZACAO) == null;
    }

    /**
     * Verifica se já existe uma unidade com o CNPJ informado.
     *
     * @param cnpj CNPJ digitado
     * @return true se o CNPJ já estiver cadastrado
     */
    private boolean cnpjJaCadastrado(String cnpj) {
        try {
            new UnidadeDAO().findByCnpj(cnpj);
            return true;
        } catch (NotFoundException nfe) {
            return false;
        }
    }
}
