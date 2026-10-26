package br.com.zera.servlet.senha;

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
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import static br.com.zera.exception.ErroServlet.exibirErro;
import static br.com.zera.exception.ErroServlet.voltarAoFormulario;
import static br.com.zera.regex.Constants.ERROR_PAGE;
import static br.com.zera.regex.Constants.RECUPERACAO_EMAIL;
import static br.com.zera.regex.Constants.RECUPERACAO_VALIDADA;

/**
 * Último passo do "Esqueci minha senha": o gestor cria a nova senha.
 *
 * Só funciona depois que o código enviado por e-mail foi confirmado no
 * {@link VerifyCodigoServlet}. Grava a nova senha com hash e mostra a tela
 * de "Senha alterada".
 *
 * @author Pedro Rufino
 * @since 2026-10-16
 */
@WebServlet(name = "ResetSenha", value = "/senha/nova")
public class ResetSenhaServlet extends HttpServlet {

    /** Página do formulário de nova senha */
    private static final String PAGINA = "/WEB-INF/login/nova-senha.jsp";

    /** Página de confirmação de que a senha foi alterada */
    private static final String PAGINA_SUCESSO = "/WEB-INF/login/senha-alterada.jsp";

    /** Primeiro passo, para quem chegar aqui sem ter confirmado o código */
    private static final String INICIO = "/senha/esqueci";


    /**
     * Processa GET para exibir o formulário de nova senha.
     *
     * @param request objeto HttpServletRequest da requisição
     * @param response objeto HttpServletResponse usado para exibir a página
     * @throws ServletException caso ocorra erro ao encaminhar para a página
     * @throws IOException caso ocorra erro de entrada/saída ao responder
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!dadosConfirmados(request.getSession())) {
            response.sendRedirect(request.getContextPath() + INICIO);
            return;
        }
        request.getRequestDispatcher(PAGINA).forward(request, response);
    }

    /**
     * Processa POST com a nova senha e grava no banco.
     *
     * @param request objeto HttpServletRequest com senha e confirmacao
     * @param response objeto HttpServletResponse para a página de sucesso ou erro
     * @throws ServletException caso ocorra erro ao encaminhar para a página
     * @throws IOException caso ocorra erro de entrada/saída ao responder
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // garante que caracteres especiais cheguem corretos
        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        if (!dadosConfirmados(session)) {
            response.sendRedirect(request.getContextPath() + INICIO);
            return;
        }

        String senha = request.getParameter("senha");
        String confirmacao = request.getParameter("confirmacao");

        try {
            // validarSenha devolve false se a senha for null
            if (!Regex.validarSenha(senha)) {
                voltarAoFormulario(request, response,
                        "A senha precisa ter pelo menos 8 caracteres, um número e um caractere especial.", PAGINA);
                return;
            }
            if (!senha.equals(confirmacao)) {
                voltarAoFormulario(request, response, "A confirmação está diferente da senha informada.", PAGINA);
                return;
            }

            // o e-mail vem da sessão, para ninguém trocar a senha de outra pessoa
            String email = (String) session.getAttribute(RECUPERACAO_EMAIL);
            GestorDAO dao = new GestorDAO();
            Gestor gestor = dao.findByEmail(email);

            // grava o hash da nova senha
            gestor.setSenha(HashSenha.gerarHash(senha));
            dao.update(gestor);

            // encerra o fluxo de recuperação
            session.removeAttribute(RECUPERACAO_EMAIL);
            session.removeAttribute(RECUPERACAO_VALIDADA);

            request.getRequestDispatcher(PAGINA_SUCESSO).forward(request, response);

        } catch (NotFoundException nfe) {
            nfe.printStackTrace();
            exibirErro(request, response, "Cadastro não encontrado.", ERROR_PAGE);
        } catch (ConnectionFailedException cfe) {
            cfe.printStackTrace();
            exibirErro(request, response, cfe, ERROR_PAGE);
        } catch (Exception e) {
            e.printStackTrace();
            exibirErro(request, response, "Erro inesperado encontrado", ERROR_PAGE);
        }
    }

    /**
     * Confere se o código de verificação já foi confirmado nesta sessão.
     *
     * @param session sessão atual
     * @return true se os dados foram confirmados
     */
    private boolean dadosConfirmados(HttpSession session) {
        return Boolean.TRUE.equals(session.getAttribute(RECUPERACAO_VALIDADA))
                && session.getAttribute(RECUPERACAO_EMAIL) != null;
    }
}