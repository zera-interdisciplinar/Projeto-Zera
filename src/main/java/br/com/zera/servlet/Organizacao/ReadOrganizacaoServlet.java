package br.com.zera.servlet.Organizacao;

import java.io.*;
import br.com.zera.dao.*;
import br.com.zera.exception.ZeraException.*;
import br.com.zera.model.Organizacao;
import br.com.zera.model.Unidade;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import static br.com.zera.exception.ErroServlet.exibirErro;
import static br.com.zera.regex.Constants.ERROR_PAGE;

@WebServlet(name = "ReadOrganizacao", value = "/organizacao/perfil")


/**
 * Servlet responsável por mostrar as informações de organização cadastradas
 *
 * Esta classe recebe a requisição de visualização da organização cadastrada pelo gestor via formulário HTTP,
 * e as exibe com uma tela de resposta
 *
 * @author Mayte B
 * @since 2026-10-06
 *
 * @param request objeto que contém os dados enviados pelo formulário (JSP)
 * @param response objeto usado para enviar respostas ao cliente (redirecionamento ou erro)
 */
public class ReadOrganizacaoServlet extends HttpServlet {

    /**
     * Processa requisições GET para exibir organizações
     *
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            HttpSession session = request.getSession();
            Unidade unidadeLogada = (Unidade) session.getAttribute("unidadeLogada");
            if (unidadeLogada == null) {
                exibirErro(request, response, "Sessão expirada. Faça login novamente.", ERROR_PAGE);
                return;
            }

            int codUnidade = unidadeLogada.getCodigo();
            OrganizacaoDAO organizacaoDao = new OrganizacaoDAO();
            Organizacao organizacao = organizacaoDao.findByCodigo(codUnidade);

            if (organizacao == null) {
                exibirErro(request, response, "Organização não cadastrado.", ERROR_PAGE);
                return;
            }

            //confere se a organizacao

            request.setAttribute("cnpj", organizacao.getCnpj());
            request.setAttribute("nome", organizacao.getNome());
            request.setAttribute("email", organizacao.getEmail());

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