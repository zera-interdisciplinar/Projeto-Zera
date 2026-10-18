package br.com.zera.servlet.gestor;

import br.com.zera.dao.GestorDAO;
import br.com.zera.dao.UnidadeDAO;
import br.com.zera.exception.ConnectionFailedException;
import br.com.zera.exception.NotFoundException;
import br.com.zera.model.Admin;
import br.com.zera.model.Gestor;
import br.com.zera.regex.Regex;
import br.com.zera.util.Sessao;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static br.com.zera.exception.ErroServlet.exibirErro;
import static br.com.zera.regex.Constants.ERROR_PAGE;
import static br.com.zera.regex.Constants.SESSAO_GESTOR;

/**
 * Servlet responsável por atualizar os dados de um gestor (nome, e-mail e telefone).
 *
 * O admin altera o gestor escolhido (código por parâmetro) e pode trocá-lo de
 * unidade. O gestor altera só o próprio cadastro. A senha não muda por aqui:
 * ela é mantida a do banco e trocada pelo {@link UpdateSenhaGestorServlet}.
 *
 * @author Mayte B
 * @since 2026-10-06
 */
@WebServlet(name = "UpdateGestor", value = "/gestor/alterar")
public class UpdateGestorServlet extends HttpServlet {

    /**
     * Processa POST para atualizar o gestor.
     *
     * @param request objeto HttpServletRequest com nome, email, telefone e, para o admin, codigo e codUnidade
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
            Gestor gestorLogado = Sessao.gestor(request);
            GestorDAO dao = new GestorDAO();

            // busca o gestor atual
            Gestor existente;
            int codUnidade;
            if (admin != null) {
                // lê o código
                String codigoTexto = request.getParameter("codigo");
                Integer codigo = null;
                if (codigoTexto != null && !codigoTexto.isBlank()) {
                    codigo = Integer.parseInt(codigoTexto.trim());
                }
                if (codigo == null) {
                    exibirErro(request, response, "Gestor não informado.", ERROR_PAGE);
                    return;
                }
                existente = dao.findByCodigo(codigo);

                // o admin pode trocar a unidade. Campo vazio mantém a atual
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
            } else if (gestorLogado != null) {
                existente = dao.findByCodigo(gestorLogado.getCodigo());
                codUnidade = existente.getCodUnidade();
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
            String telefoneTexto = request.getParameter("telefone");
            String telefone = null;
            if (telefoneTexto != null && !telefoneTexto.isBlank()) {
                telefone = telefoneTexto.trim();
            }

            if (nome == null || !Regex.validarNome(nome)) {
                exibirErro(request, response, "Informe o nome completo.", ERROR_PAGE);
                return;
            }
            if (email == null || !Regex.validarEmail(email)) {
                exibirErro(request, response, "Email inválido, ele precisa conter @ e o domínio.", ERROR_PAGE);
                return;
            }
            if (telefone == null || !Regex.validarTel(telefone)) {
                exibirErro(request, response, "Telefone inválido, ele precisa conter o DDD + nove números.", ERROR_PAGE);
                return;
            }
            // se o e-mail mudou, confere se já existe
            if (!email.equalsIgnoreCase(existente.getEmail()) && emailJaCadastrado(dao, email)) {
                exibirErro(request, response, "Já existe um gestor com esse e-mail.", ERROR_PAGE);
                return;
            }

            // mantém a senha do banco para o update não apagar
            Gestor atualizado = new Gestor(existente.getCodigo(), nome, email, existente.getSenha(), telefone, codUnidade);
            dao.update(atualizado);

            // se o gestor editou ele mesmo, atualiza a sessão
            if (gestorLogado != null && gestorLogado.getCodigo() == atualizado.getCodigo()) {
                request.getSession().setAttribute(SESSAO_GESTOR, atualizado);
            }

            String destino;
            if (admin != null) {
                destino = "/gestor/lista";
            } else {
                destino = "/gestor/perfil";
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

    /**
     * Verifica se já existe um gestor com o e-mail informado.
     *
     * @param dao DAO de gestor
     * @param email e-mail digitado
     * @return true se o e-mail já estiver cadastrado
     */
    private boolean emailJaCadastrado(GestorDAO dao, String email) {
        try {
            dao.findByEmail(email);
            return true;
        } catch (NotFoundException nfe) {
            return false;
        }
    }
}