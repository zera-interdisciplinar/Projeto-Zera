package br.com.zera.servlet.estoque;

import br.com.zera.dao.EstoqueDAO;
import br.com.zera.model.Estoque;
import br.com.zera.model.Gestor;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

import static br.com.zera.exception.ErroServlet.exibirErro;
import static br.com.zera.regex.Constants.ERROR_PAGE;

@WebServlet(name = "DeleteEstoque", value = "/estoque/deletarEstoque")


/**
 * Servlet responsável por apagar as informações de estoques cadastradas
 *
 * Esta classe recebe a requisição de visualização do estoque cadastrado pelo gestor via formulário HTTP,
 * e as exibe com uma tela de resposta
 *
 * @author Pedro Rufino
 * @since 2026-10-04
 */
public class DeleteEstoqueServlet extends HttpServlet {

    /**
     * Método responsável por processar a requisição de exclusão do estoque de uma empresa.
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
            // Recupera o gestor guardado na sessão durante o login
            HttpSession session = request.getSession();
            Gestor gestorLogado = (Gestor) session.getAttribute("gestorLogado");
            if (gestorLogado == null) {
                exibirErro(request, response, "Sessão expirada. Faça login novamente.", ERROR_PAGE);
                return;
            }

            // O código identifica qual estoque excluir
            String cod = request.getParameter("codigo");
            if (cod == null || cod.trim().isEmpty()) {
                exibirErro(request, response, "Código do estoque não informado.", ERROR_PAGE);
                return;
            }
            int codigo = Integer.parseInt(cod.trim());

            EstoqueDAO dao = new EstoqueDAO();

            // Busca antes de apagar
            Estoque estoque = dao.findByCodigo(codigo);

            // Impede que o gestor apague estoque de outra unidade trocando o código no HTML
            if (estoque.getCodUnidade() != gestorLogado.getCodUnidade()) {
                exibirErro(request, response, "Você não tem permissão para excluir este estoque.", ERROR_PAGE);
                return;
            }

            // Instancia as informações recebidas no Estoque com o método
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
