package br.com.zera.servlet.Organizacao;
import br.com.zera.dao.EnderecoDAO;
import br.com.zera.dao.OrganizacaoDAO;
import br.com.zera.dao.UnidadeDAO;
import br.com.zera.exception.ConnectionFailedException;
import br.com.zera.exception.NotFoundException;
import br.com.zera.model.Endereco;
import br.com.zera.model.Gestor;
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

@WebServlet(name = "UpdateEndereco", value = "/endereco/alterarEndereco")

/**
 * Servlet responsável por atualizar o cadastro de organizações
 *
 * Esta classe recebe os dados enviados via formulário HTTP, realiza a
 * validação das informações e delega as atualizações ao {@link OrganizacaoDAO}. *
 *
 * @author Mayte B
 * @since 2026-10-06
 */
public class UpdateOrganizacaoServlet extends HttpServlet {

    /**
     * Processa POST para atualizar a Organização cadastrada baseado no antigo
     *
     * @param request objeto HttpServletRequest contendo os parâmetros da página
     * @param response objeto HttpResponse para redirecionamento ou foward
     * @throws java.io.IOException caso haja um erro de input/output (entrada/saída)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        /**
         * Instancia DAO responsável por persistir objetos no banco de dados
         *
         * Instancia Chave estrangeira da tabela Unidade
         */
        OrganizacaoDAO dao = new OrganizacaoDAO();
        UnidadeDAO unidadeDAO = new UnidadeDAO();

        try {

            /**
             * Salva o login/sign in da {@link Unidade} para manter a sessão ativa
             */
            HttpSession session = request.getSession();
            Unidade unidadeLogada = (Unidade) session.getAttribute("UnidadeLogada");
            if (unidadeLogada == null) {
                exibirErro(request, response, "Sessão expirada. Faça login novamente.", ERROR_PAGE);
                return;
            }
            /**
             * Capta os atributos (colunas do banco {@link OrganizacaoDAO}) do organização recebido
             *
             * @throws caso não tenha o código procurado
             */
            int codUnidade = unidadeLogada.getCodigo();
            Organizacao organizacaoExistente = dao.findByCodigo(codUnidade);

            if (organizacaoExistente == null) {
                exibirErro(request, response, "Endereço não encontrado.", ERROR_PAGE);
                return;
            }

            int codigoOrganizacao = organizacaoExistente.getCodigo();
            String cnpj = request.getParameter("cnpj");
            String nome = request.getParameter("nome");
            String email = request.getParameter("email");

            /**
             * Instancia página de erro caso o CNPJ inserido seja inválido
             */
            if (Regex.validarCEP(cnpj) == false) {
                exibirErro(request, response, "CNPJ inválido", ERROR_PAGE);
                return;
            }

            if (Regex.validarCEP(email) == false) {
                exibirErro(request, response, "email inválido", ERROR_PAGE);
                return;
            }
            /**
             * Cria objeto {@link Organizacao}
             */
            Organizacao model = new Organizacao(codigoOrganizacao, cnpj, nome, email);

            /**
             * Instancia as informações recebidas no {@link Organizacao} com o método
             */
            dao.update(model);

            // Redireciona para a lista de endereços após a inserção bem-sucedida
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