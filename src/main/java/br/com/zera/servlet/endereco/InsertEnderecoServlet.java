package br.com.zera.servlet.endereco;

import br.com.zera.dao.EnderecoDAO;
import br.com.zera.dao.UnidadeDAO;
import br.com.zera.exception.ConnectionFailedException;
import br.com.zera.exception.NotFoundException;
import br.com.zera.model.Admin;
import br.com.zera.model.Endereco;
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
 * Servlet responsável por cadastrar o endereço de uma unidade.
 *
 * O admin informa a unidade pelo parâmetro codUnidade (parte 2 do
 * "Nova unidade"). O gestor só cadastra o endereço da própria unidade, e o
 * código dela vem da sessão. Cada unidade tem um único endereço.
 *
 * @author Mayte B
 * @since 2026-08-14
 */
@WebServlet(name = "InsertEndereco", value = "/endereco/inserir")
public class InsertEnderecoServlet extends HttpServlet {

    /**
     * Processa POST para inserir um novo endereço.
     *
     * @param request objeto HttpServletRequest com logradouro, numero, cep, estado, cidade, bairro
     *                e, para o admin, codUnidade
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

            int codUnidade;
            if (admin != null) {
                // o admin escolhe a unidade. O gestor usa a própria
                String codUnidadeParamTexto = request.getParameter("codUnidade");
                Integer codUnidadeParam = null;
                if (codUnidadeParamTexto != null && !codUnidadeParamTexto.isBlank()) {
                    codUnidadeParam = Integer.parseInt(codUnidadeParamTexto.trim());
                }
                if (codUnidadeParam == null) {
                    exibirErro(request, response, "Selecione a unidade.", ERROR_PAGE);
                    return;
                }
                // confere se a unidade existe
                new UnidadeDAO().findByCodigo(codUnidadeParam);
                codUnidade = codUnidadeParam;
            } else if (unidadeLogada != null) {
                codUnidade = unidadeLogada.getCodigo();
            } else {
                exibirErro(request, response, "Sessão expirada. Faça login novamente.", ERROR_PAGE);
                return;
            }

            // lê os campos do formulário
            String logradouroTexto = request.getParameter("logradouro");
            String logradouro = null;
            if (logradouroTexto != null && !logradouroTexto.isBlank()) {
                logradouro = logradouroTexto.trim();
            }
            String cepTexto = request.getParameter("cep");
            String cep = null;
            if (cepTexto != null && !cepTexto.isBlank()) {
                cep = cepTexto.trim();
            }
            String estadoTexto = request.getParameter("estado");
            String estado = null;
            if (estadoTexto != null && !estadoTexto.isBlank()) {
                estado = estadoTexto.trim();
            }
            String cidadeTexto = request.getParameter("cidade");
            String cidade = null;
            if (cidadeTexto != null && !cidadeTexto.isBlank()) {
                cidade = cidadeTexto.trim();
            }
            String bairroTexto = request.getParameter("bairro");
            String bairro = null;
            if (bairroTexto != null && !bairroTexto.isBlank()) {
                bairro = bairroTexto.trim();
            }
            String numeroTexto = request.getParameter("numero");
            Integer numero = null;
            if (numeroTexto != null && !numeroTexto.isBlank()) {
                numero = Integer.parseInt(numeroTexto.trim());
            }

            if (logradouro == null || cidade == null || bairro == null || estado == null || numero == null) {
                exibirErro(request, response, "Preencha todos os campos do endereço.", ERROR_PAGE);
                return;
            }
            if (cep == null || !Regex.validarCEP(cep)) {
                exibirErro(request, response, "CEP inválido, ele precisa conter 8 números.", ERROR_PAGE);
                return;
            }

            EnderecoDAO dao = new EnderecoDAO();
            // cada unidade tem um único endereço
            if (unidadeJaTemEndereco(dao, codUnidade)) {
                exibirErro(request, response, "Essa unidade já possui endereço. Use a opção de editar.", ERROR_PAGE);
                return;
            }

            dao.insert(new Endereco(bairro, numero, cep, logradouro, cidade, estado.toUpperCase(), codUnidade));

            String destino;
            if (admin != null) {
                destino = "/unidade/lista";
            } else {
                destino = "/endereco/perfil";
            }
            // redirect
            response.sendRedirect(request.getContextPath() + destino);

        } catch (NotFoundException nfe) {
            nfe.printStackTrace();
            exibirErro(request, response, nfe, ERROR_PAGE);
        } catch (NumberFormatException nmfe) {
            exibirErro(request, response, "Número ou unidade inválidos.", ERROR_PAGE);
        } catch (ConnectionFailedException cfe) {
            cfe.printStackTrace();
            exibirErro(request, response, cfe, ERROR_PAGE);
        } catch (Exception e) {
            e.printStackTrace();
            exibirErro(request, response, e, ERROR_PAGE);
        }
    }

    /**
     * Verifica se a unidade já possui endereço cadastrado.
     *
     * @param dao DAO de endereço
     * @param codUnidade código da unidade
     * @return true se já existir endereço para a unidade
     */
    private boolean unidadeJaTemEndereco(EnderecoDAO dao, int codUnidade) {
        try {
            dao.findByCodUnidade(codUnidade);
            return true;
        } catch (NotFoundException nfe) {
            return false;
        }
    }
}
