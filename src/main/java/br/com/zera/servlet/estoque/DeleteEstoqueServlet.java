package br.com.zera.servlet.estoque;

import br.com.zera.dao.EstoqueDAO;
import br.com.zera.exception.ConnectionFailedException;
import br.com.zera.exception.NotFoundException;
import br.com.zera.model.Admin;
import br.com.zera.model.Estoque;
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
     * Processa POST para excluir o estoque informado.
     *
     * @param request objeto HttpServletRequest com o parâmetro codigo
     * @param response objeto HttpServletResponse para redirecionamento ou erro
     * @throws IOException caso ocorra erro de entrada/saída ao responder
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        try {
            // descobre quem está logado: admin ou gestor
            Admin admin = Sessao.admin(request);
            Unidade unidadeLogada = Sessao.unidade(request);
            if (admin == null && unidadeLogada == null) {
                exibirErro(request, response, "Sessão expirada. Faça login novamente.", ERROR_PAGE);
                return;
            }

            // lê o código
            String codigoTexto = request.getParameter("codigo");
            Integer codigo = null;
            if (codigoTexto != null && !codigoTexto.isBlank()) {
                codigo = Integer.parseInt(codigoTexto.trim());
            }
            if (codigo == null) {
                exibirErro(request, response, "Estoque não informado.", ERROR_PAGE);
                return;
            }

            EstoqueDAO dao = new EstoqueDAO();
            if (admin == null) {
                // gestor não pode excluir estoque de outra unidade
                Estoque estoque = dao.findByCodigo(codigo);
                if (estoque.getCodUnidade() != unidadeLogada.getCodigo()) {
                    exibirErro(request, response, "Este estoque não pertence à sua unidade.", ERROR_PAGE);
                    return;
                }
            }

            // exclui do banco
            dao.delete(codigo);

            String destino;
            if (admin != null) {
                destino = "/estoque/lista";
            } else {
                destino = "/estoque/perfil";
            }
            // redirect
            response.sendRedirect(request.getContextPath() + destino);

        } catch (NotFoundException nfe) {
            nfe.printStackTrace();
            exibirErro(request, response, nfe, ERROR_PAGE);
        } catch (NumberFormatException nmfe) {
            exibirErro(request, response, "Código do estoque inválido.", ERROR_PAGE);
        } catch (ConnectionFailedException cfe) {
            cfe.printStackTrace();
            exibirErro(request, response, cfe, ERROR_PAGE);
        } catch (Exception e) {
            e.printStackTrace();
            exibirErro(request, response, e, ERROR_PAGE);
        }
    }
}
