
package br.com.zera.servlet.gestor;

import br.com.zera.config.HashSenha;
import br.com.zera.dao.GestorDAO;
import br.com.zera.exception.ConnectionFailedException;
import br.com.zera.exception.NotFoundException;
import br.com.zera.model.Gestor;
import br.com.zera.regex.Regex;
import br.com.zera.util.Sessao;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static br.com.zera.exception.ErroServlet.exibirErro;
import static br.com.zera.regex.Constants.ERROR_PAGE;

/**
 * Servlet responsável por trocar a senha do gestor logado.
 *
 * Exige a senha atual, para que alguém com acesso a uma sessão aberta
 * não consiga trocar a senha sem conhecê-la.
 * A nova senha é gravada com hash.
 *
 * Os erros de validação retornam ao formulário do perfil.
 *
 * @author Pedro Rufino
 * @since 2026-10-16
 */
@WebServlet(name = "UpdateSenhaGestor", value = "/gestor/alterarSenha")
public class UpdateSenhaGestorServlet extends HttpServlet {

    /**
     * Processa POST para trocar a senha do gestor logado.
     *
     * @param request requisição com senhaAtual, novaSenha e confirmacao
     * @param response resposta para redirecionamento ou erro
     * @throws IOException caso ocorra erro de entrada ou saída
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        request.setCharacterEncoding("UTF-8");

        try {
            Gestor gestorLogado = Sessao.gestor(request);

            if (gestorLogado == null) {
                exibirErro(request, response,
                        "Sessão expirada. Faça login novamente.",
                        ERROR_PAGE);
                return;
            }

            String senhaAtual = request.getParameter("senhaAtual");
            String novaSenha = request.getParameter("novaSenha");
            String confirmacao = request.getParameter("confirmacao");

            if (senhaAtual == null || senhaAtual.isBlank()) {
                redirecionarErro(request, response, "atualVazia");
                return;
            }

            if (novaSenha == null || novaSenha.isBlank()) {
                redirecionarErro(request, response, "novaVazia");
                return;
            }

            if (confirmacao == null || confirmacao.isBlank()) {
                redirecionarErro(request, response, "confirmacaoVazia");
                return;
            }

            if (!novaSenha.equals(confirmacao)) {
                redirecionarErro(request, response, "confirmacao");
                return;
            }

            if (!Regex.validarSenha(novaSenha)) {
                redirecionarErro(request, response, "formato");
                return;
            }

            GestorDAO dao = new GestorDAO();
            Gestor gestor = dao.findByCodigo(gestorLogado.getCodigo());

            if (!HashSenha.verificar(senhaAtual, gestor.getSenha())) {
                redirecionarErro(request, response, "atual");
                return;
            }

            gestor.setSenha(HashSenha.gerarHash(novaSenha));
            dao.update(gestor);

            response.sendRedirect(
                    request.getContextPath() + "/gestor/perfil"
            );

        } catch (ConnectionFailedException cfe) {
            cfe.printStackTrace();
            exibirErro(request, response, cfe, ERROR_PAGE);

        } catch (NotFoundException nfe) {
            nfe.printStackTrace();
            exibirErro(request, response, nfe, ERROR_PAGE);

        } catch (Exception e) {
            e.printStackTrace();
            exibirErro(request, response, e, ERROR_PAGE);
        }
    }

    /**
     * Redireciona o gestor ao formulário com o código do erro.
     *
     * Nenhuma senha é enviada na URL.
     *
     * @param request requisição HTTP
     * @param response resposta HTTP
     * @param erro código do erro de validação
     * @throws IOException caso ocorra erro no redirecionamento
     */
    private void redirecionarErro(
            HttpServletRequest request,
            HttpServletResponse response,
            String erro
    ) throws IOException {

        response.sendRedirect(
                request.getContextPath()
                        + "/gestor/perfil?erroSenha=" + erro
        );
    }
}
