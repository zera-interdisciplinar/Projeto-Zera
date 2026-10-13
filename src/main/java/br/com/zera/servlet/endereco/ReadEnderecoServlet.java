package br.com.zera.servlet.endereco;

import java.io.*;
import br.com.zera.dao.*;
import br.com.zera.exception.NotFoundException;
import br.com.zera.model.Endereco;
import br.com.zera.model.Unidade;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import static br.com.zera.exception.ErroServlet.exibirErro;
import static br.com.zera.regex.Constants.ERROR_PAGE;

/**
 * Servlet responsável por mostrar as informações de endereço cadastradas
 *
 * Esta classe recebe a requisição de visualização do endereço da unidade logada
 * e o exibe em uma tela de resposta.
 *
 * @author Mayte B
 * @since 2026-09-18
 */
@WebServlet(name = "ReadEndereco", value = "/endereco/perfil")
public class ReadEnderecoServlet extends HttpServlet {

    /**
     * Processa requisições GET para exibir o endereço da unidade logada.
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
            // resgata a unidade logada na sessão
            HttpSession session = request.getSession();
            Unidade unidadeLogada = (Unidade) session.getAttribute("Unidade Logada");
            if (unidadeLogada == null) {
                exibirErro(request, response, "Sessão expirada. Faça login novamente.", ERROR_PAGE);
                return;
            }

            // busca pela chave estrangeira cod_unidade
            Endereco endereco = EnderecoDAO.findByCodUnidade(unidadeLogada.getCodigo());

            request.setAttribute("bairro", endereco.getBairro());
            request.setAttribute("numero", endereco.getNumero());
            request.setAttribute("cep", endereco.getCep());
            request.setAttribute("logradouro", endereco.getLogradouro());
            request.setAttribute("cidade", endereco.getCidade());
            request.setAttribute("estado", endereco.getEstado());

            request.getRequestDispatcher("/WEB-INF/perfil-endereco.jsp").forward(request, response);
        } catch (NotFoundException nfe) {
            exibirErro(request, response, "Endereço não cadastrado.", ERROR_PAGE);
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
