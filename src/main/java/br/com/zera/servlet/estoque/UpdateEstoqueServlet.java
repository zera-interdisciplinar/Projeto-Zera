package br.com.zera.servlet.estoque;

import br.com.zera.dao.EstoqueDAO;
import br.com.zera.dao.GestorDAO;
import br.com.zera.dao.UnidadeDAO;
import br.com.zera.exception.ConnectionFailedException;
import br.com.zera.exception.NotFoundException;
import br.com.zera.model.Admin;
import br.com.zera.model.Estoque;
import br.com.zera.model.Gestor;
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
 * Servlet responsável por atualizar um estoque.
 *
 * Os dois perfis mandam o codigo do estoque. O admin pode alterar
 * qualquer estoque e trocá-lo de unidade. O gestor só altera estoques da
 * própria unidade, e a unidade não muda. O responsável precisa ser um gestor
 * da mesma unidade do estoque.
 *
 * @author Pedro Rufino
 * @since 2026-10-04
 */
@WebServlet(name = "UpdateEstoque", value = "/estoque/alterar")
public class UpdateEstoqueServlet extends HttpServlet {

    /**
     * Atualiza o estoque respeitando as permissões de cada perfil.
     *
     * @param request requisição HTTP
     * @param response resposta HTTP
     * @throws IOException caso ocorra erro de entrada ou saída
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
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
            Estoque existente = dao.findByCodigo(codigo);
            int codUnidade = existente.getCodUnidade();

            if (admin != null) {
                String unidadeTexto = request.getParameter("codUnidade");
                if (unidadeTexto != null && !unidadeTexto.isBlank()) {
                    codUnidade = Integer.parseInt(unidadeTexto.trim());
                    new UnidadeDAO().findByCodigo(codUnidade);
                }
            } else if (existente.getCodUnidade() != unidadeLogada.getCodigo()) {
                exibirErro(request, response, "Estoque não pertence à sua unidade.", ERROR_PAGE);
                return;
            }

            String espaco = request.getParameter("espacoNaUnidade");
            if (espaco == null || espaco.isBlank()) {
                exibirErro(request, response, "Informe o espaço do estoque.", ERROR_PAGE);
                return;
            }

            String responsavelTexto = request.getParameter("codResponsavel");
            int codResponsavel = existente.getCodResponsavel();
            if (responsavelTexto != null && !responsavelTexto.isBlank()) {
                codResponsavel = Integer.parseInt(responsavelTexto.trim());
            }

            Gestor responsavel = new GestorDAO().findByCodigo(codResponsavel);
            if (responsavel.getCodUnidade() != codUnidade) {
                exibirErro(request, response, "Responsável precisa pertencer à unidade.", ERROR_PAGE);
                return;
            }

            Estoque atualizado = new Estoque(codigo, espaco.trim(), codResponsavel, codUnidade);
            if (admin != null) {
                dao.update(atualizado);
            } else {
                dao.updateByUnidade(atualizado, unidadeLogada.getCodigo());
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