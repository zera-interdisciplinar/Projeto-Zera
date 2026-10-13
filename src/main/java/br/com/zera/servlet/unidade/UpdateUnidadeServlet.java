package br.com.zera.servlet.unidade;

import br.com.zera.dao.UnidadeDAO;
import br.com.zera.exception.ConnectionFailedException;
import br.com.zera.exception.NotFoundException;
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

/**
 * Servlet responsável por atualizar o cadastro de unidades
 *
 * Esta classe recebe os dados enviados via formulário HTTP, realiza a
 * validação das informações e delega as atualizações ao {@link UnidadeDAO}.
 *
 * @author Mayte B
 * @since 2026-10-06
 */
@WebServlet(name = "UpdateUnidade", value = "/unidade/alterarUnidade")
public class UpdateUnidadeServlet extends HttpServlet {

    /**
     * Processa POST para atualizar a unidade cadastrada baseada na antiga
     *
     * @param request objeto HttpServletRequest contendo os parâmetros da página
     * @param response objeto HttpResponse para redirecionamento ou foward
     * @throws java.io.IOException caso haja um erro de input/output (entrada/saída)
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        // DAO responsável por persistir a unidade no banco de dados
        UnidadeDAO dao = new UnidadeDAO();

        try {

            // resgata a unidade logada na sessão
            HttpSession session = request.getSession();
            Unidade unidadeLogada = (Unidade) session.getAttribute("Unidade Logada");
            if (unidadeLogada == null) {
                exibirErro(request, response, "Sessão expirada. Faça login novamente.", ERROR_PAGE);
                return;
            }

            // busca a unidade atual
            Unidade unidadeExistente = dao.findByCodigo(unidadeLogada.getCodigo());

            int codigoUnidade = unidadeExistente.getCodigo();
            // a organização vem do banco, e não do formulário, para o gestor não trocá-la
            int codOrganizacao = unidadeExistente.getCodOrganizacao();
            String cnpj = request.getParameter("cnpj");
            String email = request.getParameter("email");

            // exibe página de erro caso o cnpj inserido seja inválido
            if (Regex.validarCNPJ(cnpj) == false) {
                exibirErro(request, response, "cnpj inválido", ERROR_PAGE);
                return;
            }

            // exibe página de erro caso o email inserido seja inválido
            if (Regex.validarEmail(email) == false) {
                exibirErro(request, response, "email inválido", ERROR_PAGE);
                return;
            }

            // cria objeto Unidade com os dados atualizados
            Unidade model = new Unidade(codigoUnidade, cnpj, email, codOrganizacao);

            // atualiza a unidade no banco
            dao.update(model);

            // Redireciona para o perfil da unidade após a atualização bem-sucedida
            response.sendRedirect(request.getContextPath() + "/unidade/perfil");

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