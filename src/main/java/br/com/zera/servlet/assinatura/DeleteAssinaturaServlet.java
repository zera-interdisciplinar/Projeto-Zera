package br.com.zera.servlet.assinatura;

import br.com.zera.dao.AssinaturaDAO;
import br.com.zera.exception.ConnectionFailedException;
import br.com.zera.exception.NotFoundException;
import br.com.zera.util.Sessao;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static br.com.zera.exception.ErroServlet.exibirErro;
import static br.com.zera.regex.Constants.ERROR_PAGE;

/**
 * Servlet responsável por excluir uma assinatura (apenas admin).
 *
 * Para encerrar uma assinatura sem apagar o histórico, prefira alterar o
 * status para "Cancelada" pelo {@link UpdateAssinaturaServlet}.
 *
 * @author Pedro Rufino
 * @since 2026-10-06
 */
@WebServlet(name = "DeleteAssinatura", value = "/assinatura/excluir")
public class DeleteAssinaturaServlet extends HttpServlet {

    /**
     * Processa POST para excluir a assinatura informada.
     *
     * @param request objeto HttpServletRequest com o parâmetro codigo
     * @param response objeto HttpServletResponse para redirecionamento ou erro
     * @throws IOException caso ocorra erro de entrada/saída ao responder
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        try {
            // só o admin pode excluir assinatura
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
                exibirErro(request, response, "Assinatura não informada.", ERROR_PAGE);
                return;
            }

            // exclui do banco
            new AssinaturaDAO().delete(codigo);

            // redirect
            response.sendRedirect(request.getContextPath() + "/assinatura/lista");

        } catch (NotFoundException nfe) {
            nfe.printStackTrace();
            exibirErro(request, response, nfe, ERROR_PAGE);
        } catch (NumberFormatException nmfe) {
            exibirErro(request, response, "Código da assinatura inválido.", ERROR_PAGE);
        } catch (ConnectionFailedException cfe) {
            cfe.printStackTrace();
            exibirErro(request, response, cfe, ERROR_PAGE);
        } catch (Exception e) {
            e.printStackTrace();
            exibirErro(request, response, e, ERROR_PAGE);
        }
    }
}
