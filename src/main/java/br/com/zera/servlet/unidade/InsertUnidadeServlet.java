package br.com.zera.servlet.unidade;

import br.com.zera.dao.OrganizacaoDAO;
import br.com.zera.dao.UnidadeDAO;
import br.com.zera.exception.ConnectionFailedException;
import br.com.zera.exception.NotFoundException;
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

/** Cadastra a unidade e encaminha para a segunda etapa (endereço). */
@WebServlet(name = "InsertUnidade", value = "/unidade/inserir")
public class InsertUnidadeServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        request.setCharacterEncoding("UTF-8");

        try {
            if (Sessao.admin(request) == null) {
                exibirErro(request, response, "Acesso permitido apenas ao administrador.", ERROR_PAGE);
                return;
            }

            String cnpjTexto = request.getParameter("cnpj");
            String cnpj = (cnpjTexto == null || cnpjTexto.isBlank()) ? null : cnpjTexto.trim();

            String emailTexto = request.getParameter("email");
            String email = (emailTexto == null || emailTexto.isBlank()) ? null : emailTexto.trim();

            String codOrganizacaoTexto = request.getParameter("codOrganizacao");
            Integer codOrganizacao = null;
            if (codOrganizacaoTexto != null && !codOrganizacaoTexto.isBlank()) {
                codOrganizacao = Integer.parseInt(codOrganizacaoTexto.trim());
            }

            if (codOrganizacao == null) {
                exibirErro(request, response, "Selecione a organização.", ERROR_PAGE);
                return;
            }
            if (cnpj == null || !Regex.validarCNPJ(cnpj)) {
                exibirErro(request, response, "CNPJ inválido.", ERROR_PAGE);
                return;
            }
            if (email == null || !Regex.validarEmail(email)) {
                exibirErro(request, response, "Email inválido.", ERROR_PAGE);
                return;
            }

            new OrganizacaoDAO().findByCodigo(codOrganizacao);
            UnidadeDAO dao = new UnidadeDAO();
            if (cnpjJaCadastrado(dao, cnpj)) {
                exibirErro(request, response, "Já existe uma unidade com esse CNPJ.", ERROR_PAGE);
                return;
            }

            dao.insert(new Unidade(cnpj, email, codOrganizacao));
            Unidade nova = dao.findByCnpj(cnpj);

            // Rota dedicada à etapa 2, servida pelo ReadEnderecoServlet.
            response.sendRedirect(request.getContextPath()
                    + "/unidade/etapa2?codUnidade=" + nova.getCodigo());

        } catch (NotFoundException nfe) {
            nfe.printStackTrace();
            exibirErro(request, response, nfe, ERROR_PAGE);
        } catch (NumberFormatException nmfe) {
            exibirErro(request, response, "Organização inválida.", ERROR_PAGE);
        } catch (ConnectionFailedException cfe) {
            cfe.printStackTrace();
            exibirErro(request, response, cfe, ERROR_PAGE);
        } catch (Exception e) {
            e.printStackTrace();
            exibirErro(request, response, e, ERROR_PAGE);
        }
    }

    private boolean cnpjJaCadastrado(UnidadeDAO dao, String cnpj) {
        try {
            dao.findByCnpj(cnpj);
            return true;
        } catch (NotFoundException nfe) {
            return false;
        }
    }
}
