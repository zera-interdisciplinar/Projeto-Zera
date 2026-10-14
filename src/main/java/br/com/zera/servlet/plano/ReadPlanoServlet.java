package br.com.zera.servlet.plano;

import br.com.zera.dao.PlanoDAO;
import br.com.zera.exception.ZeraException;
import br.com.zera.model.Plano;
import br.com.zera.model.Unidade;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

import static br.com.zera.exception.ErroServlet.exibirErro;
import static br.com.zera.regex.Constants.ERROR_PAGE;
import static br.com.zera.regex.Constants.SESSAO_UNIDADE;

/**
 * Servlet responsável por mostrar os planos cadastrados
 *
 * Esta classe busca todos os planos no banco por meio do {@link PlanoDAO}
 * e os envia para a página de exibição. Como o plano é um catálogo válido
 * para todas as organizações, a lista não é filtrada.
 *
 * @author Pedro Rufino
 * @since 2026-10-07
 */
@WebServlet(name = "ReadPlano", value = "/plano/perfil")
public class ReadPlanoServlet extends HttpServlet {

    /**
     * Processa requisições GET para exibir os planos cadastrados.
     *
     * @param request objeto HttpServletRequest que recebe a lista de planos como atributo
     * @param response objeto HttpServletResponse usado para exibir a página ou o erro
     * @throws ServletException caso ocorra erro ao encaminhar para a página
     * @throws IOException caso ocorra erro de entrada/saída ao responder
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        PlanoDAO dao = new PlanoDAO();

        try {
            // Recupera a unidade guardada na sessão durante o login
            HttpSession session = request.getSession();
            Unidade unidadeLogada = (Unidade) session.getAttribute(SESSAO_UNIDADE);
            if (unidadeLogada == null) {
                exibirErro(request, response, "Sessão expirada. Faça login novamente.", ERROR_PAGE);
                return;
            }

            // Plano é um catálogo geral, então todos os planos são exibidos, sem filtro
            List<Plano> planos = dao.findAll();

            // Envia a lista para a página de planos
            request.setAttribute("planos", planos);
            request.getRequestDispatcher("/WEB-INF/perfil.jsp").forward(request, response);

        } catch (ZeraException ze) {
            ze.printStackTrace();
            exibirErro(request, response, ze, ERROR_PAGE);
        } catch (Exception e) {
            e.printStackTrace();
            exibirErro(request, response, "Erro inesperado encontrado.", ERROR_PAGE);
        }
    }
}