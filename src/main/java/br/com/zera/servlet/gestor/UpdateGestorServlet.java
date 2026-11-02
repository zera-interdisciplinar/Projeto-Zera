package br.com.zera.servlet.gestor;

import br.com.zera.config.HashSenha;
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
     * Processa a atualização dos dados de um gestor.
     *
     * @param request requisição contendo os dados do formulário
     * @param response resposta utilizada para redirecionamento ou erro
     * @throws IOException caso ocorra falha de entrada ou saída
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        request.setCharacterEncoding("UTF-8");

        try {
            Admin admin = Sessao.admin(request);
            Gestor gestorLogado = Sessao.gestor(request);
            GestorDAO dao = new GestorDAO();

            Gestor existente;
            int codUnidade;

            if (admin != null) {
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

            String nomeTexto = request.getParameter("nome");
            String emailTexto = request.getParameter("email");
            String telefoneTexto = request.getParameter("telefone");

            String nome = nomeTexto == null ? null : nomeTexto.trim();
            String email = emailTexto == null ? null : emailTexto.trim();
            String telefone = telefoneTexto == null
                    ? null : telefoneTexto.trim().replaceAll("\\D", "");

            if (nome == null || !Regex.validarNome(nome)) {
                exibirErro(request, response, "Informe o nome completo.", ERROR_PAGE);
                return;
            }

            if (email == null || !Regex.validarEmail(email)) {
                exibirErro(request, response,
                        "Email inválido, ele precisa conter @ e o domínio.", ERROR_PAGE);
                return;
            }

            if (telefone == null || !Regex.validarTel(telefone)) {
                exibirErro(request, response,
                        "Telefone inválido, ele precisa conter o DDD + nove números.", ERROR_PAGE);
                return;
            }

            if (!email.equalsIgnoreCase(existente.getEmail())
                    && emailJaCadastrado(dao, email)) {
                exibirErro(request, response,
                        "Já existe um gestor com esse e-mail.", ERROR_PAGE);
                return;
            }

            String senha = existente.getSenha();

            if (admin != null) {
                String novaSenha = request.getParameter("senha");
                String confirmacao = request.getParameter("confirmacao");

                boolean senhaInformada = novaSenha != null && !novaSenha.isEmpty();
                boolean confirmacaoInformada = confirmacao != null && !confirmacao.isEmpty();

                if (senhaInformada || confirmacaoInformada) {
                    if (!senhaInformada || !confirmacaoInformada) {
                        exibirErro(request, response,
                                "Preencha a nova senha e a confirmação.", ERROR_PAGE);
                        return;
                    }

                    if (!novaSenha.equals(confirmacao)) {
                        exibirErro(request, response,
                                "A confirmação está diferente da senha informada.", ERROR_PAGE);
                        return;
                    }

                    if (!Regex.validarSenha(novaSenha)) {
                        exibirErro(request, response,
                                "A senha precisa ter pelo menos 8 caracteres, um número e um caractere especial.",
                                ERROR_PAGE);
                        return;
                    }

                    senha = HashSenha.gerarHash(novaSenha);
                }
            }

            Gestor atualizado = new Gestor(
                    existente.getCodigo(),
                    nome,
                    email,
                    senha,
                    telefone,
                    codUnidade
            );

            dao.update(atualizado);

            if (gestorLogado != null
                    && gestorLogado.getCodigo() == atualizado.getCodigo()) {
                request.getSession().setAttribute(SESSAO_GESTOR, atualizado);
            }

            String destino = admin != null ? "/gestor/lista" : "/gestor/perfil";
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
     * Verifica se o e-mail já está cadastrado para outro gestor.
     *
     * @param dao DAO utilizado para consultar os gestores
     * @param email e-mail que será verificado
     * @return true quando o e-mail já está cadastrado
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