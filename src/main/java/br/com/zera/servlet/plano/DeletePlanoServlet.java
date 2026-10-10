package br.com.zera.servlet.plano;

import br.com.zera.dao.AssinaturaDAO;
import br.com.zera.dao.PlanoDAO;
import br.com.zera.exception.ZeraException;
import br.com.zera.model.Assinatura;
import br.com.zera.model.Plano;
import br.com.zera.model.Unidade;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import static br.com.zera.exception.ErroServlet.exibirErro;
import static br.com.zera.regex.Constants.ERROR_PAGE;

/**
 * Servlet responsável por excluir os planos cadastrados
 *
 * Esta classe recebe a requisição de exclusão do plano via formulário HTTP,
 * confere se nenhuma assinatura usa o plano e delega a exclusão ao {@link PlanoDAO}.
 *
 * @author Pedro Rufino
 * @since 2026-10-07
 */
@WebServlet(name = "DeletePlano", value = "/plano/deletarPlano")
public class DeletePlanoServlet extends HttpServlet {

    /**
     * Método responsável por processar a requisição de exclusão de um plano.
     * É acionado quando o usuário envia a requisição de remoção do sistema.
     *
     * @param request objeto que contém os dados enviados pelo formulário (JSP)
     * @param response objeto usado para enviar respostas ao cliente (redirecionamento ou erro)
     * @throws IOException caso haja um erro de entrada/saída ao responder
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        PlanoDAO dao = new PlanoDAO();
        AssinaturaDAO assinaturaDAO = new AssinaturaDAO();

        try {
            // Recupera a unidade guardada na sessão durante o login
            HttpSession session = request.getSession();
            Unidade unidadeLogada = (Unidade) session.getAttribute("unidadeLogada");
            if (unidadeLogada == null) {
                exibirErro(request, response, "Sessão expirada. Faça login novamente.", ERROR_PAGE);
                return;
            }

            //captura o código do plano enviado pelo formulário
            String cod = request.getParameter("codigo");
            if (cod == null || cod.trim().isEmpty()) {
                exibirErro(request, response, "Código do plano não informado.", ERROR_PAGE);
                return;
            }
            int codigo = Integer.parseInt(cod.trim());

            //confere se o plano existe
            Plano plano = dao.findByCodigo(codigo);
            if (plano == null) {
                exibirErro(request, response, "Plano não encontrado.", ERROR_PAGE);
                return;
            }

            //impede excluir um plano que ainda é usado por alguma assinatura
            for (Assinatura a : assinaturaDAO.findAll()) {
                if (a.getCodPlano() == codigo) {
                    exibirErro(request, response,
                            "Este plano não pode ser excluído porque há assinaturas vinculadas a ele.", ERROR_PAGE);
                    return;
                }
            }

            //chama função de deletar o plano baseado no código validado
            dao.delete(codigo);
            //envia página de resposta bem-sucedida da requisição de exclusão
            response.sendRedirect(request.getContextPath() + "/operacaoBemSucedida");

        } catch (NumberFormatException nfe) {
            exibirErro(request, response, "Código do plano inválido.", ERROR_PAGE);
        } catch (ZeraException ze) {
            exibirErro(request, response, ze, ERROR_PAGE);
            ze.printStackTrace();
        } catch (Exception e) {
            exibirErro(request, response, e, ERROR_PAGE);
            e.printStackTrace();
        }
    }
}