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
 * Passo 2 de 5 do cadastro (alternativo): organização que já existe.
 *
 * Acessado pelo link "Já possui organização?". O usuário informa o CNPJ e,
 * se a organização for encontrada, a nova unidade será vinculada a ela.
 * Nesse caminho não há escolha de plano, porque a organização já tem assinatura.
 *
 * @author Mayte B
 * @since 2026-10-06
 */
@WebServlet(name = "Organizacao", value = "/cadastro/cnpj")
public class SearchOrganizacaoServlet extends HttpServlet {

    /** Página do formulário deste passo */
    private static final String PAGINA = "/WEB-INF/cadastro/cnpj.jsp";

    /** Próximo passo do cadastro */
    private static final String PROXIMO = "/cadastro/unidade";

    /** Primeiro passo, para quem chegar aqui sem ter passado por ele */
    private static final String INICIO = "/cadastro/gestor";

    /**
     * Processa GET para exibir o formulário de CNPJ da organização.
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
     * Processa POST com o CNPJ e, se a organização existir, avança para o passo da unidade.
     *
     * @param request objeto HttpServletRequest com o parâmetro cnpj
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

        String cnpj = request.getParameter("cnpj");
        if (cnpj == null || cnpj.trim().isEmpty()) {
            exibirErro(request, response, "Informe o cnpj.", ERROR_PAGE);
            return;
        }
        cnpj = cnpj.trim();

        try {
            if (cnpj == null || !Regex.validarCNPJ(cnpj)) {
                voltarAoFormulario(request, response, "CNPJ inválido, ele precisa conter 14 números.", PAGINA);
                return;
            }

            Organizacao organizacao;
            try {
                organizacao = new OrganizacaoDAO().findByCnpj(cnpj);
            } catch (NotFoundException nfe) {
                voltarAoFormulario(request, response, "Nenhuma organização encontrada com esse CNPJ.", PAGINA);
                return;
            }

            // organização existente
            session.setAttribute(CADASTRO_ORGANIZACAO, organizacao);

            response.sendRedirect(request.getContextPath() + PROXIMO);

        } catch (ConnectionFailedException cfe) {
            cfe.printStackTrace();
            exibirErro(request, response, cfe, ERROR_PAGE);
        } catch (Exception e) {
            e.printStackTrace();
            exibirErro(request, response, "Erro inesperado encontrado", ERROR_PAGE);
        }
    }
}
