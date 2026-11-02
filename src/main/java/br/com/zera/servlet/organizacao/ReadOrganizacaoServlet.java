

package br.com.zera.servlet.organizacao;

import br.com.zera.dao.*;
import br.com.zera.exception.ConnectionFailedException;
import br.com.zera.exception.NotFoundException;
import br.com.zera.model.*;
import br.com.zera.util.Sessao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static br.com.zera.exception.ErroServlet.exibirErro;
import static br.com.zera.regex.Constants.ERROR_PAGE;

/**
 * Servlet responsável por mostrar uma organização.
 *
 * O que aparece depende de quem está logado:
 * - Admin com o parâmetro codigo: abre o formulário de edição da organização escolhida na lista.
 * - Admin sem código: abre o formulário de nova organização.
 * - Gestor: mostra a organização da própria unidade.
 *
 * @author Mayte B
 * @since 2026-10-06
 */
@WebServlet(name = "ReadOrganizacao", value = "/organizacao/perfil")
public class ReadOrganizacaoServlet extends HttpServlet {

    /**
     * Processa a requisição GET da organização.
     *
     * @param request requisição HTTP
     * @param response resposta HTTP
     * @throws ServletException erro no encaminhamento
     * @throws IOException erro de entrada ou saída
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            Admin admin = Sessao.admin(request);
            Unidade unidadeLogada = Sessao.unidade(request);

            OrganizacaoDAO dao = new OrganizacaoDAO();
            String pagina;

            if (admin != null) {
                String codigoTexto = request.getParameter("codigo");
                String cnpj = request.getParameter("cnpj");
                Organizacao organizacao = null;

                if (codigoTexto != null && !codigoTexto.isBlank()) {
                    int codigo = Integer.parseInt(codigoTexto.trim());
                    organizacao = dao.findByCodigo(codigo);

                } else if (cnpj != null && !cnpj.isBlank()) {
                    organizacao = dao.findByCnpj(cnpj.trim());
                }

                if (organizacao == null) {
                    pagina = "/WEB-INF/admin/organizacao/nova-organizacao.jsp";

                } else {
                    request.setAttribute("organizacao", organizacao);
                    boolean visualizar = "true".equalsIgnoreCase(
                            request.getParameter("ver")
                    );

                    if (visualizar) {
                        carregarDadosVisualizacao(request, organizacao);
                        pagina = "/WEB-INF/admin/organizacao/ver-organizacao.jsp";
                    } else {
                        pagina = "/WEB-INF/admin/organizacao/editar-organizacao.jsp";
                    }
                }

            } else if (unidadeLogada != null) {
                request.setAttribute(
                        "organizacao",
                        dao.findByCodigo(unidadeLogada.getCodOrganizacao())
                );
                pagina = "/WEB-INF/gestor/organizacao.jsp";
            } else {
                exibirErro(request, response, "Sessão expirada. Faça login novamente.", ERROR_PAGE);
                return;
            }

            request.getRequestDispatcher(pagina).forward(request, response);

        } catch (NotFoundException e) {
            exibirErro(request, response, "Organização não encontrada.", ERROR_PAGE);

        } catch (NumberFormatException e) {
            exibirErro(request, response, "Código inválido.", ERROR_PAGE
            );

        } catch (ConnectionFailedException e) {
            e.printStackTrace();
            exibirErro(request, response, e, ERROR_PAGE);

        } catch (Exception e) {
            e.printStackTrace();
            exibirErro(request, response, "Erro inesperado encontrado.", ERROR_PAGE);
        }
    }

    /**
     * Carrega os dados relacionados à organização para a
     * página de visualização do administrador.
     *
     * @param request requisição que receberá os atributos
     * @param organizacao organização selecionada
     */
    private void carregarDadosVisualizacao(
            HttpServletRequest request,
            Organizacao organizacao) {
        int codigoOrganizacao = organizacao.getCodigo();

        // Assinatura vinculada à organização
        Assinatura assinatura = null;

        for (Assinatura item : new AssinaturaDAO().findAll()) {
            if (item.getCodOrganizacao() == codigoOrganizacao) {
                if (assinatura == null ||
                        (item.getDataFim() != null &&
                                (assinatura.getDataFim() == null ||
                                        item.getDataFim().isAfter(assinatura.getDataFim())))) {

                    assinatura = item;
                }
            }
        }

        if (assinatura != null) {
            request.setAttribute("assinatura", assinatura);
            Plano plano = new PlanoDAO().findByCodigo(
                    assinatura.getCodPlano()
            );
            request.setAttribute("plano", plano);
        }

        // Unidades pertencentes à organização
        List<Unidade> unidades = new ArrayList<>();
        for (Unidade unidade : new UnidadeDAO().findAll()) {
            if (unidade.getCodOrganizacao() == codigoOrganizacao) {
                unidades.add(unidade);
            }
        }

        request.setAttribute("unidades", unidades);

        // Endereços das unidades
        Map<Integer, String> enderecosUnidades = new HashMap<>();
        for (Endereco endereco : new EnderecoDAO().findAll()) {
            for (Unidade unidade : unidades) {
                if (endereco.getCodUnidade() == unidade.getCodigo()) {
                    String cidade = endereco.getCidade();
                    String estado = endereco.getEstado();
                    String localizacao = cidade + "/" + estado;
                    enderecosUnidades.put(
                            unidade.getCodigo(),
                            localizacao
                    );
                }
            }
        }
        request.setAttribute("enderecosUnidades", enderecosUnidades);

        // Quantidade de gestores por unidade
        Map<Integer, Integer> totalGestoresUnidade = new HashMap<>();

        for (Unidade unidade : unidades) {
            totalGestoresUnidade.put(unidade.getCodigo(), 0);
        }

        for (Gestor gestor : new GestorDAO().findAll()) {
            int codigoUnidade = gestor.getCodUnidade();
            if (totalGestoresUnidade.containsKey(codigoUnidade)) {
                totalGestoresUnidade.put(
                        codigoUnidade,
                        totalGestoresUnidade.get(codigoUnidade) + 1
                );
            }
        }

        request.setAttribute(
                "totalGestoresUnidade",
                totalGestoresUnidade
        );

        // Quantidade de estoques por unidade
        Map<Integer, Integer> totalEstoquesUnidade = new HashMap<>();

        for (Unidade unidade : unidades) {
            totalEstoquesUnidade.put(unidade.getCodigo(), 0);
        }

        for (Estoque estoque : new EstoqueDAO().findAll()) {
            int codigoUnidade = estoque.getCodUnidade();
            if (totalEstoquesUnidade.containsKey(codigoUnidade)) {
                totalEstoquesUnidade.put(
                        codigoUnidade,
                        totalEstoquesUnidade.get(codigoUnidade) + 1
                );
            }
        }
        request.setAttribute(
                "totalEstoquesUnidade",
                totalEstoquesUnidade
        );
    }
}