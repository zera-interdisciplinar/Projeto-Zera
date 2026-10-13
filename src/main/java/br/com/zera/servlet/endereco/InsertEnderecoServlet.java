package br.com.zera.servlet.endereco;

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
import static br.com.zera.regex.Constants.SESSAO_UNIDADE;

/**
 * Servlet responsável por processar o cadastro de endereços
 *
 * Esta classe recebe os dados enviados via formulário HTTP, realiza a
 * validação das informações e delega a persistência ao {@link EnderecoDAO}.
 *
 * @author Mayte B
 * @since 2026-08-14
 */
@WebServlet(name = "InsertEndereco", value = "/endereco/signIn")
public class InsertEnderecoServlet extends HttpServlet{

    /**
     * Processa POST para inserir um novo Endereço
     *
     * @param request objeto HttpServletRequest contendo os parâmetros da página
     * @param response objeto HttpResponse para redirecionamento ou foward
     * @throws java.io.IOException caso haja um erro de input/output (entrada/saída)
     * */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        try {

            // resgata a unidade logada na sessão
            HttpSession session = request.getSession();
            Unidade unidadeLogada = (Unidade) session.getAttribute(SESSAO_UNIDADE);
            if (unidadeLogada == null) {
                exibirErro(request, response, "Sessão expirada. Faça login novamente.", ERROR_PAGE);
                return;
            }

            // capta os atributos (colunas do banco) do endereço recebido
            String bairro = request.getParameter("bairro");
            String num = request.getParameter("numero");
            if (num == null || num.isEmpty()) {
                exibirErro(request, response, "Número não informado.", ERROR_PAGE);
                return;
            }
            int numero = Integer.parseInt(num);
            String cep = request.getParameter("cep");
            String logradouro = request.getParameter("logradouro");
            String cidade = request.getParameter("cidade");
            String estado = request.getParameter("estado");

            // o endereço pertence à unidade logada (chave estrangeira)
            int codUnidade = unidadeLogada.getCodigo();

            // exibe página de erro caso o CEP inserido seja inválido
            if(Regex.validarCEP(cep) == false){
                exibirErro(request, response, "CEP inválido", ERROR_PAGE);
                return;
            }

            // cria objeto model
            Endereco model = new Endereco(bairro, numero, cep, logradouro, cidade, estado, codUnidade);

            // instancia o DAO de endereço (os métodos não são mais static)
            EnderecoDAO dao = new EnderecoDAO();

            //Instancia as informações recebidas no Endereco com o método
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