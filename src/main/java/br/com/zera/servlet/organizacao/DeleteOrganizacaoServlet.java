package br.com.zera.servlet.organizacao;

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
 * Servlet responsável por excluir uma organização.
 *
 * Apenas o admin pode excluir: ele escolhe a organização na lista e confirma
 * no modal "Deseja mesmo excluir?". O código chega por parâmetro.
 *
 * A exclusão é em cascata: todas as unidades (com tudo delas) e as
 * assinaturas da organização também são apagadas (veja {@link ExclusaoCascata}).
 *
 * @author Mayte B
 * @since 2026-10-06
 */
@WebServlet(name = "DeleteOrganizacao", value = "/organizacao/excluir")
public class DeleteOrganizacaoServlet extends HttpServlet {

    /**
     * Processa POST para excluir a organização informada.
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
                exibirErro(request, response, "Organização não informada.", ERROR_PAGE);
                return;
            }

            // exclui a organização e tudo o que é dela
            ExclusaoCascata.excluirOrganizacao(codigo);

            // redirect
            response.sendRedirect(request.getContextPath() + "/organizacao/lista");

        } catch (NotFoundException nfe) {
            nfe.printStackTrace();
            exibirErro(request, response, nfe, ERROR_PAGE);
        } catch (NumberFormatException nmfe) {
            exibirErro(request, response, "Código da organização inválido.", ERROR_PAGE);
        } catch (ConnectionFailedException cfe) {
            cfe.printStackTrace();
            exibirErro(request, response, cfe, ERROR_PAGE);
        } catch (Exception e) {
            e.printStackTrace();
            exibirErro(request, response, e, ERROR_PAGE);
        }
    }
}
