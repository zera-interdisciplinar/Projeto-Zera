package br.com.zera.servlet.organizacao;

import br.com.zera.dao.OrganizacaoDAO;
import br.com.zera.exception.ConnectionFailedException;
import br.com.zera.exception.NotFoundException;
import br.com.zera.model.Organizacao;
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
 * Servlet responsável por cadastrar uma organização pela área do admin
 * (botão "Nova organização"). O gestor cadastra a organização pelo fluxo
 * de cadastro, no pacote servlet.cadastro.
 *
 * @author Mayte B
 * @since 2026-10-05
 */
@WebServlet(name = "InsertOrganizacao", value = "/organizacao/inserir")
public class InsertOrganizacaoServlet extends HttpServlet {

    /**
     * Processa POST para inserir uma nova organização.
     *
     * @param request objeto HttpServletRequest com nome, email e cnpj
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
            String cnpjTexto = request.getParameter("cnpj");
            String cnpj = null;
            if (cnpjTexto != null && !cnpjTexto.isBlank()) {
                cnpj = cnpjTexto.trim();
            }

            if (nome == null) {
                exibirErro(request, response, "Informe o nome da organização.", ERROR_PAGE);
                return;
            }
            if (email == null || !Regex.validarEmail(email)) {
                exibirErro(request, response, "Email inválido", ERROR_PAGE);
                return;
            }
            if (cnpj == null || !Regex.validarCNPJ(cnpj)) {
                exibirErro(request, response, "CNPJ inválido", ERROR_PAGE);
                return;
            }

            OrganizacaoDAO dao = new OrganizacaoDAO();
            // o CNPJ não pode repetir
            if (cnpjJaCadastrado(dao, cnpj)) {
                exibirErro(request, response, "Já existe uma organização com esse CNPJ.", ERROR_PAGE);
                return;
            }

            dao.insert(new Organizacao(cnpj, nome, email));

            // redirect
            response.sendRedirect(request.getContextPath() + "/organizacao/lista");

        } catch (ConnectionFailedException cfe) {
            cfe.printStackTrace();
            exibirErro(request, response, cfe, ERROR_PAGE);
        } catch (Exception e) {
            e.printStackTrace();
            exibirErro(request, response, e, ERROR_PAGE);
        }
    }

    /**
     * Verifica se já existe uma organização com o CNPJ informado.
     *
     * @param dao DAO de organização
     * @param cnpj CNPJ digitado
     * @return true se o CNPJ já estiver cadastrado
     */
    private boolean cnpjJaCadastrado(OrganizacaoDAO dao, String cnpj) {
        try {
            dao.findByCnpj(cnpj);
            return true;
        } catch (NotFoundException nfe) {
            return false;
        }
    }
}