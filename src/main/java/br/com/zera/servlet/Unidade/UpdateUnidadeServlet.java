package br.com.zera.servlet.Unidade;

import br.com.zera.dao.EnderecoDAO;
import br.com.zera.dao.GestorDAO;
import br.com.zera.dao.UnidadeDAO;
import br.com.zera.exception.ConnectionFailedException;
import br.com.zera.exception.NotFoundException;
import br.com.zera.model.Endereco;
import br.com.zera.model.Gestor;
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

@WebServlet(name = "UpdateUnidade", value = "/unidade/alterarUnidade")

/**
 * Servlet responsável por atualizar o cadastro de unidades
 *
 * Esta classe recebe os dados enviados via formulário HTTP, realiza a
 * validação das informações e delega as atualizações ao {@link UnidadeDAO}. *
 *
 * @author Mayte B
 * @since 2026-10-06
 */
public class UpdateUnidadeServlet extends HttpServlet {

    /**
     * Processa POST para atualizar a unidade cadastrada baseada na antiga
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
        UnidadeDAO dao = new UnidadeDAO();
        Unidade unidade = new Unidade();

        try {

            /**
             * Salva o login/sign in do {@link Unidade} para manter a sessão tiva
             */
            HttpSession session = request.getSession();
            Unidade unidadeLogada = (Unidade) session.getAttribute("UnidadeLogada");
            if (unidadeLogada == null) {
                exibirErro(request, response, "Sessão expirada. Faça login novamente.", ERROR_PAGE);
                return;
            }
            /**
             * Capta os atributos (colunas do banco {@link UnidadeDAO}) do unidade recebida
             *
             * @throws caso não tenha o código procurado
             */
            int codUnidade = unidadeLogada.getCodigo();
            Unidade unidadeExistente = dao.findByCodigo(codUnidade);

            if (unidadeExistente == null) {
                exibirErro(request, response, "Unidade não encontrada.", ERROR_PAGE);
                return;
            }

            int codigoUnidade = unidadeExistente.getCodigo();
            String cnpj = request.getParameter("cnpj");
            String email = request.getParameter("email");
            String codigoOrganizacao = request.getParameter("codOrganizacao");
            int codOrganizacao = Integer.parseInt(codigoOrganizacao);

            /**
             * Instancia página de erro caso o cnpj inserido seja inválido
             */
            if (Regex.validarCNPJ(cnpj) == false) {
                exibirErro(request, response, "cnpj inválido", ERROR_PAGE);
                return;
            }

            /**
             * Cria objeto {@link Unidade}
             */
            Unidade model = new Unidade(codigoUnidade, cnpj, email, codOrganizacao);

            /**
             * Instancia as informações recebidas no {@link Endereco} com o método
             */
            dao.update(model);

            // Redireciona para a lista de endereços após a inserção bem-sucedida
            response.sendRedirect(request.getContextPath() + "/endereco/perfil");

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
