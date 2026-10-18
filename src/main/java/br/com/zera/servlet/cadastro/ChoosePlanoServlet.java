package br.com.zera.servlet.cadastro;

import br.com.zera.dao.PlanoDAO;
import br.com.zera.exception.ConnectionFailedException;
import br.com.zera.exception.NotFoundException;
import br.com.zera.model.Organizacao;
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
import static br.com.zera.regex.Constants.CADASTRO_PLANO;
import static br.com.zera.regex.Constants.CADASTRO_UNIDADE;
import static br.com.zera.regex.Constants.ERROR_PAGE;

/**
 * Passo 5 de 5 do cadastro: escolha do plano.
 *
 * Só é usado quando a organização é nova. Mostra os planos cadastrados e
 * guarda na sessão o código do plano escolhido.
 *
 * @author Pedro Rufino
 * @since 2026-10-16
 */
@WebServlet(name = "ChoosePlano", value = "/cadastro/plano")
public class ChoosePlanoServlet extends HttpServlet {

    /** Página com os cards de planos */
    private static final String PAGINA = "/WEB-INF/cadastro/planos.jsp";

    /** Próximo passo do cadastro */
    private static final String PROXIMO = "/cadastro/confirmar";

    /** Primeiro passo, para quem chegar aqui sem ter passado pelos anteriores */
    private static final String INICIO = "/cadastro/gestor";

    /**
     * Processa GET para exibir os planos disponíveis.
     *
     * @param request objeto HttpServletRequest que recebe a lista de planos
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
        try {
            request.setAttribute("planos", new PlanoDAO().findAll());
            request.getRequestDispatcher(PAGINA).forward(request, response);
        } catch (ConnectionFailedException cfe) {
            cfe.printStackTrace();
            exibirErro(request, response, cfe, ERROR_PAGE);
        }
    }

    /**
     * Processa POST com o plano escolhido e avança para a confirmação.
     *
     * @param request objeto HttpServletRequest com o parâmetro codPlano
     * @param response objeto HttpServletResponse para redirecionamento ou erro
     * @throws ServletException caso ocorra erro ao voltar para o formulário
     * @throws IOException caso ocorra erro de entrada/saída ao responder
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        if (faltaPassoAnterior(session)) {
            response.sendRedirect(request.getContextPath() + INICIO);
            return;
        }

        PlanoDAO dao = new PlanoDAO();

        try {
            // a lista volta junto caso seja preciso mostrar a tela de novo
            request.setAttribute("planos", dao.findAll());

            String codigoParam = request.getParameter("codigo");
            if (codigoParam == null || codigoParam.trim().isEmpty()) {
                exibirErro(request, response, "Código não informado.", ERROR_PAGE);
                return;
            }
            int codPlano = Integer.parseInt(codigoParam.trim()); // se vier "abc", lança NumberFormatException

            // confere se o plano existe, já que o código veio do formulário
            dao.findByCodigo(codPlano);

            session.setAttribute(CADASTRO_PLANO, codPlano);
            response.sendRedirect(request.getContextPath() + PROXIMO);

        } catch (NumberFormatException | NotFoundException e) {
            voltarAoFormulario(request, response, "Plano selecionado inválido.", PAGINA);
        } catch (ConnectionFailedException cfe) {
            cfe.printStackTrace();
            exibirErro(request, response, cfe, ERROR_PAGE);
        } catch (Exception e) {
            e.printStackTrace();
            exibirErro(request, response, "Erro inesperado encontrado", ERROR_PAGE);
        }
    }

    /**
     * Confere se os passos anteriores foram preenchidos e se a organização é nova.
     *
     * @param session sessão atual
     * @return true se faltar algum passo ou se a organização já existia
     */
    private boolean faltaPassoAnterior(HttpSession session) {
        Organizacao organizacao = (Organizacao) session.getAttribute(CADASTRO_ORGANIZACAO);
        return session.getAttribute(CADASTRO_GESTOR) == null
                || organizacao == null
                || organizacao.getCodigo() != 0
                || session.getAttribute(CADASTRO_UNIDADE) == null
                || session.getAttribute(CADASTRO_ENDERECO) == null;
    }
}
