package br.com.zera.servlet.endereco;

import br.com.zera.dao.EnderecoDAO;
import br.com.zera.exception.NotFoundException;
import br.com.zera.model.Endereco;
import br.com.zera.model.Unidade;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

import static br.com.zera.exception.ErroServlet.exibirErro;
import static br.com.zera.regex.Constants.ERROR_PAGE;
import static br.com.zera.regex.Constants.SESSAO_UNIDADE;

/**
 * Servlet responsável por excluir o endereço da unidade logada.
 *
 * Esta classe recebe a requisição de exclusão via formulário HTTP, localiza o
 * endereço vinculado à unidade da sessão e delega a remoção ao {@link EnderecoDAO}.
 *
 * @author Mayte B
 * @since 2026-9-28
 */
@WebServlet(name = "DeleteEndereco", value = "/endereco/excluir")
public class DeleteEnderecoServlet extends HttpServlet {

    /**
     * Método responsável por processar a requisição de exclusão do endereço da unidade.
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
            if (unidadeLogada == null) {
                exibirErro(request, response, "Sessão expirada. Faça login novamente.", ERROR_PAGE);
                return;
            }

            //busca o endereço pela unidade
            Endereco endereco = EnderecoDAO.findByCodUnidade(unidadeLogada.getCodigo());
            //deleta o endereço pelo código do próprio endereço
            EnderecoDAO.delete(endereco.getCodigo());
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
