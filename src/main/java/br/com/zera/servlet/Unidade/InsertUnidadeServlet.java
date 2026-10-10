package br.com.zera.servlet.Unidade;

import br.com.zera.dao.UnidadeDAO;
import br.com.zera.exception.ConnectionFailedException;
import br.com.zera.exception.NotFoundException;
import br.com.zera.model.*;
import br.com.zera.regex.*;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

import static br.com.zera.exception.ErroServlet.exibirErro;
import static br.com.zera.regex.Constants.ERROR_PAGE;

@WebServlet(name = "InsertUnidade", value = "/unidade/signIn")

/**
 * Servlet responsável por processar o cadastro de unidades
 *
 * Esta classe recebe os dados enviados via formulário HTTP, realiza a
 * validação das informações e delega a persistência ao {@link UnidadeDAO}. *
 *
 * @author Mayte B
 * @since 2026-10-02
 */
public class InsertUnidadeServlet extends HttpServlet{

    /**
     * Processa POST para inserir uma nova Unidade
     *
     * @param request objeto HttpServletRequest contendo os parâmetros da página
     * @param response objeto HttpResponse para redirecionamento ou foward
     * @throws java.io.IOException caso haja um erro de input/output (entrada/saída)
     * */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

         //Instancia DAO responsável por persistir objetos no banco de dados
        UnidadeDAO dao = new UnidadeDAO();
        Organizacao organizacao = new Organizacao();

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
             * Capta os atributos (colunas do banco {@link UnidadeDAO}) do endereço recebido
             */
            String cnpj = request.getParameter("cnpj");
            String email = request.getParameter("email");

            int codOrganizacao = organizacao.getCodigo();
            if(dao.findByCodigo(codOrganizacao) == null) {
                exibirErro(request, response, "Organizacao vinculada a unidade não foi encontrada.", ERROR_PAGE);
                return;
            }

            /**
             * Instancia página de erro caso o CNPJ inserido seja inválido
             */
            if(Regex.validarCEP(cnpj) == false){
                exibirErro(request, response, "CNPJ inválido", ERROR_PAGE);
                return;
            }

            /**
             * Instancia página de erro caso o email inserido seja inválido
             */
            if(Regex.validarCEP(email) == false){
                exibirErro(request, response, "email inválido", ERROR_PAGE);
                return;
            }

            /**
             * Cria objeto {@link Unidade}
             */
            Unidade model = new Unidade();

            /**
             * Instancia as informações recebidas no {@link Endereco} com o método
             */
            dao.insert(model);

            // Redireciona para o perfil após a inserção bem-sucedida
            response.sendRedirect(request.getContextPath() + "/unidade/perfil");

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