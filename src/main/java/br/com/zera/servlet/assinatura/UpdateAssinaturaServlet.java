package br.com.zera.servlet.assinatura;

import br.com.zera.dao.AssinaturaDAO;
import br.com.zera.dao.OrganizacaoDAO;
import br.com.zera.dao.PlanoDAO;
import br.com.zera.exception.ConnectionFailedException;
import br.com.zera.exception.NotFoundException;
import br.com.zera.model.Admin;
import br.com.zera.model.Assinatura;
import br.com.zera.model.Unidade;
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
import static br.com.zera.regex.Constants.STATUS_CANCELADA;

/**
 * Servlet responsável por atualizar uma assinatura.
 *
 * Funciona de um jeito para cada perfil:
 * - Admin: altera qualquer assinatura, inclusive trocando a organização.
 * - Gestor: altera só a assinatura da própria organização. Pode trocar o plano,
 *   mudar o status (Ativa ou Cancelada) e a data de término.
 *
 * A data de início nunca muda. Os campos que não vierem preenchidos mantêm o
 * valor que já está no banco.
 *
 * @author Pedro Rufino
 * @since 2026-10-06
 */
@WebServlet(name = "UpdateAssinatura", value = "/assinatura/alterar")
public class UpdateAssinaturaServlet extends HttpServlet {

    /**
     * Processa POST para atualizar a assinatura.
     *
     * @param request objeto HttpServletRequest com codigo, codPlano, dataFim, status
     *                e, para o admin, codOrganizacao
     * @param response objeto HttpServletResponse para redirecionamento ou erro
     * @throws IOException caso haja um erro de input/output (entrada/saída)
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        request.setCharacterEncoding("UTF-8");

        try {
            // descobre quem está logado: admin ou gestor
            Admin admin = Sessao.admin(request);
            Unidade unidadeLogada = Sessao.unidade(request);
            if (admin == null && unidadeLogada == null) {
                exibirErro(request, response, "Sessão expirada. Faça login novamente.", ERROR_PAGE);
                return;
            }

            // lê o código
            String codigoTexto = request.getParameter("codigo");
            Integer codigo = null;
            if (codigoTexto != null && !codigoTexto.isBlank()) {
                codigo = Integer.parseInt(codigoTexto.trim());
            }
            if (codigo == null) {
                exibirErro(request, response, "Assinatura não informada.", ERROR_PAGE);
                return;
            }

            AssinaturaDAO dao = new AssinaturaDAO();
            Assinatura existente = dao.findByCodigo(codigo);

            // gestor não pode mexer na assinatura de outra organização
            if (admin == null && existente.getCodOrganizacao() != unidadeLogada.getCodOrganizacao()) {
                exibirErro(request, response, "Esta assinatura não pertence à sua organização.", ERROR_PAGE);
                return;
            }

            // lê os campos do formulário
            String codPlanoTexto = request.getParameter("codPlano");
            Integer codPlano = null;
            if (codPlanoTexto != null && !codPlanoTexto.isBlank()) {
                codPlano = Integer.parseInt(codPlanoTexto.trim());
            }
            String dataFimTexto = request.getParameter("dataFim");
            LocalDate dataFim = null;
            if (dataFimTexto != null && !dataFimTexto.isBlank()) {
                dataFim = LocalDate.parse(dataFimTexto.trim());
            }
            String statusTexto = request.getParameter("status");
            String status = null;
            if (statusTexto != null && !statusTexto.isBlank()) {
                status = statusTexto.trim();
            }

            // só o admin troca a organização; para o gestor ela vem do banco
            int codOrganizacao = existente.getCodOrganizacao();
            if (admin != null) {
                String codOrganizacaoTexto = request.getParameter("codOrganizacao");
                if (codOrganizacaoTexto != null && !codOrganizacaoTexto.isBlank()) {
                    codOrganizacao = Integer.parseInt(codOrganizacaoTexto.trim());
                    // confere se a organização existe
                    new OrganizacaoDAO().findByCodigo(codOrganizacao);
                }
            }

            // campo vazio mantém o valor atual
            if (codPlano == null) {
                codPlano = existente.getCodPlano();
            }
            if (dataFim == null) {
                dataFim = existente.getDataFim();
            }
            if (status == null) {
                status = existente.getStatus();
            }

            // o plano tem que existir (o gestor só escolhe entre os planos cadastrados)
            new PlanoDAO().findByCodigo(codPlano);

            // o gestor só pode deixar a assinatura Ativa ou Cancelada
            if (admin == null && !status.equals(STATUS_ATIVA) && !status.equals(STATUS_CANCELADA)) {
                exibirErro(request, response, "Status inválido.", ERROR_PAGE);
                return;
            }

            // a data de início não muda: vem sempre do banco
            LocalDate dataInicio = existente.getDataInicio();

            // a data de fim tem que ser depois da de início
            if (!dataFim.isAfter(dataInicio)) {
                exibirErro(request, response, "A data de fim precisa ser depois da data de início.", ERROR_PAGE);
                return;
            }

            dao.update(new Assinatura(existente.getCodigo(), dataInicio, dataFim, status, codOrganizacao, codPlano));

            String destino;
            if (admin != null) {
                destino = "/assinatura/lista";
            } else {
                destino = "/assinatura/perfil";
            }
            // redirect
            response.sendRedirect(request.getContextPath() + destino);

        } catch (NotFoundException nfe) {
            nfe.printStackTrace();
            exibirErro(request, response, nfe, ERROR_PAGE);
        } catch (NumberFormatException nmfe) {
            exibirErro(request, response, "Código inválido.", ERROR_PAGE);
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