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
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static br.com.zera.exception.ErroServlet.exibirErro;
import static br.com.zera.regex.Constants.ERROR_PAGE;
import static br.com.zera.regex.Constants.STATUS_ATIVA;

/**
 * Servlet responsável por mostrar assinaturas.
 *
 * O que aparece depende de quem está logado:
 * - Admin com o parâmetro codigo: formulário de edição, com as listas de organizações e planos.
 * - Admin sem código: formulário de nova assinatura.
 * - Gestor: a assinatura da organização dele e o plano dessa assinatura.
 * - Gestor com o parâmetro editar: formulário de edição da assinatura, com a
 *   lista de planos para trocar de plano.
 *
 * @author Pedro Rufino
 * @since 2026-10-04
 */
@WebServlet(name = "ReadAssinatura", value = "/assinatura/perfil")
public class ReadAssinaturaServlet extends HttpServlet {

    /**
     * Processa requisições GET para exibir uma assinatura ou o formulário de nova assinatura.
     *
     * @param request objeto que contém os dados da requisição
     * @param response objeto usado para enviar respostas ao cliente (forward ou erro)
     * @throws ServletException se ocorrer falha no encaminhamento para a JSP
     * @throws IOException caso ocorra erro de entrada/saída ao responder
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // descobre quem está logado: admin ou gestor
            Admin admin = Sessao.admin(request);
            Unidade unidadeLogada = Sessao.unidade(request);
            AssinaturaDAO dao = new AssinaturaDAO();

            String pagina;
            if (admin != null) {
                // listas para os campos de seleção
                request.setAttribute("organizacoes", new OrganizacaoDAO().findAll());
                request.setAttribute("planos", new PlanoDAO().findAll());

                // lê o código
                String codigoTexto = request.getParameter("codigo");
                Integer codigo = null;
                if (codigoTexto != null && !codigoTexto.isBlank()) {
                    codigo = Integer.parseInt(codigoTexto.trim());
                }
                if (codigo == null) {
                    pagina = "/WEB-INF/admin/assinatura/nova-assinatura.jsp";
                } else {
                    request.setAttribute("assinatura", dao.findByCodigo(codigo));
                    pagina = "/WEB-INF/admin/assinatura/editar-assinatura.jsp";
                }
            } else if (unidadeLogada != null) {
                // busca a assinatura da organização do gestor
                Assinatura atual = assinaturaAtual(dao, unidadeLogada.getCodOrganizacao());
                // pode ser null se a organização não tiver assinatura
                request.setAttribute("assinatura", atual);
                if (atual != null) {
                    request.setAttribute("plano", new PlanoDAO().findByCodigo(atual.getCodPlano()));
                }

                if (request.getParameter("editar") == null) {
                    pagina = "/WEB-INF/gestor/assinatura.jsp";
                } else {
                    if (atual == null) {
                        exibirErro(request, response, "Sua organização não tem assinatura.", ERROR_PAGE);
                        return;
                    }
                    // nome da organização (campo bloqueado) e planos para o campo de seleção
                    request.setAttribute("organizacao", new OrganizacaoDAO().findByCodigo(atual.getCodOrganizacao()));
                    request.setAttribute("planos", new PlanoDAO().findAll());
                    pagina = "/WEB-INF/gestor/editar-assinatura.jsp";
                }
            } else {
                exibirErro(request, response, "Sessão expirada. Faça login novamente.", ERROR_PAGE);
                return;
            }

            // envia para a JSP
            request.getRequestDispatcher(pagina).forward(request, response);

        } catch (NotFoundException nfe) {
            exibirErro(request, response, "Assinatura não cadastrada.", ERROR_PAGE);
        } catch (NumberFormatException nmfe) {
            exibirErro(request, response, "Código da assinatura inválido.", ERROR_PAGE);
        } catch (ConnectionFailedException cfe) {
            cfe.printStackTrace();
            exibirErro(request, response, cfe, ERROR_PAGE);
        } catch (Exception e) {
            e.printStackTrace();
            exibirErro(request, response, "Erro inesperado encontrado", ERROR_PAGE);
        }
    }

    /**
     * Procura a assinatura atual de uma organização: a ativa que termina por
     * último. Se não houver nenhuma ativa (por exemplo, o gestor cancelou),
     * devolve a mais recente, para ele ainda conseguir ver e reativar.
     *
     * @param dao DAO de assinatura
     * @param codOrganizacao código da organização
     * @return a assinatura atual, ou null se a organização não tiver nenhuma
     */
    private Assinatura assinaturaAtual(AssinaturaDAO dao, int codOrganizacao) {
        Assinatura ativa = null;
        Assinatura maisRecente = null;
        for (Assinatura assinatura : dao.findAll()) {
            if (assinatura.getCodOrganizacao() != codOrganizacao) {
                continue;
            }
            if (maisRecente == null || assinatura.getDataFim().isAfter(maisRecente.getDataFim())) {
                maisRecente = assinatura;
            }
            boolean estaAtiva = STATUS_ATIVA.equalsIgnoreCase(assinatura.getStatus());
            if (estaAtiva && (ativa == null || assinatura.getDataFim().isAfter(ativa.getDataFim()))) {
                ativa = assinatura;
            }
        }
        if (ativa != null) {
            return ativa;
        }
        return maisRecente;
    }
}
