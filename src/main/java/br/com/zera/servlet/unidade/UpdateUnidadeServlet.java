package br.com.zera.servlet.unidade;

import br.com.zera.dao.OrganizacaoDAO;
import br.com.zera.dao.UnidadeDAO;
import br.com.zera.exception.ConnectionFailedException;
import br.com.zera.exception.NotFoundException;
import br.com.zera.model.Admin;
import br.com.zera.model.Unidade;
import br.com.zera.regex.Regex;
import br.com.zera.util.Sessao;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static br.com.zera.exception.ErroServlet.exibirErro;
import static br.com.zera.regex.Constants.ERROR_PAGE;
import static br.com.zera.regex.Constants.SESSAO_UNIDADE;

/**
 * Servlet responsável por atualizar o cadastro de unidades.
 *
 * O admin altera a unidade escolhida na lista (código por parâmetro) e pode
 * trocar a organização dela. O gestor altera só a própria unidade e não troca
 * de organização.
 *
 * @author Mayte B
 * @since 2026-10-06
 */
@WebServlet(name = "UpdateUnidade", value = "/unidade/alterar")
public class UpdateUnidadeServlet extends HttpServlet {

    /**
     * Processa POST para atualizar a unidade.
     *
     * @param request objeto HttpServletRequest com cnpj, email e, para o admin, codigo e codOrganizacao
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
            UnidadeDAO dao = new UnidadeDAO();

            // busca a unidade atual
            Unidade existente;
            int codOrganizacao;
            if (admin != null) {
                // lê o código
                String codigoTexto = request.getParameter("codigo");
                Integer codigo = null;
                if (codigoTexto != null && !codigoTexto.isBlank()) {
                    codigo = Integer.parseInt(codigoTexto.trim());
                }
                if (codigo == null) {
                    exibirErro(request, response, "Unidade não informada.", ERROR_PAGE);
                    return;
                }
                existente = dao.findByCodigo(codigo);

                // o admin pode trocar a organização. Campo vazio mantém a atual
                String novaOrganizacaoTexto = request.getParameter("codOrganizacao");
                Integer novaOrganizacao = null;
                if (novaOrganizacaoTexto != null && !novaOrganizacaoTexto.isBlank()) {
                    novaOrganizacao = Integer.parseInt(novaOrganizacaoTexto.trim());
                }
                if (novaOrganizacao == null) {
                    codOrganizacao = existente.getCodOrganizacao();
                } else {
                    new OrganizacaoDAO().findByCodigo(novaOrganizacao);
                    codOrganizacao = novaOrganizacao;
                }
            } else if (unidadeLogada != null) {
                existente = dao.findByCodigo(unidadeLogada.getCodigo());
                // o gestor não pode trocar a organização
                codOrganizacao = existente.getCodOrganizacao();
            } else {
                exibirErro(request, response, "Sessão expirada. Faça login novamente.", ERROR_PAGE);
                return;
            }

            // lê os campos do formulário
            String cnpjTexto = request.getParameter("cnpj");
            String cnpj = null;
            if (cnpjTexto != null && !cnpjTexto.isBlank()) {
                cnpj = cnpjTexto.trim();
            }
            String emailTexto = request.getParameter("email");
            String email = null;
            if (emailTexto != null && !emailTexto.isBlank()) {
                email = emailTexto.trim();
            }

            if (cnpj == null || !Regex.validarCNPJ(cnpj)) {
                exibirErro(request, response, "CNPJ inválido", ERROR_PAGE);
                return;
            }
            if (email == null || !Regex.validarEmail(email)) {
                exibirErro(request, response, "Email inválido", ERROR_PAGE);
                return;
            }

            Unidade atualizada = new Unidade(existente.getCodigo(), cnpj, email, codOrganizacao);
            dao.update(atualizada);

            if (admin != null) {
                // redirect
                response.sendRedirect(request.getContextPath() + "/unidade/lista");
            } else {
                // mantém a sessão do gestor com os dados novos da unidade
                request.getSession().setAttribute(SESSAO_UNIDADE, atualizada);
                response.sendRedirect(request.getContextPath() + "/unidade/perfil");
            }

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
