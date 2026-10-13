package br.com.zera.servlet.gestor;
import java.io.*;
import br.com.zera.dao.*;
import br.com.zera.exception.NotFoundException;
import br.com.zera.model.Gestor;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import static br.com.zera.exception.ErroServlet.exibirErro;
import static br.com.zera.regex.Constants.ERROR_PAGE;

/**
 * Servlet responsável por mostrar as informações do gestor cadastrado
 *
 * Esta classe recebe a requisição de visualização do perfil do gestor logado
 * e o exibe em uma tela de resposta.
 *
 * @author Mayte B
 * @since 2026-10-06
 */
@WebServlet(name = "ReadGestor", value = "/gestor/perfil")
public class ReadGestorServlet extends HttpServlet {

    /**
     * Processa requisições GET para exibir o perfil do gestor logado.
     *
     * @param request objeto que contém os dados da requisição
     * @param response objeto usado para enviar respostas ao cliente (forward ou erro)
     * @throws IOException caso ocorra erro de entrada/saída ao responder
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        try {
            HttpSession session = request.getSession();
            Gestor gestorLogado = (Gestor) session.getAttribute("Gestor Logado");

            if (gestorLogado == null) {
                exibirErro(request, response, "Sessão expirada. Faça login novamente.", ERROR_PAGE);
                return;
            }

            // busca pelo código do próprio gestor
            GestorDAO gestorDao = new GestorDAO();
            Gestor gestor = gestorDao.findByCodigo(gestorLogado.getCodigo());

            // a senha não é enviada para a tela
            request.setAttribute("nome", gestor.getNome());
            request.setAttribute("email", gestor.getEmail());
            request.setAttribute("telefone", gestor.getTelefone());
            request.setAttribute("codUnidade", gestor.getCodUnidade());

            request.getRequestDispatcher("/WEB-INF/perfil-gestor.jsp").forward(request, response);
        } catch (NotFoundException nfe) {
            exibirErro(request, response, "Gestor não cadastrado.", ERROR_PAGE);
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

