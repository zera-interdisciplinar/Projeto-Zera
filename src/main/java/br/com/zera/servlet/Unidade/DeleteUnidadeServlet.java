package br.com.zera.servlet.Unidade;

import br.com.zera.dao.UnidadeDAO;
import br.com.zera.model.Unidade;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

import static br.com.zera.exception.ErroServlet.exibirErro;
import static br.com.zera.regex.Constants.ERROR_PAGE;

@WebServlet(name = "DeleteUnidade", value = "/logOut")


/**
 * Servlet responsável por mostrar as informações de endereço cadastradas
 *
 * Esta classe recebe a requisição de visualização do endereço cadastrado pelo gestor via formulário HTTP,
 * e as exibe com uma tela de resposta
 *
 * @author Mayte B
 * @since 2026-10-05
 */
public class DeleteUnidadeServlet extends HttpServlet {

    /**
     * Método responsável por processar a requisição de exclusão da unidade de uma empresa.
     * É acionado quando o usuário envia a requisição de remoção do sistema.
     *
     * @param request objeto que contém os dados enviados pelo formulário (JSP)
     * @param response objeto usado para enviar respostas ao cliente (redirecionamento ou erro)
     * @throws IOException caso a entrada do usuário esteja incorreta
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException{
        try{
            //resgata sessão logada
            HttpSession session = request.getSession();
            Unidade unidadeLogada = (Unidade) session.getAttribute("unidadeLogada");
            //checa se tal sessão existe
            if (unidadeLogada == null) {
                exibirErro(request, response, "Sessão expirada. Faça login novamente.", ERROR_PAGE);
                return;
            }
            //instancia dao de unidade
            UnidadeDAO dao = new UnidadeDAO();
            //captura o código do dal instanciado
            int codigo = unidadeLogada.getCodigo();
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