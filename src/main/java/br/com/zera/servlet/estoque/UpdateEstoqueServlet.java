package br.com.zera.servlet.estoque;

import br.com.zera.dao.EstoqueDAO;
import br.com.zera.exception.ConnectionFailedException;
import br.com.zera.exception.NotFoundException;
import br.com.zera.model.Estoque;
import br.com.zera.model.Gestor;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import static br.com.zera.exception.ErroServlet.exibirErro;
import static br.com.zera.regex.Constants.ERROR_PAGE;
import static br.com.zera.regex.Constants.SESSAO_GESTOR;

/**
 * Servlet responsável por atualizar o cadastro de estoques
 *
 * Esta classe recebe os dados enviados via formulário HTTP, realiza a
 * validação das informações e delega as atualizações ao {@link EstoqueDAO}. *
 *
 * @author Pedro Rufino
 * @since 2026-10-04
 */
@WebServlet(name = "UpdateEstoque", value = "/estoque/alterarEstoque")
public class UpdateEstoqueServlet extends HttpServlet {

    /**
     * Processa POST para atualizar o Estoque cadastrado baseado no antigo
     *
     * @param request  objeto HttpServletRequest contendo os parâmetros da página
     * @param response objeto HttpResponse para redirecionamento ou foward
     * @throws java.io.IOException caso haja um erro de input/output (entrada/saída)
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        // garante que caracteres especiais cheguem corretos
        request.setCharacterEncoding("UTF-8");

        try {
            // Recupera o gestor guardado na sessão durante o login
            HttpSession session = request.getSession();
            Gestor gestorLogado = (Gestor) session.getAttribute(SESSAO_GESTOR);
            if (gestorLogado == null) {
                exibirErro(request, response, "Sessão expirada. Faça login novamente.", ERROR_PAGE);
                return;
            }

            // identifica qual estoque alterar
            String cod = request.getParameter("codigo");
            if (cod == null || cod.trim().isEmpty()) {
                exibirErro(request, response, "Código do estoque não informado.", ERROR_PAGE);
                return;
            }
            int codigo = Integer.parseInt(cod.trim());

            String espacoNaUnidade = request.getParameter("espacoNaUnidade");
            if (espacoNaUnidade == null || espacoNaUnidade.trim().isEmpty()) {
                exibirErro(request, response, "Espaço na unidade não informado.", ERROR_PAGE);
                return;
            }

            EstoqueDAO dao = new EstoqueDAO();

            // Busca o estoque atual
            Estoque estoque = dao.findByCodigo(codigo);

            // Impede que o gestor altere estoque de outra unidade trocando o código no HTML
            if (estoque.getCodUnidade() != gestorLogado.getCodUnidade()) {
                exibirErro(request, response, "Você não tem permissão para alterar este estoque.", ERROR_PAGE);
                return;
            }

            estoque.setEspacoNaUnidade(espacoNaUnidade.trim());

            // Instancia as informações recebidas no Estoque com o método
            dao.update(estoque);

            // Redireciona para a lista de estoques após a inserção bem-sucedida
            response.sendRedirect(request.getContextPath() + "/estoque/perfil");

        } catch (ConnectionFailedException cfe) {
            cfe.printStackTrace();
            exibirErro(request, response, cfe, ERROR_PAGE);
        } catch (NotFoundException nfe) {
            nfe.printStackTrace();
            exibirErro(request, response, nfe, ERROR_PAGE);
        } catch (NumberFormatException nmfe) {
            nmfe.printStackTrace();
            exibirErro(request, response, nmfe, ERROR_PAGE);
        } catch (Exception e) {
            exibirErro(request, response, e, ERROR_PAGE);
        }
    }
}
