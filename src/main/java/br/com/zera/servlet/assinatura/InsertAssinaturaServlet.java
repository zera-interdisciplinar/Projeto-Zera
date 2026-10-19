package br.com.zera.servlet.assinatura;

import br.com.zera.dao.AssinaturaDAO;
import br.com.zera.dao.OrganizacaoDAO;
import br.com.zera.dao.PlanoDAO;
import br.com.zera.exception.ConnectionFailedException;
import br.com.zera.exception.NotFoundException;
import br.com.zera.model.Assinatura;
import br.com.zera.model.Plano;
import br.com.zera.util.Sessao;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import static br.com.zera.exception.ErroServlet.exibirErro;
import static br.com.zera.regex.Constants.ERROR_PAGE;
import static br.com.zera.regex.Constants.STATUS_ATIVA;

/**
 * Servlet responsável por cadastrar uma assinatura pela área do admin.
 *
 * A data de início é sempre a data de hoje (não vem do formulário). Campos
 * opcionais: se a data de fim vier vazia, soma a duração do plano; se o status vier vazio,
 * usa "Ativa". A assinatura da organização nova é criada pelo fluxo de
 * cadastro (ConfirmarCadastroServlet).
 *
 * @author Pedro Rufino
 * @since 2026-10-04
 */
@WebServlet(name = "InsertAssinatura", value = "/assinatura/inserir")
public class InsertAssinaturaServlet extends HttpServlet {

    /**
     * Processa POST para inserir uma nova assinatura.
     *
     * @param request objeto HttpServletRequest com codOrganizacao, codPlano, dataFim e status
     * @param response objeto HttpServletResponse para redirecionamento ou erro
     * @throws IOException caso haja um erro de input/output (entrada/saída)
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        request.setCharacterEncoding("UTF-8");

        try {
            // só o admin pode fazer isso
            if (Sessao.admin(request) == null) {
                exibirErro(request, response, "Acesso permitido apenas ao administrador.", ERROR_PAGE);
                return;
            }

            // lê os campos do formulário
            String codOrganizacaoTexto = request.getParameter("codOrganizacao");
            Integer codOrganizacao = null;
            if (codOrganizacaoTexto != null && !codOrganizacaoTexto.isBlank()) {
                codOrganizacao = Integer.parseInt(codOrganizacaoTexto.trim());
            }
            String codPlanoTexto = request.getParameter("codPlano");
            Integer codPlano = null;
            if (codPlanoTexto != null && !codPlanoTexto.isBlank()) {
                codPlano = Integer.parseInt(codPlanoTexto.trim());
            }
            if (codOrganizacao == null || codPlano == null) {
                exibirErro(request, response, "Selecione a organização e o plano.", ERROR_PAGE);
                return;
            }

            // confere se a organização e o plano existem
            new OrganizacaoDAO().findByCodigo(codOrganizacao);
            Plano plano = new PlanoDAO().findByCodigo(codPlano);

            // a assinatura sempre começa hoje
            LocalDate dataInicio = LocalDate.now();

            String dataFimTexto = request.getParameter("dataFim");
            LocalDate dataFim = null;
            if (dataFimTexto != null && !dataFimTexto.isBlank()) {
                dataFim = LocalDate.parse(dataFimTexto.trim());
            }
            // sem data de fim, soma a duração do plano
            if (dataFim == null) {
                dataFim = dataInicio.plusMonths(plano.getDuracaoEmMeses());
            }
            // a data de fim tem que ser depois da de início
            if (!dataFim.isAfter(dataInicio)) {
                exibirErro(request, response, "A data de fim precisa ser depois da data de início.", ERROR_PAGE);
                return;
            }

            String statusTexto = request.getParameter("status");
            String status = null;
            if (statusTexto != null && !statusTexto.isBlank()) {
                status = statusTexto.trim();
            }
            // sem status, a assinatura já nasce ativa
            if (status == null) {
                status = STATUS_ATIVA;
            }

            new AssinaturaDAO().insert(new Assinatura(dataInicio, dataFim, status, codOrganizacao, codPlano));

            // redirect
            response.sendRedirect(request.getContextPath() + "/assinatura/lista");

        } catch (NotFoundException nfe) {
            nfe.printStackTrace();
            exibirErro(request, response, nfe, ERROR_PAGE);
        } catch (NumberFormatException nmfe) {
            exibirErro(request, response, "Organização ou plano inválidos.", ERROR_PAGE);
        } catch (DateTimeParseException dtpe) {
            exibirErro(request, response, "Data inválida.", ERROR_PAGE);
        } catch (ConnectionFailedException cfe) {
            cfe.printStackTrace();
            exibirErro(request, response, cfe, ERROR_PAGE);
        } catch (Exception e) {
            e.printStackTrace();
            exibirErro(request, response, e, ERROR_PAGE);
        }
    }
}
