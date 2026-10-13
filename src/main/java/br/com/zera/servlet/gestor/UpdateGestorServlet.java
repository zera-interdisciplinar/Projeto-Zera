package br.com.zera.servlet.gestor;

import br.com.zera.config.HashSenha;
import br.com.zera.dao.GestorDAO;
import br.com.zera.exception.ConnectionFailedException;
import br.com.zera.exception.NotFoundException;
import br.com.zera.model.Gestor;
import br.com.zera.regex.Regex;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import static br.com.zera.exception.ErroServlet.exibirErro;
import static br.com.zera.regex.Constants.ERROR_PAGE;
import static br.com.zera.regex.Constants.SESSAO_GESTOR;

/**
 * Servlet responsável por atualizar o cadastro de gestor
 *
 * Esta classe recebe os dados enviados via formulário HTTP, realiza a
 * validação das informações e delega as atualizações ao {@link GestorDAO}.
 *
 * @author Mayte B
 * @since 2026-10-06
 */
@WebServlet(name = "UpdateGestor", value = "/gestor/alterarGestor")
public class UpdateGestorServlet extends HttpServlet {

    /**
     * Processa POST para atualizar o Gestor cadastrado baseado no antigo
     *
     * @param request objeto HttpServletRequest contendo os parâmetros da página
     * @param response objeto HttpResponse para redirecionamento ou foward
     * @throws java.io.IOException caso haja um erro de input/output (entrada/saída)
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        // DAO responsável por persistir o gestor no banco de dados
        GestorDAO dao = new GestorDAO();

        try {

            // resgata o gestor logado na sessão
            HttpSession session = request.getSession();
            Gestor gestorLogado = (Gestor) session.getAttribute(SESSAO_GESTOR);
            if (gestorLogado == null) {
                exibirErro(request, response, "Sessão expirada. Faça login novamente.", ERROR_PAGE);
                return;
            }

            // busca o gestor atual pelo próprio código (lança NotFoundException se não existir)
            Gestor gestorExistente = dao.findByCodigo(gestorLogado.getCodigo());

            int codigoGestor = gestorExistente.getCodigo();
            int codUnidade = gestorExistente.getCodUnidade();
            String nome = request.getParameter("nome");
            String email = request.getParameter("email");
            String senha = request.getParameter("senha");
            String telefone = request.getParameter("telefone");

            // exibe página de erro caso o email inserido seja inválido
            if (Regex.validarEmail(email) == false) {
                exibirErro(request, response, "email inválido", ERROR_PAGE);
                return;
            }

            // exibe página de erro caso o telefone inserido seja inválido
            if (Regex.validarTel(telefone) == false) {
                exibirErro(request, response, "telefone inválido", ERROR_PAGE);
                return;
            }

            // senha em branco mantém a atual; senha nova é criptografada
            String senhaFinal = (senha == null || senha.isBlank())
                    ? gestorExistente.getSenha()
                    : HashSenha.gerarHash(senha);

            // cria objeto Gestor com os dados atualizados
            Gestor model = new Gestor(codigoGestor, nome, email, senhaFinal, telefone, codUnidade);

            // atualiza o gestor no banco
            dao.update(model);

            // Redireciona para o perfil do gestor após a atualização bem-sucedida
            response.sendRedirect(request.getContextPath() + "/gestor/perfil");

        } catch (ConnectionFailedException cfe) {
            cfe.printStackTrace();
            exibirErro(request, response, cfe, ERROR_PAGE);
        } catch (NotFoundException nfe) {
            nfe.printStackTrace();
            exibirErro(request, response, nfe, ERROR_PAGE);
        } catch (NumberFormatException nmfe) {
            nmfe.printStackTrace();
            exibirErro(request, response, nmfe, ERROR_PAGE);
        } catch (Exception e) {
            exibirErro(request, response, e, ERROR_PAGE);
        }
    }
}