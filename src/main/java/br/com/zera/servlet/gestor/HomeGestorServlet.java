
package br.com.zera.servlet.gestor;

import br.com.zera.dao.AssinaturaDAO;
import br.com.zera.dao.EstoqueDAO;
import br.com.zera.dao.PlanoDAO;
import br.com.zera.dao.UnidadeDAO;
import br.com.zera.exception.ConnectionFailedException;
import br.com.zera.model.Assinatura;
import br.com.zera.model.Estoque;
import br.com.zera.model.Gestor;
import br.com.zera.model.Unidade;
import br.com.zera.util.Sessao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import static br.com.zera.exception.ErroServlet.exibirErro;
import static br.com.zera.regex.Constants.ERROR_PAGE;
import static br.com.zera.regex.Constants.STATUS_ATIVA;

/**
 * Servlet responsável pela tela inicial do gestor.
 * Mostra o nome do gestor, a quantidade de estoques e unidades,
 * a assinatura ativa, o plano e os dados dos gráficos.
 *
 * @author Pedro Rufino
 * @since 2026-10-16
 */
@WebServlet(name = "HomeGestor", value = "/gestor/home")
public class HomeGestorServlet extends HttpServlet {

    /** Página da home do gestor */
    private static final String PAGINA = "/WEB-INF/gestor/home.jsp";

    /**
     * Processa GET para exibir a home do gestor logado.
     *
     * @param request objeto HttpServletRequest que recebe os dados como atributos
     * @param response objeto HttpServletResponse usado para exibir a página ou o erro
     * @throws ServletException caso ocorra erro ao encaminhar para a página
     * @throws IOException caso ocorra erro de entrada/saída ao responder
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Gestor gestor = Sessao.gestor(request);
            Unidade unidade = Sessao.unidade(request);

            if (gestor == null || unidade == null) {
                exibirErro(request, response, "Sessão expirada. Faça login novamente.", ERROR_PAGE);
                return;
            }

            // Conta os estoques da unidade do gestor
            int estoquesNaUnidade = 0;
            Map<String, Integer> estoquesPorLocal = new LinkedHashMap<>();

            for (Estoque estoque : new EstoqueDAO().findAll()) {
                if (estoque.getCodUnidade() == unidade.getCodigo()) {
                    estoquesNaUnidade++;
                }
            }

            // Conta as unidades pertencentes à organização do gestor
            int totalUnidadesOrganizacao = 0;
            Map<String, Integer> unidadesPorEstado = new LinkedHashMap<>();

            for (Unidade unidadeEncontrada : new UnidadeDAO().findAll()) {
                if (unidadeEncontrada.getCodOrganizacao() == unidade.getCodOrganizacao()) {
                    totalUnidadesOrganizacao++;
                }
            }

            // Procura a assinatura ativa da organização (a que termina por último)
            Assinatura assinaturaAtiva = null;

            for (Assinatura assinatura : new AssinaturaDAO().findAll()) {
                boolean daOrganizacao = assinatura.getCodOrganizacao() == unidade.getCodOrganizacao();
                boolean ativa = STATUS_ATIVA.equalsIgnoreCase(assinatura.getStatus());

                if (daOrganizacao && ativa) {
                    if (assinaturaAtiva == null || assinatura.getDataFim().isAfter(assinaturaAtiva.getDataFim())) {
                        assinaturaAtiva = assinatura;
                    }
                }
            }

            // Envia os dados dos cards para a JSP
            request.setAttribute("nomeGestor", gestor.getNome());
            request.setAttribute("estoquesNaUnidade", estoquesNaUnidade);
            request.setAttribute("totalUnidadesOrganizacao", totalUnidadesOrganizacao);
            request.setAttribute("assinatura", assinaturaAtiva);

            if (assinaturaAtiva != null) {
                request.setAttribute("plano", new PlanoDAO().findByCodigo(assinaturaAtiva.getCodPlano()));
            }

            // Envia os dados dos gráficos para a JSP
            request.setAttribute("estoquesPorLocal", estoquesPorLocal);
            request.setAttribute("unidadesPorEstado", unidadesPorEstado);

            // Envia para a JSP
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
