package br.com.zera.servlet.endereco;

import br.com.zera.dao.EnderecoDAO;
import br.com.zera.exception.ConnectionFailedException;
import br.com.zera.exception.NotFoundException;
import br.com.zera.model.Endereco;
import br.com.zera.model.Unidade;
import br.com.zera.regex.Regex;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import static br.com.zera.exception.ErroServlet.exibirErro;
import static br.com.zera.regex.Constants.ERROR_PAGE;

/**
 * Servlet responsável por atualizar o cadastro de endereços
 *
 * Esta classe recebe os dados enviados via formulário HTTP, realiza a
 * validação das informações e delega as atualizações ao {@link EnderecoDAO}.
 *
 * @author Mayte B
 * @since 2026-08-14
 */
@WebServlet(name = "UpdateEndereco", value = "/endereco/alterarEndereco")
public class UpdateEnderecoServlet extends HttpServlet {

    /**
     * Processa POST para atualizar o Endereço cadastrado baseado no antigo
     *
     * @param request  objeto HttpServletRequest contendo os parâmetros da página
     * @param response objeto HttpResponse para redirecionamento ou foward
     * @throws java.io.IOException caso haja um erro de input/output (entrada/saída)
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        try {

            // resgata a unidade logada na sessão
            HttpSession session = request.getSession();
            Unidade unidadeLogada = (Unidade) session.getAttribute("Unidade Logada");
            if (unidadeLogada == null) {
                exibirErro(request, response, "Sessão expirada. Faça login novamente.", ERROR_PAGE);
                return;
            }

            // busca o endereço atual pela unidade
            int codUnidade = unidadeLogada.getCodigo();
            Endereco enderecoExistente = EnderecoDAO.findByCodUnidade(codUnidade);

            int codigoEndereco = enderecoExistente.getCodigo();
            String bairro = request.getParameter("bairro");
            String num = request.getParameter("numero");
            if (num == null || num.isEmpty()) {
                exibirErro(request, response, "Número não informado.", ERROR_PAGE);
                return;
            }
            int numero = Integer.parseInt(num);
            String cep = request.getParameter("cep");
            String logradouro = request.getParameter("logradouro");
            String cidade = request.getParameter("cidade");
            String estado = request.getParameter("estado");

            // exibe página de erro caso o CEP inserido seja inválido
            if (Regex.validarCEP(cep) == false) {
                exibirErro(request, response, "CEP inválido", ERROR_PAGE);
                return;
            }

            // cria objeto Endereco
            Endereco model = new Endereco(codigoEndereco, bairro, numero, cep, logradouro, cidade, estado, codUnidade);

            // atualiza o endereço no banco
            EnderecoDAO.update(model);

            // Redireciona para o perfil do endereço após a atualização bem-sucedida
            response.sendRedirect(request.getContextPath() + "/endereco/perfil");

        } catch (ConnectionFailedException cfe) {
            cfe.printStackTrace();
            exibirErro(request, response, cfe, ERROR_PAGE);
        } catch (NotFoundException nfe) {
            nfe.printStackTrace();
            exibirErro(request, response, nfe, ERROR_PAGE);
        } catch (NumberFormatException nmfe) {
            nmfe.printStackTrace();
            exibirErro(request, response, nmfe, ERROR_PAGE);
        } catch (Exception e) {
            exibirErro(request, response, e, ERROR_PAGE);
        }
    }
}
