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
     * Processa requisições GET para exibir estoques ou o formulário de novo estoque.
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
            EstoqueDAO dao = new EstoqueDAO();
            GestorDAO gestorDAO = new GestorDAO();

            String pagina;
            if (admin != null) {
                // listas para os campos de seleção
                request.setAttribute("unidades", new UnidadeDAO().findAll());
                request.setAttribute("gestores", gestorDAO.findAll());

                // lê o código
                String codigoTexto = request.getParameter("codigo");
                Integer codigo = null;
                if (codigoTexto != null && !codigoTexto.isBlank()) {
                    codigo = Integer.parseInt(codigoTexto.trim());
                }
                if (codigo == null) {
                    pagina = "/WEB-INF/admin/novo-estoque.jsp";
                } else {
                    request.setAttribute("estoque", dao.findByCodigo(codigo));
                    pagina = "/WEB-INF/admin/editar-estoque.jsp";
                }
            } else if (unidadeLogada != null) {
                int codUnidade = unidadeLogada.getCodigo();

                // filtra só o que é da unidade do gestor
                List<Estoque> estoques = new ArrayList<>();
                for (Estoque estoque : dao.findAll()) {
                    if (estoque.getCodUnidade() == codUnidade) {
                        estoques.add(estoque);
                    }
                }
                List<Gestor> gestores = new ArrayList<>();
                for (Gestor gestor : gestorDAO.findAll()) {
                    if (gestor.getCodUnidade() == codUnidade) {
                        // não manda o hash da senha para a JSP
                        gestor.setSenha(null);
                        gestores.add(gestor);
                    }
                }

                request.setAttribute("estoques", estoques);
                request.setAttribute("gestores", gestores);
                pagina = "/WEB-INF/gestor/estoques.jsp";
            } else {
                exibirErro(request, response, "Sessão expirada. Faça login novamente.", ERROR_PAGE);
                return;
            }

            // envia para a JSP
            request.getRequestDispatcher(pagina).forward(request, response);

        } catch (NotFoundException nfe) {
            exibirErro(request, response, "Estoque não cadastrado.", ERROR_PAGE);
        } catch (NumberFormatException nmfe) {
            exibirErro(request, response, "Código do estoque inválido.", ERROR_PAGE);
        } catch (ConnectionFailedException cfe) {
            cfe.printStackTrace();
            exibirErro(request, response, cfe, ERROR_PAGE);
        } catch (Exception e) {
            e.printStackTrace();
            exibirErro(request, response, "Erro inesperado encontrado", ERROR_PAGE);
        }
    }
}
