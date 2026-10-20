package br.com.zera.servlet.plano;

import br.com.zera.dao.PlanoDAO;
import br.com.zera.exception.ConnectionFailedException;
import br.com.zera.exception.NotFoundException;
import br.com.zera.model.Admin;
import br.com.zera.model.Gestor;
import br.com.zera.util.Sessao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static br.com.zera.exception.ErroServlet.exibirErro;
import static br.com.zera.regex.Constants.ERROR_PAGE;

/**
 * Servlet responsável por mostrar planos.
 *
 * O que aparece depende de quem está logado:
 * - Admin com o parâmetro codigo: formulário de edição do plano.
 * - Admin sem código: formulário de novo plano.
 * - Gestor: lista dos planos disponíveis (somente leitura).
 *
 * @author Pedro Rufino
 * @since 2026-10-07
 */
@WebServlet(name = "ReadPlano", value = "/plano/perfil")
public class ReadPlanoServlet extends HttpServlet {

    /**
     * Processa requisições GET para exibir um plano, a lista de planos ou o formulário de novo plano.
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
            Gestor gestor = Sessao.gestor(request);
            PlanoDAO dao = new PlanoDAO();

            String pagina;
            if (admin != null) {
                // lê o código
                String codigoTexto = request.getParameter("codigo");
                Integer codigo = null;
                if (codigoTexto != null && !codigoTexto.isBlank()) {
                    codigo = Integer.parseInt(codigoTexto.trim());
                }
                if (codigo == null) {
                    pagina = "/WEB-INF/admin/novo-plano.jsp";
                } else {
                    request.setAttribute("plano", dao.findByCodigo(codigo));
                    pagina = "/WEB-INF/admin/editar-plano.jsp";
                }
            } else if (gestor != null) {
                // o gestor só vê os planos
                request.setAttribute("planos", dao.findAll());
                pagina = "/WEB-INF/gestor/planos.jsp";
            } else {
                exibirErro(request, response, "Sessão expirada. Faça login novamente.", ERROR_PAGE);
                return;
            }

            // envia para a JSP
            request.getRequestDispatcher(pagina).forward(request, response);

        } catch (NotFoundException nfe) {
            exibirErro(request, response, "Plano não cadastrado.", ERROR_PAGE);
        } catch (NumberFormatException nmfe) {
            exibirErro(request, response, "Código do plano inválido.", ERROR_PAGE);
        } catch (ConnectionFailedException cfe) {
            cfe.printStackTrace();
            exibirErro(request, response, cfe, ERROR_PAGE);
        } catch (Exception e) {
            e.printStackTrace();
            exibirErro(request, response, "Erro inesperado encontrado", ERROR_PAGE);
        }
    }
}
