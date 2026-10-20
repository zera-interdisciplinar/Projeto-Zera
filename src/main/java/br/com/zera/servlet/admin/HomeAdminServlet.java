package br.com.zera.servlet.admin;

import br.com.zera.dao.AssinaturaDAO;
import br.com.zera.dao.EstoqueDAO;
import br.com.zera.dao.OrganizacaoDAO;
import br.com.zera.dao.UnidadeDAO;
import br.com.zera.exception.ConnectionFailedException;
import br.com.zera.model.Admin;
import br.com.zera.model.Assinatura;
import br.com.zera.util.Sessao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static br.com.zera.exception.ErroServlet.exibirErro;
import static br.com.zera.regex.Constants.ERROR_PAGE;
import static br.com.zera.regex.Constants.STATUS_ATIVA;

/**
 * Servlet responsável pela tela inicial do admin.
 *
 * Calcula os números exibidos nos cards da home: total de organizações,
 * total de unidades, total de estoques, assinaturas ativas e assinaturas que vencem nos próximos 30 dias.
 *
 * @author Pedro Rufino
 * @since 2026-10-16
 */
@WebServlet(name = "HomeAdmin", value = "/admin/home")
public class HomeAdminServlet extends HttpServlet {

    /** Página da home do admin */
    private static final String PAGINA = "/WEB-INF/admin/home.jsp";

    /** Quantidade de dias considerada para "vencendo em breve" */
    private static final int DIAS_VENCIMENTO = 30;

    /**
     * Processa GET para exibir a home do admin com os totais do sistema.
     *
     * @param request objeto HttpServletRequest que recebe os totais como atributos
     * @param response objeto HttpServletResponse usado para exibir a página ou o erro
     * @throws ServletException caso ocorra erro ao encaminhar para a página
     * @throws IOException caso ocorra erro de entrada/saída ao responder
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            Admin admin = Sessao.admin(request);
            if (admin == null) {
                exibirErro(request, response, "Acesso permitido apenas ao administrador.", ERROR_PAGE);
                return;
            }

            List<Assinatura> assinaturas = new AssinaturaDAO().findAll();
            LocalDate hoje = LocalDate.now();
            LocalDate limite = hoje.plusDays(DIAS_VENCIMENTO);

            // conta as assinaturas ativas e, entre elas, as que vencem nos próximos 30 dias
            int ativas = 0;
            int vencendo = 0;
            for (Assinatura assinatura : assinaturas) {
                if (STATUS_ATIVA.equalsIgnoreCase(assinatura.getStatus())) {
                    ativas++;
                    LocalDate fim = assinatura.getDataFim();
                    if (fim != null && !fim.isBefore(hoje) && !fim.isAfter(limite)) {
                        vencendo++;
                    }
                }
            }

            request.setAttribute("nomeAdmin", admin.getNome());
            request.setAttribute("totalOrganizacoes", new OrganizacaoDAO().findAll().size());
            request.setAttribute("totalUnidades", new UnidadeDAO().findAll().size());
            request.setAttribute("totalEstoques", new EstoqueDAO().findAll().size());
            request.setAttribute("assinaturasAtivas", ativas);
            request.setAttribute("assinaturasVencendo", vencendo);

            request.getRequestDispatcher(PAGINA).forward(request, response);

        } catch (ConnectionFailedException cfe) {
            cfe.printStackTrace();
            exibirErro(request, response, cfe, ERROR_PAGE);
        } catch (Exception e) {
            e.printStackTrace();
            exibirErro(request, response, "Erro inesperado encontrado", ERROR_PAGE);
        }
    }
}
