package br.com.zera.servlet.estoque;

import br.com.zera.dao.EstoqueDAO;
import br.com.zera.exception.ConnectionFailedException;
import br.com.zera.exception.NotFoundException;
import br.com.zera.model.Admin;
import br.com.zera.model.Unidade;
import br.com.zera.util.Sessao;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static br.com.zera.exception.ErroServlet.exibirErro;
import static br.com.zera.regex.Constants.ERROR_PAGE;

/**
 * Servlet responsável por excluir um estoque.
 *
 * O admin exclui qualquer estoque. O gestor só exclui estoques da própria unidade.
 *
 * @author Pedro Rufino
 * @since 2026-10-04
 */
@WebServlet(name = "DeleteEstoque", value = "/estoque/excluir")
public class DeleteEstoqueServlet extends HttpServlet {

    /**
     * Exclui um estoque por código, restringindo a unidade no perfil gestor.
     *
     * @param request requisição HTTP
     * @param response resposta HTTP
     * @throws IOException caso ocorra erro de entrada ou saída
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            Admin admin = Sessao.admin(request);
            Unidade unidadeLogada = Sessao.unidade(request);

            if (admin == null && unidadeLogada == null) {
                exibirErro(request, response, "Sessão expirada.", ERROR_PAGE);
                return;
            }

            String codigoTexto = request.getParameter("codigo");
            if (codigoTexto == null || codigoTexto.isBlank()) {
                exibirErro(request, response, "Estoque não informado.", ERROR_PAGE);
                return;
            }

            int codigo = Integer.parseInt(codigoTexto.trim());
            EstoqueDAO dao = new EstoqueDAO();

            if (admin != null) {
                dao.delete(codigo);
            } else {
                dao.deleteByUnidade(codigo, unidadeLogada.getCodigo());
            }

            response.sendRedirect(request.getContextPath() +
                    (admin != null ? "/estoque/lista" : "/estoque/perfil"));

        } catch (NotFoundException e) {
            exibirErro(request, response, e, ERROR_PAGE);
        } catch (NumberFormatException e) {
            exibirErro(request, response, "Código inválido.", ERROR_PAGE);
        } catch (ConnectionFailedException e) {
            exibirErro(request, response, e, ERROR_PAGE);
        } catch (Exception e) {
            e.printStackTrace();
            exibirErro(request, response, e, ERROR_PAGE);
        }
    }
}