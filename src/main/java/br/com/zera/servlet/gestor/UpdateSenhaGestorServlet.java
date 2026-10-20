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
 * não consiga trocar a senha sem conhecê-la. A nova senha é gravada com hash.
 *
 * @author Pedro Rufino
 * @since 2026-10-16
 */
@WebServlet(name = "UpdateSenhaGestor", value = "/gestor/alterarSenha")
public class UpdateSenhaGestorServlet extends HttpServlet {

    /**
     * Processa POST para trocar a senha do gestor logado.
     *
     * @param request objeto HttpServletRequest com senhaAtual, novaSenha e confirmacao
     * @param response objeto HttpServletResponse para redirecionamento ou erro
     * @throws IOException caso haja um erro de input/output (entrada/saída)
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        // aceita acentos e caracteres especiais
        request.setCharacterEncoding("UTF-8");

        try {
            Gestor gestorLogado = Sessao.gestor(request);
            if (gestorLogado == null) {
                exibirErro(request, response, "Sessão expirada. Faça login novamente.", ERROR_PAGE);
                return;
            }

            // lê os campos do formulário
            String senhaAtualTexto = request.getParameter("senhaAtual");
            String senhaAtual = null;
            if (senhaAtualTexto != null && !senhaAtualTexto.isBlank()) {
                senhaAtual = senhaAtualTexto.trim();
            }
            String novaSenhaTexto = request.getParameter("novaSenha");
            String novaSenha = null;
            if (novaSenhaTexto != null && !novaSenhaTexto.isBlank()) {
                novaSenha = novaSenhaTexto.trim();
            }
            String confirmacaoTexto = request.getParameter("confirmacao");
            String confirmacao = null;
            if (confirmacaoTexto != null && !confirmacaoTexto.isBlank()) {
                confirmacao = confirmacaoTexto.trim();
            }

            if (senhaAtual == null || novaSenha == null) {
                exibirErro(request, response, "Preencha a senha atual e a nova senha.", ERROR_PAGE);
                return;
            }
            if (!novaSenha.equals(confirmacao)) {
                exibirErro(request, response, "A confirmação está diferente da senha informada.", ERROR_PAGE);
                return;
            }
            if (!Regex.validarSenha(novaSenha)) {
                exibirErro(request, response, "A senha precisa ter pelo menos 8 caracteres, um número e um caractere especial.", ERROR_PAGE);
                return;
            }

            // busca a senha salva para comparar com a atual
            GestorDAO dao = new GestorDAO();
            Gestor gestor = dao.findByCodigo(gestorLogado.getCodigo());

            if (!HashSenha.verificar(senhaAtual, gestor.getSenha())) {
                exibirErro(request, response, "Senha atual incorreta.", ERROR_PAGE);
                return;
            }

            // grava a nova senha com hash
            gestor.setSenha(HashSenha.gerarHash(novaSenha));
            dao.update(gestor);

            // redirect
            response.sendRedirect(request.getContextPath() + "/gestor/perfil");

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
}
