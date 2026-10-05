package br.com.zera.servlet.Organizacao;

import br.com.zera.dao.OrganizacaoDAO;
import br.com.zera.dao.UnidadeDAO;
import br.com.zera.exception.ConnectionFailedException;
import br.com.zera.exception.NotFoundException;
import br.com.zera.model.*;
import br.com.zera.regex.*;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.time.LocalDate;

import static br.com.zera.exception.ErroServlet.exibirErro;
import static br.com.zera.regex.Constants.ERROR_PAGE;

@WebServlet(name = "InsertEndereco", value = "/endereco/signIn")

/**
 * Servlet responsável por processar o cadastro da organização
 *
 * Esta classe recebe os dados enviados via formulário HTTP, realiza a
 * validação das informações e delega a persistência a {@link OrganizacaoDAO}.
 *
 * @author Mayte B
 * @since 2026-10-05
 */
public class InsertOrganizacaoServlet extends HttpServlet{

    /**
     * Processa POST para inserir uma nova Organização
     *
     * @param request objeto HttpServletRequest contendo os parâmetros da página
     * @param response objeto HttpResponse para redirecionamento ou foward
     * @throws java.io.IOException caso haja um erro de input/output (entrada/saída)
     * */
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
             * Salva o login/signin da {@link Unidade} para manter a sessão ativa
             */
            HttpSession session = request.getSession();
            Unidade unidadeLogada = (Unidade) session.getAttribute("UnidadeLogada");
            if (unidadeLogada == null) {
                exibirErro(request, response, "Sessão expirada. Faça login novamente.", ERROR_PAGE);
                return;
            }

            /**
             * Capta os atributos (colunas do banco {@link OrganizacaoDAO}) da organização recebida
             */
            int codigoOrganizacao = unidadeLogada.getCodigo();
            String cnpj = request.getParameter("cnpj");
            String nome = request.getParameter("nome");
            String email = request.getParameter("email");
            String codigoCadastro = request.getParameter("codCadastro");
            if(!codigoCadastro.equals(null)){
                exibirErro(request, response, "codigo de cadastro não pode ser null", ERROR_PAGE);
            }
            int codCadastro = Integer.parseInt(codigoCadastro);

            int codUnidade = unidadeLogada.getCodigo();
            if(unidadeDAO.findByCodigo(codUnidade) == null) {
                exibirErro(request, response, "Organização vinculada ao gestor não foi encontrada.", ERROR_PAGE);
                return;
            }

            /**
             * Instancia página de erro caso o CPF inserido seja inválido
             */
            if(Regex.validarCEP(cnpj) == false){
                exibirErro(request, response, "CNPJ inválido", ERROR_PAGE);
                return;
            }

            /**
             * Cria objeto {@link Organizacao}
             */
            Organizacao model = new Organizacao(codigoOrganizacao, cnpj, nome, email, codCadastro);

            /**
             * Instancia as informações recebidas no {@link Organizacao} com o método
             */
            dao.insert(model);

            // Redireciona para o perfil após a inserção bem-sucedida
            response.sendRedirect(request.getContextPath() + "/endereco/perfil");

        } catch(ConnectionFailedException cfe){
            cfe.printStackTrace();
            exibirErro(request, response, cfe, ERROR_PAGE);
        }catch(NotFoundException nfe){
            nfe.printStackTrace();
            exibirErro(request, response, nfe, ERROR_PAGE);
        }catch(NumberFormatException nmfe){
            nmfe.printStackTrace();
            exibirErro(request, response, nmfe, ERROR_PAGE);
        } catch (Exception e) {
            exibirErro(request, response, e, ERROR_PAGE);
        }
    }
}