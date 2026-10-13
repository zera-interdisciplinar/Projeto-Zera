package br.com.zera.servlet.gestor;

import br.com.zera.dao.GestorDAO;
import br.com.zera.exception.NotFoundException;
import br.com.zera.model.Gestor;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

import static br.com.zera.exception.ErroServlet.exibirErro;
import static br.com.zera.regex.Constants.ERROR_PAGE;

/**
 * Servlet responsável por excluir o cadastro do gestor logado.
 *
 * Esta classe recebe a requisição de exclusão via formulário HTTP, delega a
 * remoção ao {@link GestorDAO} e encerra a sessão do gestor excluído.
 *
 * @author Mayte B
 * @since 2026-10-06
 */
@WebServlet(name = "DeleteGestor", value = "/gestor/excluir")
public class DeleteGestorServlet extends HttpServlet {

    /**
     * Método responsável por processar a requisição de exclusão do gestor logado.
     * É acionado quando o usuário envia a requisição de remoção do sistema.
     *
     * @param request objeto que contém os dados enviados pelo formulário (JSP)
     * @param response objeto usado para enviar respostas ao cliente (redirecionamento ou erro)
     * @throws IOException caso ocorra erro de entrada/saída ao responder
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException{

        try{
            //resgata o gestor logado na sessão
            HttpSession session = request.getSession();
            Gestor gestorLogado = (Gestor) session.getAttribute("Gestor Logado");
            //checa se tal sessão existe
            if (gestorLogado == null) {
                exibirErro(request, response, "Sessão expirada. Faça login novamente.", ERROR_PAGE);
                return;
            }
            //instancia dao do gestor
            GestorDAO dao = new GestorDAO();
            //deleta o gestor pelo código do próprio gestor
            dao.delete(gestorLogado.getCodigo());
            // encerra a sessão, já que o gestor não existe mais
            session.invalidate();
            //envia página de resposta bem-sucedida da requisição de exclusão
            response.sendRedirect(request.getContextPath() + "/operacaoBemSucedida");
        }catch(NotFoundException nfe){
            exibirErro(request, response, nfe, ERROR_PAGE);
            nfe.printStackTrace();
        }catch(IOException ioe){
            exibirErro(request, response, ioe, ERROR_PAGE);
            ioe.printStackTrace();
        }catch(Exception e){
            exibirErro(request, response, e, ERROR_PAGE);
            e.printStackTrace();
        }
    }
}