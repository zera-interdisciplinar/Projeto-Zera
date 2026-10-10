package br.com.zera.servlet.assinatura;

import br.com.zera.dao.AssinaturaDAO;
import br.com.zera.model.Assinatura;
import br.com.zera.model.Unidade;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

import static br.com.zera.exception.ErroServlet.exibirErro;
import static br.com.zera.regex.Constants.ERROR_PAGE;

/**
 * Servlet responsável por excluir a assinatura de uma organização.
 * <p>
 * Recebe, via POST, o código da assinatura, confere se ela pertence à
 * organização da unidade logada e solicita a exclusão ao {@link AssinaturaDAO}.
 *
 * @author Pedro Rufino
 * @since 2026-10-06
 */
@WebServlet(name = "DeleteAssinatura", value = "/assinatura/deletarAssinatura")
public class DeleteAssinaturaServlet extends HttpServlet {

    /**
     * Processa a requisição de exclusão de uma assinatura.
     *
     * @param request  requisição com o parâmetro "codigo" da assinatura a excluir
     * @param response resposta usada para redirecionar ou exibir a página de erro
     * @throws IOException se ocorrer falha ao redirecionar ou escrever a resposta
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException{
        try{
            // Recupera o gestor guardado na sessão durante o login
            HttpSession session = request.getSession();
            Unidade unidadeLogada = (Unidade) session.getAttribute("unidadeLogada");
            if (unidadeLogada == null) {
                exibirErro(request, response, "Sessão expirada. Faça login novamente.", ERROR_PAGE);
                return;
            }

            // Valida o código antes de ir ao banco
            String cod = request.getParameter("codigo");
            if (cod == null || cod.trim().isEmpty()) {
                exibirErro(request, response, "Código da assinatura não informado.", ERROR_PAGE);
                return;
            }

            int codigo;
            codigo = Integer.parseInt(cod.trim());

            AssinaturaDAO dao = new AssinaturaDAO();
            Assinatura assinatura = dao.findByCodigo(codigo);

            // Necessário caso o findById devolva null em vez de lançar NotFoundException
            if (assinatura == null) {
                exibirErro(request, response, "Assinatura não encontrada.", ERROR_PAGE);
                return;
            }

            // A assinatura pertence à organização; a unidade só pode excluir a da sua organização
            if (assinatura.getCodOrganizacao() != unidadeLogada.getCodOrganizacao()) {
                exibirErro(request, response, "Você não tem permissão para excluir esta assinatura.", ERROR_PAGE);
                return;
            }

            dao.delete(codigo);

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
