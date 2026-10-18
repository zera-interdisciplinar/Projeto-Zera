package br.com.zera.servlet.gestor;

import br.com.zera.config.HashSenha;
import br.com.zera.dao.GestorDAO;
import br.com.zera.dao.UnidadeDAO;
import br.com.zera.exception.ConnectionFailedException;
import br.com.zera.exception.NotFoundException;
import br.com.zera.model.Gestor;
import br.com.zera.regex.Regex;
import br.com.zera.util.Sessao;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static br.com.zera.exception.ErroServlet.exibirErro;
import static br.com.zera.regex.Constants.ERROR_PAGE;

/**
 * Servlet responsável por cadastrar um gestor pela área do admin.
 *
 * Apenas o admin pode cadastrar gestores: ele escolhe a unidade (codUnidade)
 * e define a senha, que é obrigatória. O primeiro gestor de uma unidade nova
 * é criado pelo fluxo de cadastro (servlet.cadastro), não por aqui.
 *
 * @author Mayte B
 * @since 2026-10-06
 */
@WebServlet(name = "InsertGestor", value = "/gestor/inserir")
public class InsertGestorServlet extends HttpServlet {

    /**
     * Processa POST para inserir um novo gestor.
     *
     * @param request objeto HttpServletRequest com codUnidade, nome, email, telefone, senha e confirmacao
     * @param response objeto HttpServletResponse para redirecionamento ou erro
     * @throws IOException caso haja um erro de input/output (entrada/saída)
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        request.setCharacterEncoding("UTF-8");

        try {
            // só o admin pode fazer isso
            if (Sessao.admin(request) == null) {
                exibirErro(request, response, "Acesso permitido apenas ao administrador.", ERROR_PAGE);
                return;
            }

            // lê a unidade escolhida
            String codUnidadeTexto = request.getParameter("codUnidade");
            Integer codUnidade = null;
            if (codUnidadeTexto != null && !codUnidadeTexto.isBlank()) {
                codUnidade = Integer.parseInt(codUnidadeTexto.trim());
            }
            if (codUnidade == null) {
                exibirErro(request, response, "Selecione a unidade.", ERROR_PAGE);
                return;
            }
            // confere se a unidade existe
            new UnidadeDAO().findByCodigo(codUnidade);

            // lê os campos do formulário
            String nomeTexto = request.getParameter("nome");
            String nome = null;
            if (nomeTexto != null && !nomeTexto.isBlank()) {
                nome = nomeTexto.trim();
            }
            String emailTexto = request.getParameter("email");
            String email = null;
            if (emailTexto != null && !emailTexto.isBlank()) {
                email = emailTexto.trim();
            }
            String telefoneTexto = request.getParameter("telefone");
            String telefone = null;
            if (telefoneTexto != null && !telefoneTexto.isBlank()) {
                telefone = telefoneTexto.trim();
            }
            String senha = request.getParameter("senha");
            String confirmacao = request.getParameter("confirmacao");

            if (nome == null || !Regex.validarNome(nome)) {
                exibirErro(request, response, "Informe o nome completo.", ERROR_PAGE);
                return;
            }
            if (email == null || !Regex.validarEmail(email)) {
                exibirErro(request, response, "Email inválido, ele precisa conter @ e o domínio.", ERROR_PAGE);
                return;
            }
            if (telefone == null || !Regex.validarTel(telefone)) {
                exibirErro(request, response, "Telefone inválido, ele precisa conter o DDD + nove números.", ERROR_PAGE);
                return;
            }
            // a senha é obrigatória (validarSenha devolve false se ela vier null)
            if (!Regex.validarSenha(senha)) {
                exibirErro(request, response,
                        "A senha precisa ter pelo menos 8 caracteres, um número e um caractere especial.", ERROR_PAGE);
                return;
            }
            if (!senha.equals(confirmacao)) {
                exibirErro(request, response, "A confirmação está diferente da senha informada.", ERROR_PAGE);
                return;
            }

            GestorDAO dao = new GestorDAO();
            // o e-mail é o login, então não pode repetir
            if (emailJaCadastrado(dao, email)) {
                exibirErro(request, response, "Já existe um gestor com esse e-mail.", ERROR_PAGE);
                return;
            }

            // a senha é salva com hash
            dao.insert(new Gestor(nome, email, HashSenha.gerarHash(senha), telefone, codUnidade));

            // redirect
            response.sendRedirect(request.getContextPath() + "/gestor/lista");

        } catch (NotFoundException nfe) {
            nfe.printStackTrace();
            exibirErro(request, response, nfe, ERROR_PAGE);
        } catch (NumberFormatException nmfe) {
            exibirErro(request, response, "Unidade inválida.", ERROR_PAGE);
        } catch (ConnectionFailedException cfe) {
            cfe.printStackTrace();
            exibirErro(request, response, cfe, ERROR_PAGE);
        } catch (Exception e) {
            e.printStackTrace();
            exibirErro(request, response, e, ERROR_PAGE);
        }
    }

    /**
     * Verifica se já existe um gestor com o e-mail informado.
     *
     * @param dao DAO de gestor
     * @param email e-mail digitado
     * @return true se o e-mail já estiver cadastrado
     */
    private boolean emailJaCadastrado(GestorDAO dao, String email) {
        try {
            dao.findByEmail(email);
            return true;
        } catch (NotFoundException nfe) {
            return false;
        }
    }
}