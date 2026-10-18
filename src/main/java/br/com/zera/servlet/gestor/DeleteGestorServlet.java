package br.com.zera.servlet.gestor;

import br.com.zera.exception.ConnectionFailedException;
import br.com.zera.exception.NotFoundException;
import br.com.zera.util.ExclusaoCascata;
import br.com.zera.util.Sessao;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static br.com.zera.exception.ErroServlet.exibirErro;
import static br.com.zera.regex.Constants.ERROR_PAGE;

/**
 * Servlet responsável por excluir um gestor.
 *
 * Apenas o admin pode excluir: ele escolhe o gestor na lista e confirma
 * no modal "Deseja mesmo excluir?". O código chega por parâmetro.
 *
 * A exclusão é em cascata: os estoques em que o gestor é responsável
 * também são apagados (veja {@link ExclusaoCascata}).
 *
 * @author Mayte B
 * @since 2026-10-06
 */
@WebServlet(name = "DeleteGestor", value = "/gestor/excluir")
public class DeleteGestorServlet extends HttpServlet {

    /**
     * Processa POST para excluir o gestor informado.
     *
     * @param request objeto HttpServletRequest com o parâmetro codigo
     * @param response objeto HttpServletResponse para redirecionamento ou erro
     * @throws IOException caso ocorra erro de entrada/saída ao responder
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        try {
            // só o admin pode fazer isso
            if (Sessao.admin(request) == null) {
                exibirErro(request, response, "Acesso permitido apenas ao administrador.", ERROR_PAGE);
                return;
            }

            // lê o código
            String codigoTexto = request.getParameter("codigo");
            Integer codigo = null;
            if (codigoTexto != null && !codigoTexto.isBlank()) {
                codigo = Integer.parseInt(codigoTexto.trim());
            }
            if (codigo == null) {
                exibirErro(request, response, "Gestor não informado.", ERROR_PAGE);
                return;
            }

            // exclui o gestor e os estoques dele
            ExclusaoCascata.excluirGestor(codigo);

            // redirect
            response.sendRedirect(request.getContextPath() + "/gestor/lista");

        } catch (NotFoundException nfe) {
            nfe.printStackTrace();
            exibirErro(request, response, nfe, ERROR_PAGE);
        } catch (NumberFormatException nmfe) {
            exibirErro(request, response, "Código do gestor inválido.", ERROR_PAGE);
        } catch (ConnectionFailedException cfe) {
            cfe.printStackTrace();
            exibirErro(request, response, cfe, ERROR_PAGE);
        } catch (Exception e) {
            e.printStackTrace();
            exibirErro(request, response, e, ERROR_PAGE);
        }
    }
}
