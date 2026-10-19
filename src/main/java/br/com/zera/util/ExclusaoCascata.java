package br.com.zera.util;

import br.com.zera.dao.AssinaturaDAO;
import br.com.zera.dao.EnderecoDAO;
import br.com.zera.dao.EstoqueDAO;
import br.com.zera.dao.GestorDAO;
import br.com.zera.dao.OrganizacaoDAO;
import br.com.zera.dao.PlanoDAO;
import br.com.zera.dao.UnidadeDAO;
import br.com.zera.exception.NotFoundException;
import br.com.zera.model.Assinatura;
import br.com.zera.model.Estoque;
import br.com.zera.model.Gestor;
import br.com.zera.model.Unidade;

/**
 * Classe com as exclusões em cascata do sistema.
 *
 * O banco não deixa apagar um registro que outro ainda aponta (chave
 * estrangeira). Por isso, cada método apaga primeiro quem depende do
 * registro e só depois o registro em si:
 * - Gestor: estoques em que ele é responsável e depois o gestor.
 * - Unidade: estoques, gestores e endereço da unidade e depois a unidade.
 * - Organização: todas as unidades (com tudo delas), as assinaturas e depois a organização.
 * - Plano: as assinaturas que usam o plano e depois o plano.
 *
 * Os métodos são usados pelos servlets de exclusão e pelo "Excluir minha conta".
 *
 * @author Pedro Rufino
 * @since 2026-10-17
 */
public class ExclusaoCascata {

    /**
     * Exclui um gestor e os estoques em que ele é o responsável.
     *
     * @param codGestor código do gestor
     * @throws NotFoundException se o gestor não existir
     * @throws br.com.zera.exception.ConnectionFailedException se ocorrer falha no banco
     */
    public static void excluirGestor(int codGestor) {
        // apaga os estoques em que ele é responsável
        EstoqueDAO estoqueDAO = new EstoqueDAO();
        for (Estoque estoque : estoqueDAO.findAll()) {
            if (estoque.getCodResponsavel() == codGestor) {
                estoqueDAO.delete(estoque.getCodigo());
            }
        }

        new GestorDAO().delete(codGestor);
    }

    /**
     * Exclui uma unidade com os estoques, gestores e endereço dela.
     *
     * @param codUnidade código da unidade
     * @throws NotFoundException se a unidade não existir
     * @throws br.com.zera.exception.ConnectionFailedException se ocorrer falha no banco
     */
    public static void excluirUnidade(int codUnidade) {
        // confere se a unidade existe antes de começar a apagar
        new UnidadeDAO().findByCodigo(codUnidade);

        // estoques primeiro, porque eles apontam para a unidade e para os gestores
        EstoqueDAO estoqueDAO = new EstoqueDAO();
        for (Estoque estoque : estoqueDAO.findAll()) {
            if (estoque.getCodUnidade() == codUnidade) {
                estoqueDAO.delete(estoque.getCodigo());
            }
        }

        // gestores da unidade
        GestorDAO gestorDAO = new GestorDAO();
        for (Gestor gestor : gestorDAO.findAll()) {
            if (gestor.getCodUnidade() == codUnidade) {
                gestorDAO.delete(gestor.getCodigo());
            }
        }

        // endereço da unidade (se tiver)
        EnderecoDAO enderecoDAO = new EnderecoDAO();
        try {
            enderecoDAO.delete(enderecoDAO.findByCodUnidade(codUnidade).getCodigo());
        } catch (NotFoundException nfe) {
            // unidade sem endereço: não tem o que apagar
        }

        new UnidadeDAO().delete(codUnidade);
    }

    /**
     * Exclui uma organização com todas as unidades e assinaturas dela.
     *
     * @param codOrganizacao código da organização
     * @throws NotFoundException se a organização não existir
     * @throws br.com.zera.exception.ConnectionFailedException se ocorrer falha no banco
     */
    public static void excluirOrganizacao(int codOrganizacao) {
        // confere se a organização existe antes de começar a apagar
        new OrganizacaoDAO().findByCodigo(codOrganizacao);

        // cada unidade é apagada com tudo o que é dela
        for (Unidade unidade : new UnidadeDAO().findAll()) {
            if (unidade.getCodOrganizacao() == codOrganizacao) {
                excluirUnidade(unidade.getCodigo());
            }
        }

        // assinaturas da organização
        AssinaturaDAO assinaturaDAO = new AssinaturaDAO();
        for (Assinatura assinatura : assinaturaDAO.findAll()) {
            if (assinatura.getCodOrganizacao() == codOrganizacao) {
                assinaturaDAO.delete(assinatura.getCodigo());
            }
        }

        new OrganizacaoDAO().delete(codOrganizacao);
    }

    /**
     * Exclui um plano e as assinaturas que usam ele.
     *
     * @param codPlano código do plano
     * @throws NotFoundException se o plano não existir
     * @throws br.com.zera.exception.ConnectionFailedException se ocorrer falha no banco
     */
    public static void excluirPlano(int codPlano) {
        // confere se o plano existe antes de começar a apagar
        new PlanoDAO().findByCodigo(codPlano);

        // assinaturas que usam o plano
        AssinaturaDAO assinaturaDAO = new AssinaturaDAO();
        for (Assinatura assinatura : assinaturaDAO.findAll()) {
            if (assinatura.getCodPlano() == codPlano) {
                assinaturaDAO.delete(assinatura.getCodigo());
            }
        }

        new PlanoDAO().delete(codPlano);
    }

    /**
     * Verifica se a organização ainda tem alguma unidade.
     *
     * @param codOrganizacao código da organização
     * @return true se existir pelo menos uma unidade da organização
     * @throws br.com.zera.exception.ConnectionFailedException se ocorrer falha no banco
     */
    public static boolean organizacaoTemUnidades(int codOrganizacao) {
        for (Unidade unidade : new UnidadeDAO().findAll()) {
            if (unidade.getCodOrganizacao() == codOrganizacao) {
                return true;
            }
        }
        return false;
    }
}
