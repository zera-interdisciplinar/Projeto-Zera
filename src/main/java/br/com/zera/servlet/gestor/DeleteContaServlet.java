package br.com.zera.servlet.gestor;

import br.com.zera.config.HashSenha;
import br.com.zera.dao.GestorDAO;
import br.com.zera.exception.ConnectionFailedException;
import br.com.zera.exception.NotFoundException;
import br.com.zera.model.Gestor;
import br.com.zera.model.Unidade;
import br.com.zera.util.ExclusaoCascata;
import br.com.zera.util.Sessao;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import static br.com.zera.exception.ErroServlet.exibirErro;
import static br.com.zera.regex.Constants.ERROR_PAGE;

/**
 * Servlet responsável por excluir a conta do gestor logado ("Excluir minha conta").
 *
 * Ao excluir a conta, tudo o que é da unidade do gestor é apagado junto:
 * estoques, gestores, endereço e a própria unidade. Se a organização ficar
 * sem nenhuma unidade, as assinaturas e a organização também são apagadas.
 *
 * Para confirmar, o gestor precisa digitar a senha atual. Depois da exclusão,
 * a sessão é encerrada e ele volta para a página inicial.
 *
 * @author Pedro Rufino
 * @since 2026-10-17
 */
@WebServlet(name = "DeleteConta", value = "/gestor/excluirConta")
public class DeleteContaServlet extends HttpServlet {

    /**
     * Processa POST para excluir a conta do gestor logado e tudo o que é da unidade dele.
     *
     * @param request objeto HttpServletRequest com o parâmetro senha
     * @param response objeto HttpServletResponse para redirecionamento ou erro
     * @throws IOException caso ocorra erro de entrada/saída ao responder
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        request.setCharacterEncoding("UTF-8");

        try {
            // só o próprio gestor exclui a conta dele
            Gestor gestorLogado = Sessao.gestor(request);
            Unidade unidadeLogada = Sessao.unidade(request);
            if (gestorLogado == null || unidadeLogada == null) {
                exibirErro(request, response, "Sessão expirada. Faça login novamente.", ERROR_PAGE);
                return;
            }

            // confirma com a senha atual antes de apagar
            String senha = request.getParameter("senha");
            Gestor gestor = new GestorDAO().findByCodigo(gestorLogado.getCodigo());
            if (senha == null || senha.isEmpty() || !HashSenha.verificar(senha, gestor.getSenha())) {
                exibirErro(request, response, "Senha incorreta. A conta não foi excluída.", ERROR_PAGE);
                return;
            }

            int codUnidade = unidadeLogada.getCodigo();
            int codOrganizacao = unidadeLogada.getCodOrganizacao();

            // apaga a unidade e tudo o que é dela (estoques, gestores e endereço)
            ExclusaoCascata.excluirUnidade(codUnidade);

            // se a organização ficou sem unidades, apaga as assinaturas e a organização
            if (!ExclusaoCascata.organizacaoTemUnidades(codOrganizacao)) {
                ExclusaoCascata.excluirOrganizacao(codOrganizacao);
            }

            // encerra a sessão, porque a conta não existe mais
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }

            // redirect
            response.sendRedirect(request.getContextPath() + "/");

        } catch (NotFoundException nfe) {
            nfe.printStackTrace();
            exibirErro(request, response, nfe, ERROR_PAGE);
        } catch (ConnectionFailedException cfe) {
            cfe.printStackTrace();
            exibirErro(request, response, cfe, ERROR_PAGE);
        } catch (Exception e) {
            e.printStackTrace();
            exibirErro(request, response, e, ERROR_PAGE);
        }
    }
}
