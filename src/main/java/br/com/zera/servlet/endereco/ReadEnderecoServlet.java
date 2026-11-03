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
 * Exibe o cadastro ou a edição de endereço.
 * Para o admin, codUnidade abre a segunda etapa do cadastro de unidade
 * somente quando a unidade ainda não possui endereço.
 */
@WebServlet(name = "ReadEndereco", value = "/endereco/perfil")
public class ReadEnderecoServlet extends HttpServlet {

    private static final String EDITAR = "/WEB-INF/admin/endereco/editar-endereco.jsp";
    private static final String NOVO = "/WEB-INF/admin/endereco/novo-endereco.jsp";
    private static final String NOVA_UNIDADE_PT2 = "/WEB-INF/admin/unidade/nova-unidade-pt2.jsp";
    private static final String GESTOR = "/WEB-INF/gestor/endereco.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Admin admin = Sessao.admin(request);
            Unidade unidadeLogada = Sessao.unidade(request);
            EnderecoDAO dao = new EnderecoDAO();
            String pagina;

            if (admin != null) {
                if (request.getServletPath().equals("/unidade/etapa2")
                        && (request.getParameter("codUnidade") == null
                        || request.getParameter("codUnidade").isBlank())) {
                    exibirErro(request, response, "Unidade não informada para a segunda etapa.", ERROR_PAGE);
                    return;
                }
                request.setAttribute("unidades", new UnidadeDAO().findAll());

                String codigoTexto = request.getParameter("codigo");
                Integer codigo = null;
                if (codigoTexto != null && !codigoTexto.isBlank()) {
                    codigo = Integer.parseInt(codigoTexto.trim());
                }

                String codUnidadeTexto = request.getParameter("codUnidade");
                Integer codUnidade = null;
                if (codUnidadeTexto != null && !codUnidadeTexto.isBlank()) {
                    codUnidade = Integer.parseInt(codUnidadeTexto.trim());
                }

                if (codigo != null) {
                    request.setAttribute("endereco", dao.findByCodigo(codigo));
                    pagina = EDITAR;
                } else if (codUnidade != null) {
                    // Confere que o código recebido aponta para uma unidade real.
                    new UnidadeDAO().findByCodigo(codUnidade);
                    try {
                        request.setAttribute("endereco", dao.findByCodUnidade(codUnidade));
                        pagina = EDITAR;
                    } catch (NotFoundException semEndereco) {
                        request.setAttribute("codUnidade", codUnidade);
                        pagina = NOVA_UNIDADE_PT2;
                    }
                } else {
                    // Cadastro independente de endereço continua usando sua tela original.
                    pagina = NOVO;
                }
            } else if (unidadeLogada != null) {
                try {
                    request.setAttribute("endereco", dao.findByCodUnidade(unidadeLogada.getCodigo()));
                } catch (NotFoundException semEndereco) {
                    request.setAttribute("endereco", null);
                }
                pagina = GESTOR;
            } else {
                exibirErro(request, response, "Sessão expirada. Faça login novamente.", ERROR_PAGE);
                return;
            }

            request.getRequestDispatcher(pagina).forward(request, response);
        } catch (NotFoundException nfe) {
            exibirErro(request, response, "Unidade ou endereço não cadastrado.", ERROR_PAGE);
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
