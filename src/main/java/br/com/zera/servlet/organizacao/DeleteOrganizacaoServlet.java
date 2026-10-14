package br.com.zera.servlet.organizacao;

import br.com.zera.dao.OrganizacaoDAO;
import br.com.zera.exception.NotFoundException;
import br.com.zera.model.Unidade;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

import static br.com.zera.exception.ErroServlet.exibirErro;
import static br.com.zera.regex.Constants.ERROR_PAGE;
import static br.com.zera.regex.Constants.SESSAO_UNIDADE;

/**
 * Servlet responsável por excluir a organização da unidade logada.
 *
 * Esta classe recebe a requisição de exclusão via formulário HTTP, delega a
 * remoção ao {@link OrganizacaoDAO} e encerra a sessão em seguida.
 *
 * @author Mayte B
 * @since 2026-10-06
 */
@WebServlet(name = "DeleteOrganizacao", value = "/organizacao/excluir")
public class DeleteOrganizacaoServlet extends HttpServlet {

    /**
     * Método responsável por processar a requisição de exclusão da organização.
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
            //resgata a unidade logada na sessão
            HttpSession session = request.getSession();
            Unidade unidadeLogada = (Unidade) session.getAttribute(SESSAO_UNIDADE);
            //checa se tal sessão existe
            if (unidadeLogada == null) {
                exibirErro(request, response, "Sessão expirada. Faça login novamente.", ERROR_PAGE);
                return;
            }
            //instancia dao de organização
            OrganizacaoDAO dao = new OrganizacaoDAO();
            //a organização é a chave estrangeira da unidade logada
            int codigo = unidadeLogada.getCodOrganizacao();
            //deleta a organização pelo código da própria organização
            dao.delete(codigo);
            // encerra a sessão, já que a organização da unidade não existe mais
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