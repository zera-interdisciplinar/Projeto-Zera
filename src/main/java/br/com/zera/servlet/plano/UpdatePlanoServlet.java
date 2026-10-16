package br.com.zera.servlet.plano;

import br.com.zera.dao.PlanoDAO;
import br.com.zera.exception.ZeraException;
import br.com.zera.model.Plano;
import br.com.zera.model.Unidade;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;

import static br.com.zera.exception.ErroServlet.exibirErro;
import static br.com.zera.regex.Constants.ERROR_PAGE;
import static br.com.zera.regex.Constants.SESSAO_UNIDADE;

/**
 * Servlet responsável por atualizar o cadastro de planos
 *
 * Esta classe recebe os dados enviados via formulário HTTP (código, nome, valor e tempo),
 * realiza a validação das informações e delega a atualização ao {@link PlanoDAO}.
 *
 * @author Pedro Rufino
 * @since 2026-10-07
 */
@WebServlet(name = "UpdatePlano", value = "/plano/alterarPlano")
public class UpdatePlanoServlet extends HttpServlet {

    /**
     * Processa POST para atualizar um Plano já cadastrado.
     *
     * <p>Em caso de sucesso, redireciona para a página de planos.
     * Em caso de falha (sessão expirada, dado inválido, plano inexistente ou erro de banco),
     * encaminha para a página de erro com uma mensagem explicativa.</p>
     *
     * @param request objeto HttpServletRequest contendo os parâmetros do formulário
     * @param response objeto HttpServletResponse para redirecionamento ou forward
     * @throws IOException caso haja um erro de entrada/saída
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        // garante que caracteres especiais cheguem corretos
        request.setCharacterEncoding("UTF-8");
        PlanoDAO dao = new PlanoDAO();

        try {
            // Recupera a unidade guardada na sessão durante o login
            HttpSession session = request.getSession();
            Unidade unidadeLogada = (Unidade) session.getAttribute(SESSAO_UNIDADE);
            if (unidadeLogada == null) {
                exibirErro(request, response, "Sessão expirada. Faça login novamente.", ERROR_PAGE);
                return;
            }

            // Identifica qual plano alterar
            String cod = request.getParameter("codigo");
            if (cod == null || cod.trim().isEmpty()) {
                exibirErro(request, response, "Código do plano não informado.", ERROR_PAGE);
                return;
            }
            int codigo = Integer.parseInt(cod.trim()); // NumberFormatException tratada abaixo

            // Novos dados do plano vindos do formulário
            String nome = request.getParameter("nome");
            String valorParam = request.getParameter("valor");
            String tempoParam = request.getParameter("tempo");

            if (nome == null || nome.trim().isEmpty()) {
                exibirErro(request, response, "Nome do plano não informado.", ERROR_PAGE);
                return;
            }
            if (valorParam == null || valorParam.trim().isEmpty()) {
                exibirErro(request, response, "Valor do plano não informado.", ERROR_PAGE);
                return;
            }
            if (tempoParam == null || tempoParam.trim().isEmpty()) {
                exibirErro(request, response, "Duração do plano não informada.", ERROR_PAGE);
                return;
            }

            BigDecimal valor;
            try {
                valor = new BigDecimal(valorParam.trim().replace(",", "."));
            } catch (NumberFormatException nfe) {
                exibirErro(request, response, "Valor do plano inválido.", ERROR_PAGE);
                return;
            }
            if (valor.compareTo(BigDecimal.ZERO) <= 0) {
                exibirErro(request, response, "O valor do plano deve ser maior que zero.", ERROR_PAGE);
                return;
            }

            int tempoMeses;
            try {
                tempoMeses = Integer.parseInt(tempoParam.trim());
            } catch (NumberFormatException nfe) {
                exibirErro(request, response, "A duração deve ser um número inteiro de meses.", ERROR_PAGE);
                return;
            }
            if (tempoMeses <= 0) {
                exibirErro(request, response, "A duração do plano deve ser de pelo menos 1 mês.", ERROR_PAGE);
                return;
            }

            // Busca o plano atual
            Plano plano = dao.findByCodigo(codigo);
            if (plano == null) {
                exibirErro(request, response, "Plano não encontrado.", ERROR_PAGE);
                return;
            }

            // Atualiza os campos do plano com os dados validados
            plano.setNome(nome.trim());
            plano.setValor(valor);
            plano.setTempo(String.valueOf(tempoMeses));
            // Atualiza as alterações no banco
            dao.update(plano);

            // Redireciona para a lista de planos após a alteração bem-sucedida
            response.sendRedirect(request.getContextPath() + "/plano/perfil");

        } catch (NumberFormatException nfe) {
            exibirErro(request, response, "Código do plano inválido.", ERROR_PAGE);
        } catch (ZeraException ze) {
            ze.printStackTrace();
            exibirErro(request, response, ze, ERROR_PAGE);
        } catch (Exception e) {
            e.printStackTrace();
            exibirErro(request, response, e, ERROR_PAGE);
        }
    }
}