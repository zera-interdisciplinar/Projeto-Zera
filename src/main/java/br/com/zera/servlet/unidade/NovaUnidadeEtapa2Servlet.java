package br.com.zera.servlet.unidade;

import br.com.zera.dao.UnidadeDAO;
import br.com.zera.exception.ConnectionFailedException;
import br.com.zera.exception.NotFoundException;
import br.com.zera.model.Unidade;
import br.com.zera.util.Sessao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static br.com.zera.exception.ErroServlet.exibirErro;
import static br.com.zera.regex.Constants.ERROR_PAGE;

/**
 * Abre a segunda etapa do cadastro de unidade (endereco).
 * URL: GET /unidade/etapa2?codUnidade=123
 */
@WebServlet(name = "NovaUnidadeEtapa2", urlPatterns = "/unidade/etapa2")
public class NovaUnidadeEtapa2Servlet extends HttpServlet {

    private static final String PAGINA =
            "/WEB-INF/admin/unidade/nova-unidade-pt2.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (Sessao.admin(request) == null) {
            exibirErro(request, response,
                    "Acesso permitido apenas ao administrador.", ERROR_PAGE);
            return;
        }

        String codigoTexto = request.getParameter("codUnidade");
        if (codigoTexto == null || codigoTexto.isBlank()) {
            exibirErro(request, response,
                    "Codigo da unidade nao informado.", ERROR_PAGE);
            return;
        }

        try {
            int codigo = Integer.parseInt(codigoTexto.trim());
            if (codigo <= 0) {
                exibirErro(request, response,
                        "Codigo da unidade invalido.", ERROR_PAGE);
                return;
            }

            // Confere se a unidade existe antes de permitir cadastrar seu endereco.
            Unidade unidade = new UnidadeDAO().findByCodigo(codigo);
            request.setAttribute("codUnidade", unidade.getCodigo());

            request.getRequestDispatcher(PAGINA).forward(request, response);

        } catch (NumberFormatException e) {
            exibirErro(request, response, "Codigo da unidade invalido.", ERROR_PAGE);
        } catch (NotFoundException e) {
            exibirErro(request, response, "Unidade nao encontrada.", ERROR_PAGE);
        } catch (ConnectionFailedException e) {
            exibirErro(request, response, e, ERROR_PAGE);
        } catch (Exception e) {
            e.printStackTrace();
            exibirErro(request, response, e, ERROR_PAGE);
        }
    }
}
