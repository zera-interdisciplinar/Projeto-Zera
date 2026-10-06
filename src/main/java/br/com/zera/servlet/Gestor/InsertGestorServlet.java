package br.com.zera.servlet.Gestor;

import br.com.zera.dao.GestorDAO;
import br.com.zera.dao.UnidadeDAO;
import br.com.zera.exception.ConnectionFailedException;
import br.com.zera.exception.NotFoundException;
import br.com.zera.model.*;
import br.com.zera.regex.*;

import br.com.zera.dao.EnderecoDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

import static br.com.zera.exception.ErroServlet.exibirErro;
import static br.com.zera.regex.Constants.ERROR_PAGE;

    @WebServlet(name = "InsertGestor", value = "/gestor/signIn")

/**
 * Servlet responsável por processar o cadastro de gestores
 *
 * Esta classe recebe os dados enviados via formulário HTTP, realiza a
 * validação das informações e delega a persistência ao {@link GestorDAO}. *
 *
 * @author Mayte B
 * @since 2026-10-06
 */
public class InsertGestorServlet extends HttpServlet {

    /**
     * Processa POST para inserir um novo Gestor para a unidade
     *
     * @param request objeto HttpServletRequest contendo os parâmetros da página
     * @param response objeto HttpResponse para redirecionamento ou foward
     * @throws java.io.IOException caso haja um erro de input/output (entrada/saída)
     *
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        /**
         * Instancia DAO responsável por persistir objetos no banco de dados
         *
         * Instancia Chave estrangeira da tabela Unidade
         */
        GestorDAO dao = new GestorDAO();
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
             * Capta os atributos (colunas do banco {@link GestorDAO}) do endereço recebido
             */
            int codigoGestor = unidadeLogada.getCodigo();
            String nome = request.getParameter("nome");
            String email = request.getParameter("email");
            String senha = request.getParameter("senha");
            String telefone = request.getParameter("telefone");

            int codUnidade = unidadeLogada.getCodigo();
            if (unidadeDAO.findByCodigo(codUnidade) == null) {
                exibirErro(request, response, "Unidade vinculada ao gestor não foi encontrada.", ERROR_PAGE);
                return;
            }

            /**
             * Instancia página de erro caso o CPF inserido seja inválido
             */
            if (Regex.validarEmail(email) == false) {
                exibirErro(request, response, "Email inválido", ERROR_PAGE);
                return;
            }

            /**
             * Cria objeto {@link Gestor}
             */
            Gestor model = new Gestor();

            /**
             * Instancia as informações recebidas no {@link Gestor} com o método
             */
            dao.insert(model);

            // Redireciona para o perfil após a inserção bem-sucedida
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
