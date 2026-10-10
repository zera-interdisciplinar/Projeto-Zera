package br.com.zera.servlet.Gestor;
import java.io.*;
import br.com.zera.dao.*;
import br.com.zera.exception.ZeraException.*;
import br.com.zera.model.Gestor;
import br.com.zera.model.Organizacao;
import br.com.zera.model.Unidade;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import static br.com.zera.exception.ErroServlet.exibirErro;
import static br.com.zera.regex.Constants.ERROR_PAGE;

@WebServlet(name = "ReadGestor", value = "/gestor/perfil")


/**
 * Servlet responsável por mostrar as informações de gestor cadastradas
 *
 * Esta classe recebe a requisição de visualização do gestor cadastrado via formulário HTTP,
 * e as exibe com uma tela de resposta
 *
 * @author Mayte B
 * @since 2026-10-06
 *
 * @param request objeto que contém os dados enviados pelo formulário (JSP)
 * @param response objeto usado para enviar respostas ao cliente (redirecionamento ou erro)
 */
public class ReadGestorServlet extends HttpServlet {

    /**
     * Processa requisições GET para exibir gestores das unidades da empresa.
     *
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        try {
            HttpSession session = request.getSession();
            Unidade unidadeLogada = (Unidade) session.getAttribute("unidadeLogada");

            if (unidadeLogada == null) {
                exibirErro(request, response, "Sessão expirada. Faça login novamente.", ERROR_PAGE);
                return;
            }

            int codUnidade = unidadeLogada.getCodigo();
            GestorDAO gestorDao = new GestorDAO();
            Gestor gestor = gestorDao.findByCodigo(codUnidade);

            if (gestor == null) {
                exibirErro(request, response, "Endereço não cadastrado.", ERROR_PAGE);
                return;
            }

            request.setAttribute("nome", gestor.getNome());
            request.setAttribute("email", gestor.getEmail());
            request.setAttribute("senha", gestor.getSenha());
            request.setAttribute("telefone", gestor.getTelefone());
            request.setAttribute("codUnidade", gestor.getCodUnidade());


            request.getRequestDispatcher("/WEB-INF/perfil.jsp").forward(request, response);
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
