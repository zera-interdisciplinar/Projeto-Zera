package br.com.zera.servlet.endereco;

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
 * Servlet responsável por excluir um endereço e sua unidade.
 *
 * Apenas o admin pode excluir: ele escolhe o endereço na lista e confirma
 * no modal "Deseja mesmo excluir?". O código chega por parâmetro.
 *
 * A exclusão é em cascata: a unidade vinculada, seu gestor e seus estoques
 * também são apagados (veja {@link ExclusaoCascata}).
 *
 * @author Mayte B
 * @since 2026-09-28
 */
@WebServlet(name = "DeleteEndereco", value = "/endereco/excluir")
public class DeleteEnderecoServlet extends HttpServlet {

    /**
     * Processa POST para excluir o endereço e os dados vinculados.
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
            if (codigoTexto == null || codigoTexto.isBlank()) {
                exibirErro(request, response, "Endereço não informado.", ERROR_PAGE);
                return;
            }

            int codigo = Integer.parseInt(codigoTexto.trim());
            if (codigo <= 0) {
                exibirErro(request, response, "Código do endereço inválido.", ERROR_PAGE);
                return;
            }

            // exclui a unidade, o endereço, o gestor e os estoques na mesma transação
            ExclusaoCascata.excluirEndereco(codigo);

            // redirect
            response.sendRedirect(request.getContextPath() + "/endereco/lista");

        } catch (NotFoundException nfe) {
            exibirErro(request, response, nfe, ERROR_PAGE);
        } catch (NumberFormatException nmfe) {
            exibirErro(request, response, "Código do endereço inválido.", ERROR_PAGE);
        } catch (ConnectionFailedException cfe) {
            cfe.printStackTrace();
            exibirErro(request, response, cfe, ERROR_PAGE);
        } catch (Exception e) {
            e.printStackTrace();
            exibirErro(request, response, e, ERROR_PAGE);
        }
    }
}
