package br.com.zera.servlet.cadastro;

import br.com.zera.dao.AssinaturaDAO;
import br.com.zera.dao.EnderecoDAO;
import br.com.zera.dao.GestorDAO;
import br.com.zera.dao.OrganizacaoDAO;
import br.com.zera.dao.PlanoDAO;
import br.com.zera.dao.UnidadeDAO;
import br.com.zera.exception.ConnectionFailedException;
import br.com.zera.exception.NotFoundException;
import br.com.zera.model.Assinatura;
import br.com.zera.model.Endereco;
import br.com.zera.model.Gestor;
import br.com.zera.model.Organizacao;
import br.com.zera.model.Plano;
import br.com.zera.model.Unidade;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDate;

import static br.com.zera.exception.ErroServlet.exibirErro;
import static br.com.zera.regex.Constants.CADASTRO_ENDERECO;
import static br.com.zera.regex.Constants.CADASTRO_GESTOR;
import static br.com.zera.regex.Constants.CADASTRO_ORGANIZACAO;
import static br.com.zera.regex.Constants.CADASTRO_PLANO;
import static br.com.zera.regex.Constants.CADASTRO_UNIDADE;
import static br.com.zera.regex.Constants.ERROR_PAGE;
import static br.com.zera.regex.Constants.SESSAO_GESTOR;
import static br.com.zera.regex.Constants.SESSAO_UNIDADE;
import static br.com.zera.regex.Constants.STATUS_ATIVA;

/**
 * Último passo do cadastro: confirmação e gravação no banco.
 *
 * No GET mostra o resumo do que foi preenchido. No POST grava tudo na ordem
 * das chaves estrangeiras, já deixa o gestor logado e o leva para a home.
 *
 * @author Pedro Rufino
 * @since 2026-10-16
 */
@WebServlet(name = "ConfirmCadastro", value = "/cadastro/confirmar")
public class ConfirmCadastroServlet extends HttpServlet {

    /** Página com o resumo do cadastro */
    private static final String PAGINA = "/WEB-INF/cadastro/confirmacao.jsp";

    /** Primeiro passo, para quem chegar aqui sem ter passado pelos anteriores */
    private static final String INICIO = "/cadastro/gestor";

    /** Tela para onde o gestor vai depois de cadastrado */
    private static final String HOME_GESTOR = "/gestor/home";

