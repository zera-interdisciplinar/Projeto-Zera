package br.com.zera.servlet.assinatura;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import br.com.zera.dao.*;
import br.com.zera.exception.ConnectionFailedException;
import br.com.zera.exception.ZeraException;
import br.com.zera.model.Assinatura;
import br.com.zera.model.Unidade;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import static br.com.zera.exception.ErroServlet.exibirErro;
import static br.com.zera.regex.Constants.ERROR_PAGE;
import static br.com.zera.regex.Constants.SESSAO_UNIDADE;

/**
 * Servlet responsável por mostrar as informações de assinaturas cadastradas
 *
 * Esta classe recebe a requisição de visualização da assinatura cadastrada pelo gestor via formulário HTTP,
 * e as exibe com uma tela de resposta
 *
 * @author Pedro Rufino
 * @since 2026-10-04
 */
@WebServlet(name = "ReadAssinatura", value = "/assinatura/perfil")
public class ReadAssinaturaServlet extends HttpServlet {

    /**
     * Processa requisições GET para exibir assinatura da empresa.
     *@param request  requisição HTTP; recebe a lista de estoques como atributo
     *@param response resposta HTTP usada para exibir a página ou o erro
     *@throws ServletException caso ocorra erro ao encaminhar para a página
     *@throws IOException      caso ocorra erro de entrada/saída ao responder
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // Recupera o gestor guardado na sessão durante o login
            HttpSession session = request.getSession();
            Unidade unidadeLogada = (Unidade) session.getAttribute(SESSAO_UNIDADE);
            if (unidadeLogada == null) {
                exibirErro(request, response, "Sessão expirada. Faça login novamente.", ERROR_PAGE);
                return;
            }

            AssinaturaDAO assinaturaDAO = new AssinaturaDAO();
            UnidadeDAO unidadeDAO = new UnidadeDAO();

            Unidade unidade = unidadeDAO.findByCodigo(unidadeLogada.getCodigo());
            if (unidade == null) {
                exibirErro(request, response, "Unidade não encontrada. Faça login novamente.", ERROR_PAGE);
                return;
            }
            int codOrganizacao = unidade.getCodOrganizacao();

            // Mantém só os estoques da unidade do gestor, para ele não ver os de outras empresas
            List<Assinatura> assinaturas = new ArrayList<>();
            for (Assinatura assinatura : assinaturaDAO.findAll()) {
                if (assinatura.getCodOrganizacao() == codOrganizacao) {
                    assinaturas.add(assinatura);
                }
            }

            // Envia a lista filtrada para a página de perfil
            request.setAttribute("assinaturas", assinaturas);

            request.getRequestDispatcher("/WEB-INF/assinatura/perfil.jsp").forward(request, response);
        } catch (ConnectionFailedException cfe) {
            cfe.printStackTrace();
            exibirErro(request, response, cfe, ERROR_PAGE);
        } catch (ZeraException ze) {
            ze.printStackTrace();
            exibirErro(request, response, ze, ERROR_PAGE);
        } catch (Exception e) {
            e.printStackTrace();
            exibirErro(request, response, "Erro inesperado encontrado.", ERROR_PAGE);
        }
    }
}
