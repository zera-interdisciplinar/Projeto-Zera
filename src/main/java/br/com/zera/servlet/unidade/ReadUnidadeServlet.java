package br.com.zera.servlet.unidade;

import java.io.*;
import br.com.zera.dao.*;
import br.com.zera.exception.NotFoundException;
import br.com.zera.model.Unidade;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import static br.com.zera.exception.ErroServlet.exibirErro;
import static br.com.zera.regex.Constants.ERROR_PAGE;

/**
 * Servlet responsável por mostrar as informações de unidade cadastradas
 *
 * Esta classe recebe a requisição de visualização da unidade logada
 * e a exibe em uma tela de resposta.
 *
 * @author Mayte B
 * @since 2026-10-02
 */
@WebServlet(name = "ReadUnidade", value = "/unidade/perfil")
public class ReadUnidadeServlet extends HttpServlet {

    /**
     * Processa requisições GET para exibir os dados da unidade logada.
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
            HttpSession session = request.getSession();
            Unidade unidadeLogada = (Unidade) session.getAttribute("Unidade Logada");
            if (unidadeLogada == null) {
                exibirErro(request, response, "Sessão expirada. Faça login novamente.", ERROR_PAGE);
                return;
            }

            // busca pelo código da própria unidade
            UnidadeDAO dao = new UnidadeDAO();
            Unidade unidade = dao.findByCodigo(unidadeLogada.getCodigo());

            request.setAttribute("cnpj", unidade.getCnpj());
            request.setAttribute("email", unidade.getEmail());

            request.getRequestDispatcher("/WEB-INF/perfil-unidade.jsp").forward(request, response);
        } catch (NotFoundException nfe) {
            exibirErro(request, response, "Unidade não cadastrada.", ERROR_PAGE);
        } catch (ServletException se) {
            exibirErro(request, response, se, ERROR_PAGE);
            se.printStackTrace();
        }catch(IOException ioe){
            exibirErro(request, response, ioe, ERROR_PAGE);
            ioe.printStackTrace();
        }catch (Exception e) {
            exibirErro(request, response, "Erro inesperado encontrado", ERROR_PAGE);
            e.printStackTrace();
        }
    }
}
