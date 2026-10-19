package br.com.zera.servlet.unidade;

import br.com.zera.dao.OrganizacaoDAO;
import br.com.zera.dao.UnidadeDAO;
import br.com.zera.exception.ConnectionFailedException;
import br.com.zera.exception.NotFoundException;
import br.com.zera.model.Unidade;
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
 * Servlet responsável por cadastrar uma unidade pela área do admin.
 *
 * Depois de gravar, leva o admin direto ao formulário de endereço da unidade
 * recém-criada. O gestor cadastra a unidade pelo fluxo de cadastro, no pacote servlet.cadastro.
 *
 * @author Mayte B
 * @since 2026-10-02
 */
@WebServlet(name = "InsertUnidade", value = "/unidade/inserir")
public class InsertUnidadeServlet extends HttpServlet {

    /**
     * Processa POST para inserir uma nova unidade.
     *
     * @param request objeto HttpServletRequest com cnpj, email e codOrganizacao
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
            String cnpjTexto = request.getParameter("cnpj");
            String cnpj = null;
            if (cnpjTexto != null && !cnpjTexto.isBlank()) {
                cnpj = cnpjTexto.trim();
            }
            String emailTexto = request.getParameter("email");
            String email = null;
            if (emailTexto != null && !emailTexto.isBlank()) {
                email = emailTexto.trim();
            }
            String codOrganizacaoTexto = request.getParameter("codOrganizacao");
            Integer codOrganizacao = null;
            if (codOrganizacaoTexto != null && !codOrganizacaoTexto.isBlank()) {
                codOrganizacao = Integer.parseInt(codOrganizacaoTexto.trim());
            }

            if (codOrganizacao == null) {
                exibirErro(request, response, "Selecione a organização.", ERROR_PAGE);
                return;
            }
            if (cnpj == null || !Regex.validarCNPJ(cnpj)) {
                exibirErro(request, response, "CNPJ inválido", ERROR_PAGE);
                return;
            }
            if (email == null || !Regex.validarEmail(email)) {
                exibirErro(request, response, "Email inválido", ERROR_PAGE);
                return;
            }

            // confere se a organização existe
            new OrganizacaoDAO().findByCodigo(codOrganizacao);

            UnidadeDAO dao = new UnidadeDAO();
            // o CNPJ não pode repetir
            if (cnpjJaCadastrado(dao, cnpj)) {
                exibirErro(request, response, "Já existe uma unidade com esse CNPJ.", ERROR_PAGE);
                return;
            }

            dao.insert(new Unidade(cnpj, email, codOrganizacao));

            // busca pelo CNPJ para pegar o código gerado e ir para o endereço
            Unidade nova = dao.findByCnpj(cnpj);
            // redirect
            response.sendRedirect(request.getContextPath() + "/endereco/perfil?codUnidade=" + nova.getCodigo());

        } catch (NotFoundException nfe) {
            nfe.printStackTrace();
            exibirErro(request, response, nfe, ERROR_PAGE);
        } catch (NumberFormatException nmfe) {
            exibirErro(request, response, "Organização inválida.", ERROR_PAGE);
        } catch (ConnectionFailedException cfe) {
            cfe.printStackTrace();
            exibirErro(request, response, cfe, ERROR_PAGE);
        } catch (Exception e) {
            e.printStackTrace();
            exibirErro(request, response, e, ERROR_PAGE);
        }
    }

    /**
     * Verifica se já existe uma unidade com o CNPJ informado.
     *
     * @param dao DAO de unidade
     * @param cnpj CNPJ digitado
     * @return true se o CNPJ já estiver cadastrado
     */
    private boolean cnpjJaCadastrado(UnidadeDAO dao, String cnpj) {
        try {
            dao.findByCnpj(cnpj);
            return true;
        } catch (NotFoundException nfe) {
            return false;
        }
    }
}
