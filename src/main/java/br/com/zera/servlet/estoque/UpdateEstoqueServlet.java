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
 * Servlet responsável por atualizar um estoque.
 *
 * Os dois perfis mandam o codigo do estoque. O admin pode alterar
 * qualquer estoque e trocá-lo de unidade. O gestor só altera estoques da
 * própria unidade, e a unidade não muda. O responsável precisa ser um gestor
 * da mesma unidade do estoque.
 *
 * @author Pedro Rufino
 * @since 2026-10-04
 */
@WebServlet(name = "UpdateEstoque", value = "/estoque/alterar")
public class UpdateEstoqueServlet extends HttpServlet {

    /**
     * Processa POST para atualizar o estoque.
     *
     * @param request objeto HttpServletRequest com codigo, espacoNaUnidade, codResponsavel e, para o admin, codUnidade
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
            Unidade unidadeLogada = Sessao.unidade(request);
            if (admin == null && unidadeLogada == null) {
                exibirErro(request, response, "Sessão expirada. Faça login novamente.", ERROR_PAGE);
                return;
            }

            // lê o código
            String codigoTexto = request.getParameter("codigo");
            Integer codigo = null;
            if (codigoTexto != null && !codigoTexto.isBlank()) {
                codigo = Integer.parseInt(codigoTexto.trim());
            }
            if (codigo == null) {
                exibirErro(request, response, "Estoque não informado.", ERROR_PAGE);
                return;
            }

            EstoqueDAO dao = new EstoqueDAO();
            Estoque existente = dao.findByCodigo(codigo);

            int codUnidade;
            if (admin != null) {
                // o admin pode trocar a unidade do estoque. Campo vazio mantém a atual
                String novaUnidadeTexto = request.getParameter("codUnidade");
                Integer novaUnidade = null;
                if (novaUnidadeTexto != null && !novaUnidadeTexto.isBlank()) {
                    novaUnidade = Integer.parseInt(novaUnidadeTexto.trim());
                }
                if (novaUnidade == null) {
                    codUnidade = existente.getCodUnidade();
                } else {
                    new UnidadeDAO().findByCodigo(novaUnidade);
                    codUnidade = novaUnidade;
                }
            } else {
                // gestor não pode mexer em estoque de outra unidade
                if (existente.getCodUnidade() != unidadeLogada.getCodigo()) {
                    exibirErro(request, response, "Este estoque não pertence à sua unidade.", ERROR_PAGE);
                    return;
                }
                codUnidade = existente.getCodUnidade();
            }

            // lê os campos do formulário
            String espacoNaUnidadeTexto = request.getParameter("espacoNaUnidade");
            String espacoNaUnidade = null;
            if (espacoNaUnidadeTexto != null && !espacoNaUnidadeTexto.isBlank()) {
                espacoNaUnidade = espacoNaUnidadeTexto.trim();
            }
            String codResponsavelTexto = request.getParameter("codResponsavel");
            Integer codResponsavel = null;
            if (codResponsavelTexto != null && !codResponsavelTexto.isBlank()) {
                codResponsavel = Integer.parseInt(codResponsavelTexto.trim());
            }
            if (espacoNaUnidade == null) {
                exibirErro(request, response, "Informe o espaço do estoque na unidade.", ERROR_PAGE);
                return;
            }
            if (codResponsavel == null) {
                codResponsavel = existente.getCodResponsavel();
            }

            Gestor responsavel = new GestorDAO().findByCodigo(codResponsavel);
            if (responsavel.getCodUnidade() != codUnidade) {
                exibirErro(request, response, "O responsável precisa ser um gestor da mesma unidade.", ERROR_PAGE);
                return;
            }

            dao.update(new Estoque(existente.getCodigo(), espacoNaUnidade, codResponsavel, codUnidade));

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
            exibirErro(request, response, "Código inválido.", ERROR_PAGE);
        } catch (ConnectionFailedException cfe) {
            cfe.printStackTrace();
            exibirErro(request, response, cfe, ERROR_PAGE);
        } catch (Exception e) {
            e.printStackTrace();
            exibirErro(request, response, e, ERROR_PAGE);
        }
    }
}
