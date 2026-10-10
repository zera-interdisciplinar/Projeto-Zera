package br.com.zera.servlet.Organizacao;

import br.com.zera.dao.OrganizacaoDAO;
import br.com.zera.model.Organizacao;
import br.com.zera.model.Unidade;
import br.com.zera.regex.Regex;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.logging.Logger;

import static br.com.zera.exception.ErroServlet.exibirErro;
import static br.com.zera.regex.Constants.ERROR_PAGE;

/**
 * Servlet responsável por buscar uma organização pelo CNPJ, usado no fluxo
 * de criação de unidade: o gestor informa o CNPJ para verificar se a
 * organização já está cadastrada antes de vincular a nova unidade a ela.
 *
 * @author Mayte B
 * @since 2026-10-06
 */
@WebServlet(name = "SearchOrganizacao", value = "/organizacao/buscar")
public class SearchOrganizacaoServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(SearchOrganizacaoServlet.class.getName());

    /**
     * Processa a requisição de busca de organização pelo CNPJ.
     * Se encontrada, encaminha para a tela de criação de unidade já com a
     * organização vinculada. Caso contrário, informa que não foi encontrada
     * e oferece o cadastro de uma organização nova.
     *
     * @param request objeto que contém os dados enviados pelo formulário (JSP)
     * @param response objeto usado para enviar respostas ao cliente
     * @throws ServletException se ocorrer falha no processamento da requisição
     * @throws IOException se ocorrer falha de entrada/saída ao ler a requisição ou encaminhar a resposta
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Valida se há um gestor logado
            HttpSession session = request.getSession();
            Unidade unidadeLogada = (Unidade) session.getAttribute("unidadeLogada");
            if (unidadeLogada == null) {
                exibirErro(request, response, "Sessão expirada. Faça login novamente.", ERROR_PAGE);
                return;
            }

            int codUnidade = unidadeLogada.getCodigo();

            String cnpjS = request.getParameter("cnpj");

            if (cnpjS == null || cnpjS.isBlank()) {
                exibirErro(request, response, "Informe o CNPJ da organização.", ERROR_PAGE);
                return;
            }

            //Instancia página de erro caso o CPF inserido seja inválido
            if(Regex.validarCNPJ(cnpjS) == false){
                exibirErro(request, response, "CNPJ inválido", ERROR_PAGE);
                return;
            }
            long cnpj = Long.parseLong(cnpjS.trim());

            OrganizacaoDAO organizacaoDao = new OrganizacaoDAO();
            Organizacao organizacao = organizacaoDao.findByCnpj(cnpj);

            if (organizacao == null) {
                // Não encontrada: a JSP deve oferecer o link para cadastrar uma organização nova
                request.setAttribute("cnpjBuscado", cnpj);
                request.setAttribute("organizacaoNaoEncontrada", true);
                request.getRequestDispatcher("/WEB-INF/buscarOrganizacao.jsp").forward(request, response);
                return;
            }

            // Encontrada: segue direto para a criação da unidade já vinculada a essa organização
            request.setAttribute("organizacao", organizacao);
            request.getRequestDispatcher("/WEB-INF/novaUnidade.jsp").forward(request, response);

        } catch (ServletException se) {
            exibirErro(request, response, se, ERROR_PAGE);
        } catch (Exception e) {
            exibirErro(request, response, e, ERROR_PAGE);
        }
    }
}