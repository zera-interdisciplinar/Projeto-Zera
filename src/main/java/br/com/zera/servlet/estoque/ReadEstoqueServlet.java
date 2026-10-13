package br.com.zera.servlet.estoque;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import br.com.zera.dao.*;
import br.com.zera.exception.ConnectionFailedException;
import br.com.zera.model.Estoque;
import br.com.zera.model.Gestor;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import static br.com.zera.exception.ErroServlet.exibirErro;
import static br.com.zera.regex.Constants.ERROR_PAGE;

/**
 * Servlet responsável por mostrar as informações de estoque cadastradas
 *
 * Esta classe recebe a requisição de visualização do estoque cadastrado pelo gestor via formulário HTTP,
 * e as exibe com uma tela de resposta
 *
 * @author Pedro Rufino
 * @since 2026-10-04
 */
@WebServlet(name = "ReadEstoque", value = "/estoque/perfil")
public class ReadEstoqueServlet extends HttpServlet {

    /**
     * Processa requisições GET para exibir estoques da empresa.
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
            Gestor gestorLogado = (Gestor) session.getAttribute("gestorLogado");
            if (gestorLogado == null) {
                exibirErro(request, response, "Sessão expirada. Faça login novamente.", ERROR_PAGE);
                return;
            }

            EstoqueDAO estoqueDao = new EstoqueDAO();
            List<Estoque> todos = estoqueDao.findAll();

            // Mantém só os estoques da unidade do gestor, para ele não ver os de outras empresas
            List<Estoque> estoques = new ArrayList<>();
            for (Estoque estoque : todos) {
                if (estoque.getCodUnidade() == gestorLogado.getCodUnidade()) {
                    estoques.add(estoque);
                }
            }

            request.setAttribute("estoques", estoques);

            request.getRequestDispatcher("/WEB-INF/estoque/perfil.jsp").forward(request, response);
        } catch (ConnectionFailedException cfe) {
            cfe.printStackTrace();
            exibirErro(request, response, cfe, ERROR_PAGE);
        } catch (Exception e) {
            e.printStackTrace();
            exibirErro(request, response, "Erro inesperado encontrado.", ERROR_PAGE);
        }
    }
}
