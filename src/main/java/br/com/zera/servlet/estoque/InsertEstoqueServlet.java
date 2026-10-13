package br.com.zera.servlet.estoque;

import br.com.zera.dao.UnidadeDAO;
import br.com.zera.dao.GestorDAO;
import br.com.zera.exception.ConnectionFailedException;
import br.com.zera.exception.NotFoundException;
import br.com.zera.model.*;

import br.com.zera.dao.EstoqueDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

import static br.com.zera.exception.ErroServlet.exibirErro;
import static br.com.zera.regex.Constants.ERROR_PAGE;
import static br.com.zera.regex.Constants.SESSAO_GESTOR;

/**
 * Servlet responsável por processar o cadastro de estoques
 *
 * Esta classe recebe os dados enviados via formulário HTTP, realiza a
 * validação das informações e delega a persistência ao {@link EstoqueDAO}. *
 *
 * @author Pedro Rufino
 * @since 2026-10-01
 */
@WebServlet(name = "InsertEstoque", value = "/estoque/InserirEstoque")
public class InsertEstoqueServlet extends HttpServlet{

    /**
     * Processa POST para inserir um novo Estoque
     *
     * @param request objeto HttpServletRequest contendo os parâmetros da página
     * @param response objeto HttpResponse para redirecionamento ou foward
     * @throws java.io.IOException caso haja um erro de input/output (entrada/saída)
     * */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        // garante que caracteres especiais cheguem corretos
        request.setCharacterEncoding("UTF-8");
        EstoqueDAO dao = new EstoqueDAO();
        // DAOs usados para conferir se as chaves estrangeiras existem no banco
        UnidadeDAO unidadeDAO = new UnidadeDAO();
        GestorDAO gestorDAO = new GestorDAO();

        try{
            // Recupera o gestor guardado na sessão durante o login
            HttpSession session = request.getSession();
            Gestor gestorLogado = (Gestor) session.getAttribute(SESSAO_GESTOR);
            if (gestorLogado == null) {
                exibirErro(request, response, "Sessão expirada. Faça login novamente.", ERROR_PAGE);
                return;
            }

            // Único dado que vem do formulário
            String espacoNaUnidade = request.getParameter("espacoNaUnidade");
            if (espacoNaUnidade == null || espacoNaUnidade.trim().isEmpty()) {
                exibirErro(request, response, "Espaço na unidade não informado.", ERROR_PAGE);
                return;
            }

            int codUnidade = gestorLogado.getCodUnidade();
            if(unidadeDAO.findByCodigo(codUnidade) == null) {
                exibirErro(request, response, "Unidade vinculada ao gestor não foi encontrada.", ERROR_PAGE);
                return;
            }

            // Confere se o gestor ainda existe
            int codResponsavel = gestorLogado.getCodigo();
            if(gestorDAO.findByCodigo(codResponsavel) == null) {
                exibirErro(request, response, "Gestor responsável não encontrado", ERROR_PAGE);
                return;
            }

            // Cria o objeto model
            Estoque model = new Estoque(espacoNaUnidade, codResponsavel, codUnidade);

            // Instancia as informações recebidas no Estoque com o método
            dao.insert(model);

            // Redireciona para o perfil após a inserção bem-sucedida
            response.sendRedirect(request.getContextPath() + "/estoque/perfil");
        }catch(ConnectionFailedException cfe){
            cfe.printStackTrace();
            exibirErro(request, response, cfe, ERROR_PAGE);
        }catch(NotFoundException nfe){
            nfe.printStackTrace();
            exibirErro(request, response, nfe, ERROR_PAGE);
        }catch (Exception e) {
            e.printStackTrace();
            exibirErro(request, response, e, ERROR_PAGE);
        }

    }
}