    /**
     * Processa GET para exibir o resumo do cadastro antes de confirmar.
     *
     * @param request objeto HttpServletRequest que recebe os dados do resumo
     * @param response objeto HttpServletResponse usado para exibir a página
     * @throws ServletException caso ocorra erro ao encaminhar para a página
     * @throws IOException caso ocorra erro de entrada/saída ao responder
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        if (faltaPassoAnterior(session)) {
            response.sendRedirect(request.getContextPath() + INICIO);
            return;
        }

        try {
            request.setAttribute("gestor", session.getAttribute(CADASTRO_GESTOR));
            request.setAttribute("organizacao", session.getAttribute(CADASTRO_ORGANIZACAO));
            request.setAttribute("unidade", session.getAttribute(CADASTRO_UNIDADE));
            request.setAttribute("endereco", session.getAttribute(CADASTRO_ENDERECO));

            Integer codPlano = (Integer) session.getAttribute(CADASTRO_PLANO);
            if (codPlano != null) {
                request.setAttribute("plano", new PlanoDAO().findByCodigo(codPlano));
            }

            request.getRequestDispatcher(PAGINA).forward(request, response);

        } catch (NotFoundException nfe) {
            response.sendRedirect(request.getContextPath() + "/cadastro/plano");
        } catch (ConnectionFailedException cfe) {
            cfe.printStackTrace();
            exibirErro(request, response, cfe, ERROR_PAGE);
        }
    }

    /**
     * Processa POST para gravar o cadastro completo e já fazer o login do gestor.
     *
     * @param request objeto HttpServletRequest da requisição
     * @param response objeto HttpServletResponse para redirecionamento ou erro
     * @throws ServletException caso ocorra erro ao encaminhar para a página
     * @throws IOException caso ocorra erro de entrada/saída ao responder
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        if (faltaPassoAnterior(session)) {
            response.sendRedirect(request.getContextPath() + INICIO);
            return;
        }

        Gestor gestor = (Gestor) session.getAttribute(CADASTRO_GESTOR);
        Organizacao organizacao = (Organizacao) session.getAttribute(CADASTRO_ORGANIZACAO);
        Unidade unidade = (Unidade) session.getAttribute(CADASTRO_UNIDADE);
        Endereco endereco = (Endereco) session.getAttribute(CADASTRO_ENDERECO);
        Integer codPlano = (Integer) session.getAttribute(CADASTRO_PLANO);

        OrganizacaoDAO organizacaoDAO = new OrganizacaoDAO();
        UnidadeDAO unidadeDAO = new UnidadeDAO();
        GestorDAO gestorDAO = new GestorDAO();

        try {
            // confere de novo se nada foi cadastrado por outra pessoa durante o preenchimento, para não gravar só uma parte do cadastro
            String conflito = verificarConflitos(organizacao, unidade, gestor);
            if (conflito != null) {
                exibirErro(request, response, conflito, ERROR_PAGE);
                return;
            }

            boolean organizacaoNova = organizacao.getCodigo() == 0;

            // 1. organização (só se for nova); busca pelo CNPJ para descobrir o código gerado
            if (organizacaoNova) {
                organizacaoDAO.insert(organizacao);
                organizacao = organizacaoDAO.findByCnpj(organizacao.getCnpj());
            }

            // 2. unidade, ligada à organização
            unidade.setCodOrganizacao(organizacao.getCodigo());
            unidadeDAO.insert(unidade);
            unidade = unidadeDAO.findByCnpj(unidade.getCnpj());

            // 3. endereço, ligado à unidade
            endereco.setCodUnidade(unidade.getCodigo());
            new EnderecoDAO().insert(endereco);

            // 4. gestor, ligado à unidade
            gestor.setCodUnidade(unidade.getCodigo());
            gestorDAO.insert(gestor);
            gestor = gestorDAO.findByEmail(gestor.getEmail());

            // 5. assinatura, só para organização nova
            if (organizacaoNova && codPlano != null) {
                Plano plano = new PlanoDAO().findByCodigo(codPlano);
                LocalDate inicio = LocalDate.now();
                LocalDate fim = inicio.plusMonths(plano.getDuracaoEmMeses());
                new AssinaturaDAO().insert(
                        new Assinatura(inicio, fim, STATUS_ATIVA, organizacao.getCodigo(), plano.getCodigo()));
            }

            // descarta a sessão do cadastro e já deixa o gestor logado, com uma sessão nova
            session.invalidate();
            HttpSession novaSessao = request.getSession(true);
            novaSessao.setAttribute(SESSAO_GESTOR, gestor);
            novaSessao.setAttribute(SESSAO_UNIDADE, unidade);

            response.sendRedirect(request.getContextPath() + HOME_GESTOR);

        } catch (NotFoundException nfe) {
            nfe.printStackTrace();
            exibirErro(request, response, "Não foi possível concluir o cadastro. Tente novamente.", ERROR_PAGE);
        } catch (ConnectionFailedException cfe) {
            cfe.printStackTrace();
            exibirErro(request, response, cfe, ERROR_PAGE);
        } catch (Exception e) {
            e.printStackTrace();
            exibirErro(request, response, "Erro inesperado encontrado", ERROR_PAGE);
        }
    }

    /**
     * Verifica se o e-mail do gestor ou os CNPJs já foram cadastrados
     * enquanto o usuário preenchia o formulário.
     *
     * @param organizacao organização do cadastro
     * @param unidade unidade do cadastro
     * @param gestor gestor do cadastro
     * @return a mensagem do conflito encontrado, ou null se não houver nenhum
     */
    private String verificarConflitos(Organizacao organizacao, Unidade unidade, Gestor gestor) {
        // organização só é verificada se for nova
        if (organizacao.getCodigo() == 0 && organizacaoExiste(organizacao.getCnpj())) {
            return "Já existe uma organização com esse CNPJ.";
        }
        if (unidadeExiste(unidade.getCnpj())) {
            return "Já existe uma unidade com esse CNPJ.";
        }
        if (gestorExiste(gestor.getEmail())) {
            return "Já existe um cadastro com esse e-mail.";
        }
        return null;
    }

    /**
     * Verifica se já existe uma organização com o CNPJ informado.
     *
     * @param cnpj CNPJ da organização
     * @return true se o CNPJ já estiver cadastrado
     */
    private boolean organizacaoExiste(String cnpj) {
        try {
            new OrganizacaoDAO().findByCnpj(cnpj);
            return true;
        } catch (NotFoundException nfe) {
            return false;
        }
    }

    /**
     * Verifica se já existe uma unidade com o CNPJ informado.
     *
     * @param cnpj CNPJ da unidade
     * @return true se o CNPJ já estiver cadastrado
     */
    private boolean unidadeExiste(String cnpj) {
        try {
            new UnidadeDAO().findByCnpj(cnpj);
            return true;
        } catch (NotFoundException nfe) {
            return false;
        }
    }

    /**
     * Verifica se já existe um gestor com o e-mail informado.
     *
     * @param email e-mail do gestor
     * @return true se o e-mail já estiver cadastrado
     */
    private boolean gestorExiste(String email) {
        try {
            new GestorDAO().findByEmail(email);
            return true;
        } catch (NotFoundException nfe) {
            return false;
        }
    }

    /**
     * Confere se todos os passos do cadastro foram preenchidos. Para organização
     * nova, o plano também é obrigatório.
     *
     * @param session sessão atual
     * @return true se faltar algum passo
     */
    private boolean faltaPassoAnterior(HttpSession session) {
        Organizacao organizacao = (Organizacao) session.getAttribute(CADASTRO_ORGANIZACAO);
        if (session.getAttribute(CADASTRO_GESTOR) == null
                || organizacao == null
                || session.getAttribute(CADASTRO_UNIDADE) == null
                || session.getAttribute(CADASTRO_ENDERECO) == null) {
            return true;
        }
        return organizacao.getCodigo() == 0 && session.getAttribute(CADASTRO_PLANO) == null;
    }
}
