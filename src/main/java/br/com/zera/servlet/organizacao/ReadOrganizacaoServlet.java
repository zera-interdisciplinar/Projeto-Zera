package br.com.zera.servlet.organizacao;

import java.io.*;
import br.com.zera.dao.*;
import br.com.zera.exception.NotFoundException;
import br.com.zera.model.Organizacao;
import br.com.zera.model.Unidade;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import static br.com.zera.exception.ErroServlet.exibirErro;
import static br.com.zera.regex.Constants.ERROR_PAGE;
import static br.com.zera.regex.Constants.SESSAO_UNIDADE;

/**
 * Servlet responsável por mostrar as informações de organização cadastradas
 *
 * Esta classe recebe a requisição de visualização da organização da unidade
 * logada e a exibe em uma tela de resposta.
 *
 * @author Mayte B
 * @since 2026-10-06
 */
@WebServlet(name = "ReadOrganizacao", value = "/organizacao/perfil")
public class ReadOrganizacaoServlet extends HttpServlet {

    /**
     * Processa requisições GET para exibir a organização da unidade logada.
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
            Unidade unidadeLogada = (Unidade) session.getAttribute(SESSAO_UNIDADE);
            if (unidadeLogada == null) {
                exibirErro(request, response, "Sessão expirada. Faça login novamente.", ERROR_PAGE);
                return;
            }

            // a organização é a chave estrangeira da unidade logada
            OrganizacaoDAO organizacaoDao = new OrganizacaoDAO();
            Organizacao organizacao = organizacaoDao.findByCodigo(unidadeLogada.getCodOrganizacao());

            request.setAttribute("cnpj", organizacao.getCnpj());
            request.setAttribute("nome", organizacao.getNome());
            request.setAttribute("email", organizacao.getEmail());

            request.getRequestDispatcher("/WEB-INF/perfil-organizacao.jsp").forward(request, response);
        } catch (NotFoundException nfe) {
            exibirErro(request, response, "Organização não cadastrada.", ERROR_PAGE);
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
