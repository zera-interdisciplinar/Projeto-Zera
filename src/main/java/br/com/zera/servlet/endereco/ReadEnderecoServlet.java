package br.com.zera.servlet.endereco;

import br.com.zera.dao.EnderecoDAO;
import br.com.zera.dao.UnidadeDAO;
import br.com.zera.exception.ConnectionFailedException;
import br.com.zera.exception.NotFoundException;
import br.com.zera.model.Admin;
import br.com.zera.model.Unidade;
import br.com.zera.util.Sessao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static br.com.zera.exception.ErroServlet.exibirErro;
import static br.com.zera.regex.Constants.ERROR_PAGE;

/**
 * Servlet responsável por mostrar um endereço.
 *
 * O que aparece depende de quem está logado:
 * - Admin com o parâmetro codigo: formulário de edição do endereço.
 * - Admin com o parâmetro codUnidade: se a unidade já tem endereço, abre a edição;
 *   senão, abre o formulário de novo endereço com a unidade já selecionada
 *   (é para cá que o admin vem depois de criar uma unidade).
 * - Admin sem parâmetros: formulário de novo endereço.
 * - Gestor: endereço da própria unidade.
 *
 * @author Mayte B
 * @since 2026-09-18
 */
@WebServlet(name = "ReadEndereco", value = "/endereco/perfil")
public class ReadEnderecoServlet extends HttpServlet {

    private static final String EDITAR = "/WEB-INF/admin/editar-endereco.jsp";
    private static final String NOVO = "/WEB-INF/admin/novo-endereco.jsp";
    private static final String GESTOR = "/WEB-INF/gestor/endereco.jsp";

    /**
     * Processa requisições GET para exibir um endereço ou o formulário de novo endereço.
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
            // descobre quem está logado: admin ou gestor
            Admin admin = Sessao.admin(request);
            Unidade unidadeLogada = Sessao.unidade(request);
            EnderecoDAO dao = new EnderecoDAO();

            String pagina;
            if (admin != null) {
                // lista de unidades para o campo de seleção
                request.setAttribute("unidades", new UnidadeDAO().findAll());

                // lê o código
                String codigoTexto = request.getParameter("codigo");
                Integer codigo = null;
                if (codigoTexto != null && !codigoTexto.isBlank()) {
                    codigo = Integer.parseInt(codigoTexto.trim());
                }
                // codUnidade vem da tela da unidade
                String codUnidadeTexto = request.getParameter("codUnidade");
                Integer codUnidade = null;
                if (codUnidadeTexto != null && !codUnidadeTexto.isBlank()) {
                    codUnidade = Integer.parseInt(codUnidadeTexto.trim());
                }

                if (codigo != null) {
                    request.setAttribute("endereco", dao.findByCodigo(codigo));
                    pagina = EDITAR;
                } else if (codUnidade != null) {
                    try {
                        request.setAttribute("endereco", dao.findByCodUnidade(codUnidade));
                        pagina = EDITAR;
                    } catch (NotFoundException semEndereco) {
                        // unidade ainda sem endereço: abre o formulário novo
                        request.setAttribute("codUnidade", codUnidade);
                        pagina = NOVO;
                    }
                } else {
                    pagina = NOVO;
                }
            } else if (unidadeLogada != null) {
                try {
                    request.setAttribute("endereco", dao.findByCodUnidade(unidadeLogada.getCodigo()));
                } catch (NotFoundException semEndereco) {
                    // unidade sem endereço: a JSP mostra o formulário vazio
                    request.setAttribute("endereco", null);
                }
                pagina = GESTOR;
            } else {
                exibirErro(request, response, "Sessão expirada. Faça login novamente.", ERROR_PAGE);
                return;
            }

            // envia para a JSP
            request.getRequestDispatcher(pagina).forward(request, response);

        } catch (NotFoundException nfe) {
            exibirErro(request, response, "Endereço não cadastrado.", ERROR_PAGE);
        } catch (NumberFormatException nmfe) {
            exibirErro(request, response, "Código inválido.", ERROR_PAGE);
        } catch (ConnectionFailedException cfe) {
            cfe.printStackTrace();
            exibirErro(request, response, cfe, ERROR_PAGE);
        } catch (Exception e) {
            e.printStackTrace();
            exibirErro(request, response, "Erro inesperado encontrado", ERROR_PAGE);
        }
    }
}