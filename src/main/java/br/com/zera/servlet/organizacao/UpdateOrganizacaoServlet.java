package br.com.zera.servlet.organizacao;

import br.com.zera.dao.OrganizacaoDAO;
import br.com.zera.exception.ConnectionFailedException;
import br.com.zera.exception.NotFoundException;
import br.com.zera.model.Admin;
import br.com.zera.model.Organizacao;
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

/**
 * Servlet responsável por atualizar o cadastro de organizações.
 *
 * O admin altera a organização escolhida na lista (código por parâmetro);
 * o gestor altera a organização da própria unidade.
 *
 * @author Mayte B
 * @since 2026-10-06
 */
@WebServlet(name = "UpdateOrganizacao", value = "/organizacao/alterar")
public class UpdateOrganizacaoServlet extends HttpServlet {

    /**
     * Processa POST para atualizar a organização.
     *
     * @param request objeto HttpServletRequest com nome, email, cnpj e, para o admin, codigo
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

            // admin informa a organização. Gestor só altera a da própria unidade
            int codigo;
            if (admin != null) {
                // o admin informa qual organização vai alterar
                String codigoParamTexto = request.getParameter("codigo");
                Integer codigoParam = null;
                if (codigoParamTexto != null && !codigoParamTexto.isBlank()) {
                    codigoParam = Integer.parseInt(codigoParamTexto.trim());
                }
                if (codigoParam == null) {
                    exibirErro(request, response, "Organização não informada.", ERROR_PAGE);
                    return;
                }
                codigo = codigoParam;
            } else if (unidadeLogada != null) {
                codigo = unidadeLogada.getCodOrganizacao();
            } else {
                exibirErro(request, response, "Sessão expirada. Faça login novamente.", ERROR_PAGE);
                return;
            }

            // lê os campos do formulário
            String nomeTexto = request.getParameter("nome");
            String nome = null;
            if (nomeTexto != null && !nomeTexto.isBlank()) {
                nome = nomeTexto.trim();
            }
            String emailTexto = request.getParameter("email");
            String email = null;
            if (emailTexto != null && !emailTexto.isBlank()) {
                email = emailTexto.trim();
            }
            String cnpjTexto = request.getParameter("cnpj");
            String cnpj = null;
            if (cnpjTexto != null && !cnpjTexto.isBlank()) {
                cnpj = cnpjTexto.trim();
            }

            if (nome == null) {
                exibirErro(request, response, "Informe o nome da organização.", ERROR_PAGE);
                return;
            }
            if (email == null || !Regex.validarEmail(email)) {
                exibirErro(request, response, "Email inválido", ERROR_PAGE);
                return;
            }
            if (cnpj == null || !Regex.validarCNPJ(cnpj)) {
                exibirErro(request, response, "CNPJ inválido", ERROR_PAGE);
                return;
            }

            OrganizacaoDAO dao = new OrganizacaoDAO();
            // confere se existe antes de alterar
            dao.findByCodigo(codigo);
            dao.update(new Organizacao(codigo, cnpj, nome, email));

            String destino;
            if (admin != null) {
                destino = "/organizacao/lista";
            } else {
                destino = "/organizacao/perfil";
            }
            // redirect
            response.sendRedirect(request.getContextPath() + destino);

        } catch (NotFoundException nfe) {
            nfe.printStackTrace();
            exibirErro(request, response, nfe, ERROR_PAGE);
        } catch (NumberFormatException nmfe) {
            exibirErro(request, response, "Código da organização inválido.", ERROR_PAGE);
        } catch (ConnectionFailedException cfe) {
            cfe.printStackTrace();
            exibirErro(request, response, cfe, ERROR_PAGE);
        } catch (Exception e) {
            e.printStackTrace();
            exibirErro(request, response, e, ERROR_PAGE);
        }
    }
}
