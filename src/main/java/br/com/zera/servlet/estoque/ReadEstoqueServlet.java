package br.com.zera.servlet.estoque;

import br.com.zera.dao.EstoqueDAO;
import br.com.zera.dao.GestorDAO;
import br.com.zera.dao.UnidadeDAO;
import br.com.zera.exception.ConnectionFailedException;
import br.com.zera.exception.NotFoundException;
import br.com.zera.model.Admin;
import br.com.zera.model.Estoque;
import br.com.zera.model.Gestor;
import br.com.zera.model.Unidade;
import br.com.zera.util.Sessao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static br.com.zera.exception.ErroServlet.exibirErro;
import static br.com.zera.regex.Constants.ERROR_PAGE;

/**
 * Servlet responsável por mostrar estoques.
 *
 * O que aparece depende de quem está logado:
 * - Admin com o parâmetro codigo: formulário de edição, com as listas de unidades e gestores.
 * - Admin sem código: formulário de novo estoque.
 * - Gestor: lista dos estoques da própria unidade, com os gestores dela para
 *   escolher o responsável.
 *
 * @author Pedro Rufino
 * @since 2026-10-04
 */
@WebServlet(name = "ReadEstoque", value = "/estoque/perfil")
public class ReadEstoqueServlet extends HttpServlet {

    /**
     * Exibe as informações dos estoques conforme o perfil autenticado.
     *
     * @param request requisição HTTP
     * @param response resposta HTTP
     * @throws ServletException caso ocorra erro no encaminhamento
     * @throws IOException caso ocorra erro de entrada ou saída
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Admin admin = Sessao.admin(request);
            Unidade unidadeLogada = Sessao.unidade(request);
            EstoqueDAO estoqueDAO = new EstoqueDAO();
            GestorDAO gestorDAO = new GestorDAO();
            String pagina;

            if (admin != null) {
                request.setAttribute("unidades", new UnidadeDAO().findAll());
                List<Gestor> gestores = gestorDAO.findAll();
                for (Gestor gestor : gestores) gestor.setSenha(null);
                request.setAttribute("gestores", gestores);

                String codigoTexto = request.getParameter("codigo");
                if (codigoTexto == null || codigoTexto.isBlank()) {
                    pagina = "/WEB-INF/admin/estoque/novo-estoque.jsp";
                } else {
                    int codigo = Integer.parseInt(codigoTexto.trim());
                    request.setAttribute("estoque", estoqueDAO.findByCodigo(codigo));
                    pagina = "/WEB-INF/admin/estoque/editar-estoque.jsp";
                }
            } else if (unidadeLogada != null) {
                int codUnidade = unidadeLogada.getCodigo();
                List<Gestor> gestores = new ArrayList<>();

                for (Gestor gestor : gestorDAO.findAll()) {
                    if (gestor.getCodUnidade() == codUnidade) {
                        gestor.setSenha(null);
                        gestores.add(gestor);
                    }
                }
                request.setAttribute("gestores", gestores);

                String editar = request.getParameter("editar");
                if (editar != null && !editar.isBlank()) {
                    int codigo = Integer.parseInt(editar.trim());
                    Estoque estoque = estoqueDAO.findByCodigo(codigo);
                    if (estoque.getCodUnidade() != codUnidade) {
                        exibirErro(request, response, "Estoque não pertence à sua unidade.", ERROR_PAGE);
                        return;
                    }
                    request.setAttribute("estoqueEditado", estoque);
                } else if (request.getParameter("novo") == null) {
                    String busca = request.getParameter("busca");
                    String responsavelTexto = request.getParameter("codResponsavel");
                    Integer codResponsavel = null;

                    if (responsavelTexto != null && !responsavelTexto.isBlank()) {
                        codResponsavel = Integer.parseInt(responsavelTexto.trim());
                        if (codResponsavel <= 0) throw new NumberFormatException();
                        boolean permitido = false;
                        for (Gestor gestor : gestores) {
                            if (gestor.getCodigo() == codResponsavel) {
                                permitido = true;
                                break;
                            }
                        }
                        if (!permitido) {
                            exibirErro(request, response, "Responsável não pertence à sua unidade.", ERROR_PAGE);
                            return;
                        }
                    }
                    request.setAttribute("estoques",
                            estoqueDAO.findByUnidade(codUnidade, busca, codResponsavel));
                }
                pagina = "/WEB-INF/gestor/estoques.jsp";
            } else {
                exibirErro(request, response, "Sessão expirada. Faça login novamente.", ERROR_PAGE);
                return;
            }

            request.getRequestDispatcher(pagina).forward(request, response);
        } catch (NotFoundException e) {
            exibirErro(request, response, "Estoque não cadastrado.", ERROR_PAGE);
        } catch (NumberFormatException e) {
            exibirErro(request, response, "Código ou responsável inválido.", ERROR_PAGE);
        } catch (ConnectionFailedException e) {
            e.printStackTrace();
            exibirErro(request, response, e, ERROR_PAGE);
        } catch (Exception e) {
            e.printStackTrace();
            exibirErro(request, response, "Erro inesperado encontrado.", ERROR_PAGE);
        }
    }
}
