package br.com.zera.servlet.Endereco;

import br.com.zera.dao.EnderecoDAO;
import br.com.zera.model.Gestor;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.SQLException;

import static br.com.zera.exception.ErroServlet.exibirErro;
import static br.com.zera.regex.Constants.ERROR_PAGE;

@WebServlet(name = "DeleteEndereco", value = "/logOut")


/**
 * Servlet responsável por mostrar as informações de endereço cadastradas
 *
 * Esta classe recebe a requisição de visualização do endereço cadastrado pelo gestor via formulário HTTP,
 * e as exibe com uma tela de resposta
 *
 * @author Mayte B
 * @since 2026-9-28
 */
public class DeleteEnderecoServlet extends HttpServlet {

    /**
     * Método responsável por processar a requisição de exclusão do endereço uma empresa.
     * É acionado quando o usuário envia a requisição de remoção do sistema.
     *
     * @param request objeto que contém os dados enviados pelo formulário (JSP)
     * @param response objeto usado para enviar respostas ao cliente (redirecionamento ou erro)
     * @throws IOException caso
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws IOException{
        try{
            //resgata sessão logada
            HttpSession session = request.getSession();
            Gestor gestorLogado = (Gestor) session.getAttribute("gestorLogado");
            //checa se tal sessão existe
            if (gestorLogado == null) {
                exibirErro(request, response, "Sessão expirada. Faça login novamente.", ERROR_PAGE);
                return;
            }
            //instancia dao de endereço
            EnderecoDAO dao = new EnderecoDAO();
            //captura o código do dal instanciado
            int codigo = gestorLogado.getCodigo();
            //caham função de deletar o endereço baseado no código da sessão
            dao.delete(codigo);
            //envia página de resposta bem-sucedida da requisição de exclusão
            response.sendRedirect(request.getContextPath() + "/operacaoBemSucedida");
        }catch(IOException ioe){
            exibirErro(request, response, ioe, ERROR_PAGE);
            ioe.printStackTrace();
        }catch(Exception e){
            exibirErro(request, response, e, ERROR_PAGE);
            e.printStackTrace();
        }
    }
}
