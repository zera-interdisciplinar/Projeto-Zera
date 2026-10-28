package br.com.zera.servlet.endereco;

import br.com.zera.dao.EnderecoDAO;
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
 * Servlet responsável por atualizar um endereço.
 *
 * O admin altera o endereço escolhido (código por parâmetro). O gestor altera
 * só o endereço da própria unidade, buscado pelo código da unidade na sessão.
 * A unidade dona do endereço nunca muda por aqui.
 *
 * @author Mayte B
 * @since 2026-08-14
 */
@WebServlet(name = "UpdateEndereco", value = "/endereco/alterar")
public class UpdateEnderecoServlet extends HttpServlet {

    /**
     * Processa POST para atualizar o endereço.
     *
     * @param request objeto HttpServletRequest com logradouro, numero, cep, estado, cidade, bairro
     *                e, para o admin, codigo
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
            EnderecoDAO dao = new EnderecoDAO();

            // busca o endereço atual
            Endereco existente;
            if (admin != null) {
                // lê o código
                String codigoTexto = request.getParameter("codigo");
                Integer codigo = null;
                if (codigoTexto != null && !codigoTexto.isBlank()) {
                    codigo = Integer.parseInt(codigoTexto.trim());
                }
                if (codigo == null) {
                    exibirErro(request, response, "Endereço não informado.", ERROR_PAGE);
                    return;
                }
                existente = dao.findByCodigo(codigo);
            } else if (unidadeLogada != null) {
                existente = dao.findByCodUnidade(unidadeLogada.getCodigo());
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
            // aceita o CEP com ou sem hífen e guarda sempre no formato 00000-000
            if (cep != null) {
                String cepNumeros = cep.replaceAll("\\D", "");
                if (cepNumeros.length() == 8) {
                    cep = cepNumeros.substring(0, 5) + "-" + cepNumeros.substring(5);
                }
            }
            if (cep == null || !Regex.validarCEP(cep)) {
                exibirErro(request, response, "CEP inválido, ele precisa conter 8 números.", ERROR_PAGE);
                return;
            }

            // codigo e codUnidade vêm do banco
            dao.update(new Endereco(existente.getCodigo(), bairro, numero, cep, logradouro, cidade, estado.toUpperCase(), existente.getCodUnidade()));

            String destino;
            if (admin != null) {
                destino = "/endereco/lista";
            } else {
                destino = "/endereco/perfil";
            }
            // redirect
            response.sendRedirect(request.getContextPath() + destino);

        } catch (NotFoundException nfe) {
            nfe.printStackTrace();
            exibirErro(request, response, nfe, ERROR_PAGE);
        } catch (NumberFormatException nmfe) {
            exibirErro(request, response, "Número ou código inválidos.", ERROR_PAGE);
        } catch (ConnectionFailedException cfe) {
            cfe.printStackTrace();
            exibirErro(request, response, cfe, ERROR_PAGE);
        } catch (Exception e) {
            e.printStackTrace();
            exibirErro(request, response, e, ERROR_PAGE);
        }
    }
}