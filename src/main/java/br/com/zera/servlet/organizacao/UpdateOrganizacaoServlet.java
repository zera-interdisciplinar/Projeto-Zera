package br.com.zera.servlet.organizacao;
import br.com.zera.dao.OrganizacaoDAO;
import br.com.zera.exception.ConnectionFailedException;
import br.com.zera.exception.NotFoundException;
import br.com.zera.model.Organizacao;
import br.com.zera.model.Unidade;
import br.com.zera.regex.Regex;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import static br.com.zera.exception.ErroServlet.exibirErro;
import static br.com.zera.regex.Constants.ERROR_PAGE;
import static br.com.zera.regex.Constants.SESSAO_UNIDADE;

/**
 * Servlet responsável por atualizar o cadastro de organizações
 *
 * Esta classe recebe os dados enviados via formulário HTTP, realiza a
 * validação das informações e delega as atualizações ao {@link OrganizacaoDAO}.
 *
 * @author Mayte B
 * @since 2026-10-06
 */
@WebServlet(name = "UpdateOrganizacao", value = "/organizacao/alterarOrganizacao")
public class UpdateOrganizacaoServlet extends HttpServlet {

    /**
     * Processa POST para atualizar a Organização cadastrada baseado no antigo
     *
     * @param request objeto HttpServletRequest contendo os parâmetros da página
     * @param response objeto HttpResponse para redirecionamento ou foward
     * @throws java.io.IOException caso haja um erro de input/output (entrada/saída)
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        // DAO responsável por persistir a organização no banco de dados
        OrganizacaoDAO dao = new OrganizacaoDAO();

        try {

            // resgata a unidade logada na sessão
            HttpSession session = request.getSession();
            Unidade unidadeLogada = (Unidade) session.getAttribute(SESSAO_UNIDADE);
            if (unidadeLogada == null) {
                exibirErro(request, response, "Sessão expirada. Faça login novamente.", ERROR_PAGE);
                return;
            }

            // a organização é a chave estrangeira da unidade
            Organizacao organizacaoExistente = dao.findByCodigo(unidadeLogada.getCodOrganizacao());

            int codigoOrganizacao = organizacaoExistente.getCodigo();
            String cnpj = request.getParameter("cnpj");
            String nome = request.getParameter("nome");
            String email = request.getParameter("email");

            // exibe página de erro caso o CNPJ inserido seja inválido
            if (Regex.validarCNPJ(cnpj) == false) {
                exibirErro(request, response, "CNPJ inválido", ERROR_PAGE);
                return;
            }

            // exibe página de erro caso o email inserido seja inválido
            if (Regex.validarEmail(email) == false) {
                exibirErro(request, response, "email inválido", ERROR_PAGE);
                return;
            }

            // cria objeto Organizacao com os dados atualizados
            Organizacao model = new Organizacao(codigoOrganizacao, cnpj, nome, email);

            // atualiza a organização no banco
            dao.update(model);

            // Redireciona para o perfil da organização após a atualização bem-sucedida
            response.sendRedirect(request.getContextPath() + "/organizacao/perfil");

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