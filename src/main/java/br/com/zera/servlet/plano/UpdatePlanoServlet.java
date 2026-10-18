package br.com.zera.servlet.plano;

import br.com.zera.dao.PlanoDAO;
import br.com.zera.exception.ConnectionFailedException;
import br.com.zera.exception.NotFoundException;
import br.com.zera.model.Plano;
import br.com.zera.util.Sessao;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;

import static br.com.zera.exception.ErroServlet.exibirErro;
import static br.com.zera.regex.Constants.ERROR_PAGE;

/**
 * Servlet responsável por atualizar um plano (apenas admin).
 *
 * @author Pedro Rufino
 * @since 2026-10-07
 */
@WebServlet(name = "UpdatePlano", value = "/plano/alterar")
public class UpdatePlanoServlet extends HttpServlet {

    /**
     * Processa POST para atualizar o plano.
     *
     * @param request objeto HttpServletRequest com codigo, nome, valor e tempo
     * @param response objeto HttpServletResponse para redirecionamento ou erro
     * @throws IOException caso haja um erro de input/output (entrada/saída)
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        request.setCharacterEncoding("UTF-8");

        try {
            // só o admin pode fazer isso
            if (Sessao.admin(request) == null) {
                exibirErro(request, response, "Acesso permitido apenas ao administrador.", ERROR_PAGE);
                return;
            }

            // lê o código
            String codigoTexto = request.getParameter("codigo");
            Integer codigo = null;
            if (codigoTexto != null && !codigoTexto.isBlank()) {
                codigo = Integer.parseInt(codigoTexto.trim());
            }
            if (codigo == null) {
                exibirErro(request, response, "Plano não informado.", ERROR_PAGE);
                return;
            }

            PlanoDAO dao = new PlanoDAO();
            // busca o plano atual
            Plano existente = dao.findByCodigo(codigo);

            // lê os campos do formulário
            String nomeTexto = request.getParameter("nome");
            String nome = null;
            if (nomeTexto != null && !nomeTexto.isBlank()) {
                nome = nomeTexto.trim();
            }
            String tempoTexto = request.getParameter("tempo");
            String tempo = null;
            if (tempoTexto != null && !tempoTexto.isBlank()) {
                tempo = tempoTexto.trim();
            }
            String valorTexto = request.getParameter("valor");
            BigDecimal valor = null;
            if (valorTexto != null && !valorTexto.isBlank()) {
                valor = new BigDecimal(valorTexto.replace("R$", "").trim().replace(",", "."));
            }

            if (nome == null) {
                exibirErro(request, response, "Nome do plano não informado.", ERROR_PAGE);
                return;
            }
            // BigDecimal usa compareTo para comparar valores
            if (valor == null || valor.compareTo(BigDecimal.ZERO) < 0) {
                exibirErro(request, response, "Informe um valor maior ou igual a zero.", ERROR_PAGE);
                return;
            }
            // usa a mesma regra do InsertPlanoServlet
            if (!InsertPlanoServlet.tempoValido(tempo)) {
                exibirErro(request, response, "Duração inválida. Use mensal, anual ou o número de meses.", ERROR_PAGE);
                return;
            }

            dao.update(new Plano(existente.getCodigo(), valor, nome, tempo));

            // redirect
            response.sendRedirect(request.getContextPath() + "/plano/lista");

        } catch (NotFoundException nfe) {
            nfe.printStackTrace();
            exibirErro(request, response, nfe, ERROR_PAGE);
        } catch (NumberFormatException nmfe) {
            exibirErro(request, response, "Código ou valor inválidos.", ERROR_PAGE);
        } catch (ConnectionFailedException cfe) {
            cfe.printStackTrace();
            exibirErro(request, response, cfe, ERROR_PAGE);
        } catch (Exception e) {
            e.printStackTrace();
            exibirErro(request, response, e, ERROR_PAGE);
        }
    }
}
