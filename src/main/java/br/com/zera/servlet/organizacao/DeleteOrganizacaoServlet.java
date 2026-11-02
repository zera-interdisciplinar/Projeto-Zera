
package br.com.zera.servlet.organizacao;

import br.com.zera.dao.OrganizacaoDAO;
import br.com.zera.exception.ConnectionFailedException;
import br.com.zera.exception.NotFoundException;
import br.com.zera.model.Organizacao;
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
     * Exclui uma organização identificada pelo código ou CNPJ.
     *
     * @param request requisição HTTP
     * @param response resposta HTTP
     * @throws IOException caso ocorra erro de entrada ou saída
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        request.setCharacterEncoding("UTF-8");
        try {
            if (Sessao.admin(request) == null) {
                exibirErro(request, response, "Acesso permitido apenas ao administrador.", ERROR_PAGE);
                return;
            }

            String codigoTexto = request.getParameter("codigo");
            String cnpj = request.getParameter("cnpj");
            boolean temCodigo = codigoTexto != null && !codigoTexto.isBlank();
            boolean temCnpj = cnpj != null && !cnpj.isBlank();

            if (!temCodigo && !temCnpj) {
                exibirErro(request, response, "Informe o código ou CNPJ da organização.", ERROR_PAGE);
                return;
            }

            OrganizacaoDAO dao = new OrganizacaoDAO();
            Organizacao organizacao;

            if (temCodigo) {
                int codigo = Integer.parseInt(codigoTexto.trim());
                organizacao = dao.findByCodigo(codigo);
                if (temCnpj && !organizacao.getCnpj().equals(cnpj.trim())) {
                    exibirErro(request, response, "O código e o CNPJ não correspondem à mesma organização.", ERROR_PAGE);
                    return;
                }
            } else {
                organizacao = dao.findByCnpj(cnpj.trim());
            }

            ExclusaoCascata.excluirOrganizacao(organizacao.getCodigo());
            response.sendRedirect(request.getContextPath() + "/organizacao/lista");

        } catch (NotFoundException e) {
            exibirErro(request, response, e, ERROR_PAGE);
        } catch (NumberFormatException e) {
            exibirErro(request, response, "Código inválido.", ERROR_PAGE);
        } catch (ConnectionFailedException e) {
            e.printStackTrace();
            exibirErro(request, response, e, ERROR_PAGE);
        } catch (Exception e) {
            e.printStackTrace();
            exibirErro(request, response, e, ERROR_PAGE);
        }
    }
}
