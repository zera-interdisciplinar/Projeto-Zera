package br.com.zera.servlet.gestor;

import br.com.zera.config.HashSenha;
import br.com.zera.dao.GestorDAO;
import br.com.zera.exception.ConnectionFailedException;
import br.com.zera.exception.NotFoundException;
import br.com.zera.model.*;
import br.com.zera.regex.*;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

import static br.com.zera.exception.ErroServlet.exibirErro;
import static br.com.zera.regex.Constants.ERROR_PAGE;
import static br.com.zera.regex.Constants.SESSAO_GESTOR;

/**
 * Servlet responsável por processar o cadastro de gestores
 *
 * Esta classe recebe os dados enviados via formulário HTTP, realiza a
 * validação das informações e delega a persistência ao {@link GestorDAO}.
 *
 * @author Mayte B
 * @since 2026-10-06
 */
@WebServlet(name = "InsertGestor", value = "/gestor/signIn")
public class InsertGestorServlet extends HttpServlet {

    /**
     * Processa POST para inserir um novo Gestor para a unidade
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
            //resgata o gestor logado na sessão
            HttpSession session = request.getSession();
            Gestor gestorLogado = (Gestor) session.getAttribute(SESSAO_GESTOR);
            if (gestorLogado == null) {
                exibirErro(request, response, "Sessão expirada. Faça login novamente.", ERROR_PAGE);
                return;
            }

            // capta os atributos (colunas do banco) do gestor recebido
            String nome = request.getParameter("nome");
            String email = request.getParameter("email");
            String senha = request.getParameter("senha");
            String telefone = request.getParameter("telefone");
            int codUnidade = unidadeLogada.getCodigo();

            // exibe página de erro caso o email inserido seja inválido
            if (Regex.validarEmail(email) == false) {
                exibirErro(request, response, "Email inválido", ERROR_PAGE);
                return;
            }

            // exibe página de erro caso o telefone inserido seja inválido
            if (Regex.validarTel(telefone) == false) {
                exibirErro(request, response, "Telefone inválido", ERROR_PAGE);
                return;
            }

            if (senha == null || senha.isBlank()) {
                exibirErro(request, response, "Senha não informada.", ERROR_PAGE);
                return;
            }

            // cria objeto Gestor com a senha criptografada
            Gestor model = new Gestor(nome, email, HashSenha.gerarHash(senha), telefone, codUnidade);

            // persiste o gestor no banco
            dao.insert(model);

            // Redireciona para o perfil após a inserção bem-sucedida
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
