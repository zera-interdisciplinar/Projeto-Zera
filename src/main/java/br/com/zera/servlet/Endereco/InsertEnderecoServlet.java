package br.com.zera.servlet.Endereco;

import br.com.zera.dao.UnidadeDAO;
import br.com.zera.exception.ConnectionFailedException;
import br.com.zera.exception.ErroServlet;
import br.com.zera.exception.NotFoundException;
import br.com.zera.model.Unidade;
import br.com.zera.regex.*;

import br.com.zera.dao.EnderecoDAO;
import br.com.zera.model.Endereco;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.ServletException;
import br.com.zera.regex.Constants;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import static br.com.zera.exception.ErroServlet.exibirErro;
import static br.com.zera.regex.Constants.ERROR_PAGE;

@WebServlet(name = "InsertEndereco", value = "/areaRestrita/cadastroEndereco")

 /**
 * Servlet responsável por processar o cadastro de endereços
 *
 * Esta classe recebe os dados enviados via formulário HTTP, realiza a
 * validação das informações e delega a persistência ao {@link EnderecoDAO}. *
 *
 * @author Mayte B
 * @since 2026-08-14
 */
public class InsertEnderecoServlet extends HttpServlet{

    /**
     * Processa o POST para inserir um novo Endereço
     *
     * @param request objeto HttpServletRequest contendo os parâmetros da página
     * @param response objeto HttpResponse para redirecionamento ou foward
     * @throws ServletException caso haja um erro ou exceção no Servlet
     * @throws java.io.IOException caso haja um erro de input/output (entrada/saída)
     * */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
       throws jakarta.servlet.ServletException, IOException {

        /**
         * Instancia DAO responsável por persistir objetos no banco de dados
         *
         * Instancia Chave estrangeira da tabela Unidade
         */
        EnderecoDAO dao = new EnderecoDAO();
        UnidadeDAO unidadeDAO = new UnidadeDAO();

//        /**
//         * Cria Arraylist<>() que recebe os parametros do model Empresa
//         */
//        Array
        try {

            /**
             * Capta os atributos (colunas do banco {@link EnderecoDAO}) do endereço recebido
             *
             * @throws  caso não tenha o código procurado
             */
            String codEndereco = request.getParameter("codigo");
            String codUnidade = request.getParameter("codUnidade");


            int codigoEndereco = Integer.parseInt(codEndereco);
            String bairro = request.getParameter("bairro");
            String num = request.getParameter("numero");
            int numero = Integer.parseInt(num);
            String cep = request.getParameter("cep");
            String logradouro = request.getParameter("logradouro");
            String cidade = request.getParameter("cidade");
            String estado = request.getParameter("estado");
            int codigoUnidade = Integer.parseInt(codUnidade);

            Unidade unidade = unidadeDAO.findByCodigo(codigoUnidade);

            /**
             * Cria objeto {@link Endereco}
             */
            Endereco model = new Endereco(codigoEndereco, bairro, numero, cep, logradouro, cidade, estado, codigoUnidade);

            if(Regex.validarCEP(cep) == false){
                exibirErro(request, response, "CEP inválido", ERROR_PAGE);
            }

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
