package br.com.zera.servlet.Endereco;

import java.io.*;
import br.com.zera.dao.*;
import br.com.zera.exception.ZeraException.*;
import br.com.zera.model.Endereco;
import br.com.zera.model.Gestor;
import br.com.zera.model.Unidade;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import static br.com.zera.exception.ErroServlet.exibirErro;
import static br.com.zera.regex.Constants.ERROR_PAGE;

@WebServlet(name = "ReadEndereco", value = "/endereco/perfil")


/**
 * Servlet responsável por mostrar as informações de endereço cadastradas
 *
 * Esta classe recebe a requisição de visualização do endereço cadastrado pelo gestor via formulário HTTP,
 * e as exibe com uma tela de resposta
 *
 * @author Mayte B
 * @since 2026-09-18
 *
 * @param request objeto que contém os dados enviados pelo formulário (JSP)
 * @param response objeto usado para enviar respostas ao cliente (redirecionamento ou erro)
 */
public class ReadEnderecoServlet extends HttpServlet {

    /**
     * Processa requisições GET para exibir endereços da empresa.
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
            EnderecoDAO enderecoDao = new EnderecoDAO();
            Endereco endereco = enderecoDao.findByCodigo(codUnidade);

            if (endereco == null) {
                exibirErro(request, response, "Endereço não cadastrado.", ERROR_PAGE);
                return;
            }

            request.setAttribute("bairro", endereco.getBairro());
            request.setAttribute("numero", endereco.getNumero());
            request.setAttribute("cep", endereco.getCep());
            request.setAttribute("logradouro", endereco.getLogradouro());
            request.setAttribute("cidade", endereco.getCidade());
            request.setAttribute("estado", endereco.getEstado());


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
