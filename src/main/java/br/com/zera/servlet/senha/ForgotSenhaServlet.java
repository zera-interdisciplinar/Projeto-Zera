package br.com.zera.servlet.senha;

import br.com.zera.dao.GestorDAO;
import br.com.zera.email.CodigoVerificacao;
import br.com.zera.email.EnviarEmail;
import br.com.zera.exception.ConnectionFailedException;
import br.com.zera.exception.EmailFailedException;
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
import java.time.LocalDateTime;

import static br.com.zera.exception.ErroServlet.exibirErro;
import static br.com.zera.exception.ErroServlet.voltarAoFormulario;
import static br.com.zera.regex.Constants.ERROR_PAGE;
import static br.com.zera.regex.Constants.RECUPERACAO_CODIGO;
import static br.com.zera.regex.Constants.RECUPERACAO_EMAIL;
import static br.com.zera.regex.Constants.RECUPERACAO_EXPIRA;
import static br.com.zera.regex.Constants.RECUPERACAO_TENTATIVAS;
import static br.com.zera.regex.Constants.RECUPERACAO_VALIDADA;

/**
 * Primeiro passo do "Esqueci minha senha": o gestor informa o e-mail
 * e recebe um código de verificação.
 *
 * Se o e-mail estiver cadastrado, gera um código de 6 dígitos, guarda na sessão
 * com o horário de validade e envia por e-mail. Se não estiver, segue para a
 * tela do código do mesmo jeito, sem enviar nada, para não revelar quais
 * e-mails estão cadastrados.
 *
 * @author Pedro Rufino
 * @since 2026-10-16
 */
@WebServlet(name = "ForgotSenha", value = "/senha/esqueci")
public class ForgotSenhaServlet extends HttpServlet {

    /** Página do formulário de e-mail */
    private static final String PAGINA = "/WEB-INF/login/email.jsp";

    /** Próximo passo: digitar o código */
    private static final String PROXIMO = "/senha/codigo";

    /**
     * Processa GET para exibir o formulário de e-mail.
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
     * Processa POST com o e-mail, gera o código e envia por e-mail.
     *
     * @param request objeto HttpServletRequest com o parâmetro email
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
        request.setAttribute("email", email);

        try {
            if (email == null || !Regex.validarEmail(email.trim())) {
                voltarAoFormulario(request, response, "Email inválido, ele precisa conter @ e o domínio.", PAGINA);
                return;
            }
            email = email.trim();

            // começa uma recuperação nova, apagando qualquer tentativa anterior
            HttpSession session = request.getSession();
            session.removeAttribute(RECUPERACAO_VALIDADA);
            session.removeAttribute(RECUPERACAO_CODIGO);
            session.setAttribute(RECUPERACAO_EMAIL, email);
            session.setAttribute(RECUPERACAO_TENTATIVAS, 0);

            Gestor gestor = buscarGestor(email);

            // e-mail não cadastrado ele volta para o formulário com o aviso
            if (gestor == null) {
                voltarAoFormulario(request, response, "Nenhuma conta encontrada com esse e-mail.", PAGINA);
                return;
            }
            // e-mail cadastrado
            String codigo = CodigoVerificacao.gerar();
            session.setAttribute(RECUPERACAO_CODIGO, codigo);
            session.setAttribute(RECUPERACAO_EXPIRA,
                    LocalDateTime.now().plusMinutes(CodigoVerificacao.MINUTOS_VALIDADE));

            EnviarEmail.enviar(gestor.getEmail(), "Código para redefinir sua senha - Zera",
                    CodigoVerificacao.montarEmailRecuperacao(gestor.getNome(), codigo));

            response.sendRedirect(request.getContextPath() + PROXIMO);

        } catch (EmailFailedException efe) {
            efe.printStackTrace();
            exibirErro(request, response, "Não foi possível enviar o e-mail. Tente novamente mais tarde.", ERROR_PAGE);
        } catch (ConnectionFailedException cfe) {
            cfe.printStackTrace();
            exibirErro(request, response, cfe, ERROR_PAGE);
        } catch (Exception e) {
            e.printStackTrace();
            exibirErro(request, response, "Erro inesperado encontrado", ERROR_PAGE);
        }
    }

    /**
     * Busca o gestor pelo e-mail.
     *
     * @param email e-mail digitado
     * @return o gestor encontrado, ou null se o e-mail não estiver cadastrado
     */
    private Gestor buscarGestor(String email) {
        try {
            return new GestorDAO().findByEmail(email);
        } catch (NotFoundException nfe) {
            return null;
        }
    }
}