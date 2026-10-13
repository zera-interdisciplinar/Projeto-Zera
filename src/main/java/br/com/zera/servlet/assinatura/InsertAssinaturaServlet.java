package br.com.zera.servlet.assinatura;

import br.com.zera.dao.AssinaturaDAO;
import br.com.zera.dao.PlanoDAO;
import br.com.zera.dao.UnidadeDAO;
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
import java.time.LocalDate;

import static br.com.zera.exception.ErroServlet.exibirErro;
import static br.com.zera.regex.Constants.ERROR_PAGE;
import static br.com.zera.regex.Constants.SESSAO_UNIDADE;

/**
 * Servlet responsável por processar o cadastro de assinaturas
 *
 * Esta classe recebe os dados enviados via formulário HTTP, realiza a
 * validação das informações e delega a persistência ao {@link AssinaturaDAO}. *
 *
 * @author Pedro Rufino
 * @since 2026-10-04
 */
@WebServlet(name = "InsertAssinatura", value = "/assinatura/inserirAssinatura")
public class InsertAssinaturaServlet extends HttpServlet {

    private static final String STATUS_INICIAL = "ativa";

    /**
     * Processa POST para inserir uma nova Assinatura.
     *
     * @param request objeto HttpServletRequest contendo os parâmetros do formulário
     * @param response objeto HttpServletResponse para redirecionamento ou forward
     * @throws IOException caso haja um erro de entrada/saída
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        AssinaturaDAO dao = new AssinaturaDAO();
        UnidadeDAO unidadeDAO = new UnidadeDAO();
        PlanoDAO planoDAO = new PlanoDAO();

        try {
            // Recupera o gestor guardado na sessão durante o login
            HttpSession session = request.getSession();
            Unidade unidadeLogada = (Unidade) session.getAttribute(SESSAO_UNIDADE);
            if (unidadeLogada == null) {
                exibirErro(request, response, "Sessão expirada. Faça login novamente.", ERROR_PAGE);
                return;
            }

            // Único dado que vem do formulário
            String codPlanoParam = request.getParameter("codPlano");
            if (codPlanoParam == null || codPlanoParam.trim().isEmpty()) {
                exibirErro(request, response, "Plano não informado.", ERROR_PAGE);
                return;
            }

            int codPlano = Integer.parseInt(codPlanoParam.trim());

            // O código veio do formulário e pode ter sido alterado, então confere se o plano existe
            Plano plano = planoDAO.findByCodigo(codPlano);
            if (plano == null) {
                exibirErro(request, response, "Plano selecionado não existe.", ERROR_PAGE);
                return;
            }

             // A unidade logada está ligada à organização. Confere se ela ainda existe no banco e obtém a organização a partir dela
            Unidade unidade = unidadeDAO.findByCodigo(unidadeLogada.getCodigo());
            if (unidade == null) {
                exibirErro(request, response, "Unidade não encontrada. Faça login novamente.", ERROR_PAGE);
                return;
            }
            int codOrganizacao = unidade.getCodOrganizacao();

            // Cada organização pode ter apenas uma assinatura, então percorre as existentes procurando alguma vinculada a esta organização
            boolean jaPossuiAssinatura = false;
            for (Assinatura a : dao.findAll()) {
                if (a.getCodOrganizacao() == codOrganizacao && STATUS_INICIAL.equals(a.getStatus())) {
                    jaPossuiAssinatura = true;
                    break;
                }
            }
            if (jaPossuiAssinatura) {
                exibirErro(request, response, "Esta organização já possui uma assinatura ativa.", ERROR_PAGE);
                return;
            }

            int duracaoMeses;
            try {
                duracaoMeses = Integer.parseInt(plano.getTempo().trim());
            } catch (NumberFormatException nfe) {
                exibirErro(request, response, "Duração do plano cadastrada em formato inválido.", ERROR_PAGE);
                return;
            }
            LocalDate dataInicio = LocalDate.now();
            LocalDate dataFim = dataInicio.plusMonths(duracaoMeses);

            // Cria o objeto model
            Assinatura model = new Assinatura(dataInicio, dataFim, STATUS_INICIAL, codOrganizacao, codPlano);

            // Instancia as informações recebidas no Estoque com o método
            dao.insert(model);

            // Redireciona para o perfil após a inserção bem-sucedida
            response.sendRedirect(request.getContextPath() + "/assinatura/perfil");

        } catch (NumberFormatException nfe) {
            exibirErro(request, response, "Plano inválido.", ERROR_PAGE);
        } catch (ZeraException ze) {
            ze.printStackTrace();
            exibirErro(request, response, ze, ERROR_PAGE);
        } catch (Exception e) {
            e.printStackTrace();
            exibirErro(request, response, e, ERROR_PAGE);
        }
    }
}
