package br.com.zera.servlet.Endereco;

import br.com.zera.dao.EnderecoDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.ServletException;

import java.io.IOException;

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
     * @param request objeto HttpServletRequest contendo os parâmetros do formulário
     * @param response objeto HttpResponse para redirecionamento ou foward
     * @throws ServletException caso haja um erro ou exceção no Servlet
     * @throws java.io.IOException caso haja um erro de input/output
     * */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
       throws jakarta.servlet.ServletException, IOException {

        //instancia DAO responsável por persistir objetos no banco de dados
        EnderecoDao dao = new EnderecoDao();


    }
}
