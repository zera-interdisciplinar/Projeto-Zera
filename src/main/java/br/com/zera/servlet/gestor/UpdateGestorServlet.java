package br.com.zera.servlet.Gestor;

import br.com.zera.dao.EnderecoDAO;
import br.com.zera.dao.GestorDAO;
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

@WebServlet(name = "UpdateGestor", value = "/gestor/alterarGestor")

/**
 * Servlet responsável por atualizar o cadastro de gestor
 *
 * Esta classe recebe os dados enviados via formulário HTTP, realiza a
 * validação das informações e delega as atualizações ao {@link GestorDAO}. *
 *
 * @author Mayte B
 * @since 2026-10-06
 */
public class UpdateGestorServlet extends HttpServlet {

    /**
     * Processa POST para atualizar a Gestor cadastrada baseado no antigo
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
        GestorDAO dao = new GestorDAO();
        UnidadeDAO unidadeDAO = new UnidadeDAO();

        try {

            /**
             * Salva o login/sign in da {@link Gestor} para manter a sessão ativa
             */
            HttpSession session = request.getSession();
            Unidade unidadeLogada = (Unidade) session.getAttribute("UnidadeLogada");
            if (unidadeLogada == null) {
                exibirErro(request, response, "Sessão expirada. Faça login novamente.", ERROR_PAGE);
                return;
            }
            /**
             * Capta os atributos (colunas do banco {@link GestorDAO}) do organização recebido
             *
             * @throws exibirErro() caso não tenha o código procurado
             */
            int codUnidade = unidadeLogada.getCodigo();
            Gestor gestorExistente = dao.findByCodigo(codUnidade);

            if (gestorExistente == null) {
                exibirErro(request, response, "Gestor não encontrado.", ERROR_PAGE);
                return;
            }

            int codigoGestor = gestorExistente.getCodigo();
            String nome = request.getParameter("nome");
            String email = request.getParameter("email");
            String senha = request.getParameter("senha");
            String telefone = request.getParameter("telefone");

            /**
             * Instancia página de erro caso o CNPJ inserido seja inválido
             */
            if (Regex.validarEmail(email) == false) {
                exibirErro(request, response, "email inválido", ERROR_PAGE);
                return;
            }

            if (Regex.validarTel(telefone) == false) {
                exibirErro(request, response, "telefone inválido", ERROR_PAGE);
                return;
            }

            /**
             * Cria objeto {@link Gestor}
             */
            Gestor model = new Gestor(codigoGestor, nome, email, senha, telefone, codUnidade);

            /**
             * Instancia as informações recebidas no {@link Gestor} com o método
             */
            dao.update(model);

            // Redireciona para a lista de endereços após a inserção bem-sucedida
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