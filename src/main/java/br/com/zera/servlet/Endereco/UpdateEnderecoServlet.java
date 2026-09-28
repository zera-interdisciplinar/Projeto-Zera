package br.com.zera.servlet.Endereco;

import br.com.zera.dao.EnderecoDAO;
import br.com.zera.dao.UnidadeDAO;
import br.com.zera.exception.ConnectionFailedException;
import br.com.zera.exception.NotFoundException;
import br.com.zera.model.Endereco;
import br.com.zera.model.Gestor;
import br.com.zera.regex.Regex;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import static br.com.zera.exception.ErroServlet.exibirErro;
import static br.com.zera.regex.Constants.ERROR_PAGE;

    @WebServlet(name = "UpdateEndereco", value = "/endereco/alterarEndereco")

    /**
     * Servlet responsável por atualizar o cadastro de endereços
     *
     * Esta classe recebe os dados enviados via formulário HTTP, realiza a
     * validação das informações e delega as atualizações ao {@link EnderecoDAO}. *
     *
     * @author Mayte B
     * @since 2026-08-14
     */
public class UpdateEnderecoServlet extends HttpServlet {

    /**
     * Processa POST para atualizar o Endereço cadastrado baseado no antigo
     *
     * @param request  objeto HttpServletRequest contendo os parâmetros da página
     * @param response objeto HttpResponse para redirecionamento ou foward
     * @throws java.io.IOException caso haja um erro de input/output (entrada/saída)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        /**
         * Instancia DAO responsável por persistir objetos no banco de dados
         *
         * Instancia Chave estrangeira da tabela Unidade
         */
        EnderecoDAO dao = new EnderecoDAO();
        UnidadeDAO unidadeDAO = new UnidadeDAO();

        try {

            /**
             * Salva o login/sign in do {@link Gestor} para manter a sessão tiva
             */
            HttpSession session = request.getSession();
            Gestor gestorLogado = (Gestor) session.getAttribute("gestorLogado");
            if (gestorLogado == null) {
                exibirErro(request, response, "Sessão expirada. Faça login novamente.", ERROR_PAGE);
                return;
            }
            /**
             * Capta os atributos (colunas do banco {@link EnderecoDAO}) do endereço recebido
             *
             * @throws caso não tenha o código procurado
             */
            int codUnidade = gestorLogado.getCodUnidade();
            Endereco enderecoExistente = dao.findByCodigoUnidade(codUnidade);

            if (enderecoExistente == null) {
                exibirErro(request, response, "Endereço não encontrado.", ERROR_PAGE);
                return;
            }

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

            /**
             * Instancia página de erro caso o CPF inserido seja inválido
             */
            if (Regex.validarCEP(cep) == false) {
                exibirErro(request, response, "CEP inválido", ERROR_PAGE);
                return;
            }

            /**
             * Cria objeto {@link Endereco}
             */
            Endereco model = new Endereco(codigoEndereco, bairro, numero, cep, logradouro, cidade, estado, codUnidade);

            /**
             * Instancia as informações recebidas no {@link Endereco} com o método
             */
            dao.update(model);

            // Redireciona para a lista de endereços após a inserção bem-sucedida
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
