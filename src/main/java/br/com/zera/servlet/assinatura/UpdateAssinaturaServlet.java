package br.com.zera.servlet.assinatura;

import br.com.zera.dao.AssinaturaDAO;
import br.com.zera.dao.PlanoDAO;
import br.com.zera.dao.UnidadeDAO;
import br.com.zera.exception.ConnectionFailedException;
import br.com.zera.exception.NotFoundException;
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
import static br.com.zera.regex.Constants.SESSAO_UNIDADE;

/**
 * Servlet responsável por atualizar o cadastro de assinaturas
 *
 * Esta classe recebe os dados enviados via formulário HTTP, realiza a
 * validação das informações e delega as atualizações ao {@link AssinaturaDAO}. *
 *
 * @author Pedro Rufino
 * @since 2026-10-06
 */
@WebServlet(name = "UpdateAssinatura", value = "/assinatura/alterarAssinatura")
public class UpdateAssinaturaServlet extends HttpServlet {

    /**
     * Processa POST para atualizar  a Assiantura cadastrado baseado no antigo
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
            Unidade unidadeLogada = (Unidade) session.getAttribute(SESSAO_UNIDADE);
            if (unidadeLogada == null) {
                exibirErro(request, response, "Sessão expirada. Faça login novamente.", ERROR_PAGE);
                return;
            }

            // identifica qual assinatura alterar
            String cod = request.getParameter("codigo");
            if (cod == null || cod.trim().isEmpty()) {
                exibirErro(request, response, "Código da Assinatura não informado.", ERROR_PAGE);
                return;
            }
            int codigo = Integer.parseInt(cod.trim());

            String codPlanoParam = request.getParameter("codPlano");
            if (codPlanoParam == null || codPlanoParam.trim().isEmpty()) {
                exibirErro(request, response, "Plano não informado.", ERROR_PAGE);
                return;
            }
            int codPlano = Integer.parseInt(codPlanoParam.trim());

            AssinaturaDAO dao = new AssinaturaDAO();
            UnidadeDAO unidadeDAO = new UnidadeDAO();
            PlanoDAO planoDAO = new PlanoDAO();

            // Busca a assinatura atual
            Assinatura assinatura = dao.findByCodigo(codigo);
            if (assinatura == null) {
                exibirErro(request, response, "Assinatura não encontrada.", ERROR_PAGE);
                return;
            }

            Unidade unidade = unidadeDAO.findByCodigo(unidadeLogada.getCodigo());
            if (unidade == null) {
                exibirErro(request, response, "Unidade não encontrada. Faça login novamente.", ERROR_PAGE);
                return;
            }

            // Impede que o gestor altere assinatura de outra unidade trocando o código no HTML
            if (assinatura.getCodOrganizacao() != unidade.getCodOrganizacao()) {
                exibirErro(request, response, "Você não tem permissão para alterar esta assinatura.", ERROR_PAGE);
                return;
            }

            Plano plano = planoDAO.findByCodigo(codPlano);
            if (plano == null) {
                exibirErro(request, response, "Plano selecionado não existe.", ERROR_PAGE);
                return;
            }

            int duracaoMeses;
            try {
                duracaoMeses = Integer.parseInt(plano.getTempo().trim());
            } catch (NumberFormatException nfe) {
                exibirErro(request, response, "Duração do plano cadastrada em formato inválido.", ERROR_PAGE);
                return;
            }

            assinatura.setCodPlano(codPlano);
            assinatura.setDataFim(assinatura.getDataInicio().plusMonths(duracaoMeses));

            // Instancia as informações recebidas na Assinatura com o método
            dao.update(assinatura);

            // Redireciona para a lista de assinaturas após a inserção bem-sucedida
            response.sendRedirect(request.getContextPath() + "/assinatura/perfil");

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
            e.printStackTrace();
            exibirErro(request, response, e, ERROR_PAGE);
        }
    }
}