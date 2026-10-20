package br.com.zera.servlet.cadastro;

import br.com.zera.model.Endereco;
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
import static br.com.zera.regex.Constants.CADASTRO_ENDERECO;
import static br.com.zera.regex.Constants.CADASTRO_GESTOR;
import static br.com.zera.regex.Constants.CADASTRO_ORGANIZACAO;
import static br.com.zera.regex.Constants.CADASTRO_UNIDADE;
import static br.com.zera.regex.Constants.ERROR_PAGE;

/**
 * Passo 4 de 5 do cadastro: endereço da unidade.
 *
 * Valida o endereço e guarda na sessão. Se a organização for nova, o próximo
 * passo é a escolha do plano; se a organização já existia, vai direto para a
 * confirmação, porque ela já tem assinatura.
 *
 * @author Pedro Rufino
 * @since 2026-10-16
 */
@WebServlet(name = "CadastroEndereco", value = "/cadastro/endereco")
public class SignUpEnderecoServlet extends HttpServlet {

    /** Página do formulário deste passo */
    private static final String PAGINA = "/WEB-INF/cadastro/endereco.jsp";

    /** Primeiro passo, para quem chegar aqui sem ter passado pelos anteriores */
    private static final String INICIO = "/cadastro/gestor";

    /**
     * Processa GET para exibir o formulário de endereço.
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
        request.getRequestDispatcher(PAGINA).forward(request, response);
    }

    /**
     * Processa POST com o endereço e avança para o plano ou para a confirmação.
     *
     * @param request objeto HttpServletRequest com logradouro, numero, cep, estado, cidade e bairro
     * @param response objeto HttpServletResponse para redirecionamento ou erro
     * @throws ServletException caso ocorra erro ao voltar para o formulário
     * @throws IOException caso ocorra erro de entrada/saída ao responder
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        if (faltaPassoAnterior(session)) {
            response.sendRedirect(request.getContextPath() + INICIO);
            return;
        }

        String logradouro = request.getParameter("logradouro");
        if (logradouro == null || logradouro.trim().isEmpty()) {
            exibirErro(request, response, "Informe o logradouro.", ERROR_PAGE);
            return;
        }
        logradouro = logradouro.trim();

        String numeroTexto = request.getParameter("numero");
        if (numeroTexto == null || numeroTexto.trim().isEmpty()) {
            exibirErro(request, response, "Informe o numero.", ERROR_PAGE);
            return;
        }
        numeroTexto = numeroTexto.trim();

        String cep = request.getParameter("cep");
        if (cep == null || cep.trim().isEmpty()) {
            exibirErro(request, response, "Informe o cep.", ERROR_PAGE);
            return;
        }
        cep = cep.trim();

        String estado = request.getParameter("estado");
        if (estado == null || estado.trim().isEmpty()) {
            exibirErro(request, response, "Informe o estado.", ERROR_PAGE);
            return;
        }
        estado = estado.trim();

        String cidade = request.getParameter("cidade");
        if (cidade == null || cidade.trim().isEmpty()) {
            exibirErro(request, response, "Informe a cidade.", ERROR_PAGE);
            return;
        }
        cidade = cidade.trim();

        String bairro = request.getParameter("bairro");
        if (bairro == null || bairro.trim().isEmpty()) {
            exibirErro(request, response, "Informe o bairro.", ERROR_PAGE);
            return;
        }
        bairro = bairro.trim();

        request.setAttribute("logradouro", logradouro);
        request.setAttribute("numero", numeroTexto);
        request.setAttribute("cep", cep);
        request.setAttribute("estado", estado);
        request.setAttribute("cidade", cidade);
        request.setAttribute("bairro", bairro);

        try {
            if (logradouro == null || cidade == null || bairro == null || estado == null) {
                voltarAoFormulario(request, response, "Preencha todos os campos do endereço.", PAGINA);
                return;
            }
            if (cep == null || !Regex.validarCEP(cep)) {
                voltarAoFormulario(request, response, "CEP inválido, ele precisa conter 8 números.", PAGINA);
                return;
            }

            int numero;
            try {
                numero = Integer.parseInt(numeroTexto);
            } catch (NumberFormatException nfe) {
                voltarAoFormulario(request, response, "Número inválido.", PAGINA);
                return;
            }

            // a unidade ainda não existe no banco, então o código da unidade fica 0 até a confirmação
            session.setAttribute(CADASTRO_ENDERECO,
                    new Endereco(bairro, numero, cep, logradouro, cidade, estado.toUpperCase(), 0));

            // organização nova escolhe plano; organização existente já tem assinatura
            Organizacao organizacao = (Organizacao) session.getAttribute(CADASTRO_ORGANIZACAO);
            String proximo = organizacao.getCodigo() == 0 ? "/cadastro/plano" : "/cadastro/confirmar";

            response.sendRedirect(request.getContextPath() + proximo);

        } catch (Exception e) {
            e.printStackTrace();
            exibirErro(request, response, "Erro inesperado encontrado", ERROR_PAGE);
        }
    }

    /**
     * Confere se os passos anteriores (gestor, organização e unidade) já foram preenchidos.
     *
     * @param session sessão atual
     * @return true se faltar algum passo anterior
     */
    private boolean faltaPassoAnterior(HttpSession session) {
        return session.getAttribute(CADASTRO_GESTOR) == null
                || session.getAttribute(CADASTRO_ORGANIZACAO) == null
                || session.getAttribute(CADASTRO_UNIDADE) == null;
    }
}
