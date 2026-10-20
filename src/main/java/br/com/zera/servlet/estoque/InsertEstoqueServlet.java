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
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static br.com.zera.exception.ErroServlet.exibirErro;
import static br.com.zera.regex.Constants.ERROR_PAGE;

/**
 * Servlet responsável por cadastrar um estoque.
 *
 * O admin escolhe a unidade (codUnidade). O gestor só cadastra estoque
 * na própria unidade. Nos dois casos o responsável (codResponsavel)
 * precisa ser um gestor da mesma unidade do estoque; se o gestor logado não
 * informar, ele mesmo vira o responsável.
 *
 * @author Pedro Rufino
 * @since 2026-10-01
 */
@WebServlet(name = "InsertEstoque", value = "/estoque/inserir")
public class InsertEstoqueServlet extends HttpServlet {

    /**
     * Processa POST para inserir um novo estoque.
     *
     * @param request objeto HttpServletRequest com espacoNaUnidade, codResponsavel e, para o admin, codUnidade
     * @param response objeto HttpServletResponse para redirecionamento ou erro
     * @throws IOException caso haja um erro de input/output (entrada/saída)
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        request.setCharacterEncoding("UTF-8");

        try {
            // descobre quem está logado: admin ou gestor
            Admin admin = Sessao.admin(request);
            Gestor gestorLogado = Sessao.gestor(request);
            Unidade unidadeLogada = Sessao.unidade(request);

            // lê os campos do formulário
            String codResponsavelTexto = request.getParameter("codResponsavel");
            Integer codResponsavel = null;
            if (codResponsavelTexto != null && !codResponsavelTexto.isBlank()) {
                codResponsavel = Integer.parseInt(codResponsavelTexto.trim());
            }
            int codUnidade;
            if (admin != null) {
                String codUnidadeParamTexto = request.getParameter("codUnidade");
                Integer codUnidadeParam = null;
                if (codUnidadeParamTexto != null && !codUnidadeParamTexto.isBlank()) {
                    codUnidadeParam = Integer.parseInt(codUnidadeParamTexto.trim());
                }
                if (codUnidadeParam == null || codResponsavel == null) {
                    exibirErro(request, response, "Selecione a unidade e o responsável.", ERROR_PAGE);
                    return;
                }
                new UnidadeDAO().findByCodigo(codUnidadeParam);
                codUnidade = codUnidadeParam;
            } else if (unidadeLogada != null && gestorLogado != null) {
                codUnidade = unidadeLogada.getCodigo();
                if (codResponsavel == null) {
                    codResponsavel = gestorLogado.getCodigo();
                }
            } else {
                exibirErro(request, response, "Sessão expirada. Faça login novamente.", ERROR_PAGE);
                return;
            }

            String espacoNaUnidadeTexto = request.getParameter("espacoNaUnidade");
            String espacoNaUnidade = null;
            if (espacoNaUnidadeTexto != null && !espacoNaUnidadeTexto.isBlank()) {
                espacoNaUnidade = espacoNaUnidadeTexto.trim();
            }
            if (espacoNaUnidade == null) {
                exibirErro(request, response, "Informe o espaço do estoque na unidade.", ERROR_PAGE);
                return;
            }

            // o responsável tem que ser da mesma unidade do estoque
            Gestor responsavel = new GestorDAO().findByCodigo(codResponsavel);
            if (responsavel.getCodUnidade() != codUnidade) {
                exibirErro(request, response, "O responsável precisa ser um gestor da mesma unidade.", ERROR_PAGE);
                return;
            }

            new EstoqueDAO().insert(new Estoque(espacoNaUnidade, codResponsavel, codUnidade));

            String destino;
            if (admin != null) {
                destino = "/estoque/lista";
            } else {
                destino = "/estoque/perfil";
            }
            // redirect
            response.sendRedirect(request.getContextPath() + destino);

        } catch (NotFoundException nfe) {
            nfe.printStackTrace();
            exibirErro(request, response, nfe, ERROR_PAGE);
        } catch (NumberFormatException nmfe) {
            exibirErro(request, response, "Unidade ou responsável inválidos.", ERROR_PAGE);
        } catch (ConnectionFailedException cfe) {
            cfe.printStackTrace();
            exibirErro(request, response, cfe, ERROR_PAGE);
        } catch (Exception e) {
            e.printStackTrace();
            exibirErro(request, response, e, ERROR_PAGE);
        }
    }
}
