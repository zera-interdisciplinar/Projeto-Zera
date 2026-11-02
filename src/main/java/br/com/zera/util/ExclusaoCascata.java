package br.com.zera.util;

import br.com.zera.exception.ConnectionFailedException;
import br.com.zera.exception.NotFoundException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe responsável pelas exclusões em cascata do sistema.
 *
 * Excluir um gestor, endereço ou unidade remove toda a unidade e seus dados.
 * Excluir a organização remove também todas as unidades e assinaturas.
 *
 * As exclusões relacionadas são executadas na mesma transação, para que
 * nenhum registro seja removido parcialmente quando ocorrer uma falha.
 *
 * @author Pedro Rufino
 * @since 2026-10-17
 */
public class ExclusaoCascata {

    /**
     * Impede a instanciação da classe utilitária.
     */
    private ExclusaoCascata() {}

    /**
     * Executa uma operação de exclusão dentro de uma transação.
     *
     * @param operacao operação que utilizará a conexão compartilhada
     * @throws ConnectionFailedException se ocorrer falha no banco de dados
     */
    private static void executarTransacao(OperacaoExclusao operacao) {
        try (Connection conn = Conexao.getConexao()) {
            conn.setAutoCommit(false);
            try {
                operacao.executar(conn);
                conn.commit();
            } catch (Exception e) {
                try {
                    conn.rollback();
                } catch (SQLException erroRollback) {
                    e.addSuppressed(erroRollback);
                }
                if (e instanceof RuntimeException) {
                    throw (RuntimeException) e;
                }
                throw new ConnectionFailedException("Erro durante a exclusão em cascata.", e);
            }
        } catch (SQLException e) {
            throw new ConnectionFailedException("Erro ao executar transação de exclusão.", e);
        }
    }

    /**
     * Interface interna que representa uma operação transacional.
     */
    @FunctionalInterface
    private interface OperacaoExclusao {
        void executar(Connection conn) throws SQLException;
    }

    /**
     * Confere se o registro existe e bloqueia sua alteração até o fim da transação.
     *
     * @param conn conexão utilizada na transação
     * @param tabela nome de uma tabela definida internamente
     * @param entidade nome da entidade para a mensagem de erro
     * @param codigo identificador do registro
     * @throws SQLException se ocorrer erro na consulta
     * @throws NotFoundException se o registro não existir
     */
    private static void conferirRegistro(Connection conn, String tabela, String entidade, int codigo)
            throws SQLException {
        String sql = "SELECT codigo FROM " + tabela + " WHERE codigo = ? FOR UPDATE";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, codigo);
            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    throw new NotFoundException(entidade, codigo);
                }
            }
        }
    }

    /**
     * Exclui registros de uma tabela com filtro por código.
     *
     * @param conn conexão utilizada na transação
     * @param sql comando parametrizado de exclusão
     * @param codigo código utilizado como parâmetro
     * @throws SQLException se ocorrer erro na exclusão
     */
    private static void executarDelete(Connection conn, String sql, int codigo) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, codigo);
            stmt.executeUpdate();
        }
    }

    /**
     * Exclui o gestor e toda a unidade à qual ele pertence.
     *
     * @param codGestor código do gestor
     * @throws NotFoundException se o gestor não existir
     * @throws ConnectionFailedException se ocorrer falha no banco
     */
    public static void excluirGestor(int codGestor) {
        executarTransacao(conn -> {
            int codUnidade = buscarCodigoUnidade(conn,
                    "SELECT cod_unidade FROM gestor WHERE codigo = ?", "Gestor", codGestor);
            excluirUnidade(conn, codUnidade);
        });
    }

    /**
     * Exclui o endereço e toda a unidade à qual ele pertence.
     *
     * @param codEndereco código do endereço
     * @throws NotFoundException se o endereço não existir
     * @throws ConnectionFailedException se ocorrer falha no banco
     */
    public static void excluirEndereco(int codEndereco) {
        executarTransacao(conn -> {
            int codUnidade = buscarCodigoUnidade(conn,
                    "SELECT cod_unidade FROM endereco WHERE codigo = ?", "Endereço", codEndereco);
            excluirUnidade(conn, codUnidade);
        });
    }

    /**
     * Busca o código da unidade vinculada a um registro.
     *
     * @param conn conexão utilizada na transação
     * @param sql consulta parametrizada do registro
     * @param entidade nome da entidade para a mensagem de erro
     * @param codigo identificador do registro
     * @return código da unidade vinculada
     * @throws SQLException se ocorrer falha na consulta
     * @throws NotFoundException se o registro não existir
     */
    private static int buscarCodigoUnidade(Connection conn, String sql, String entidade, int codigo)
            throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, codigo);
            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    throw new NotFoundException(entidade, codigo);
                }
                return rs.getInt("cod_unidade");
            }
        }
    }

    /**
     * Exclui uma unidade e os estoques, gestores e endereços associados.
     *
     * @param codUnidade código da unidade
     * @throws NotFoundException se a unidade não existir
     * @throws ConnectionFailedException se ocorrer falha no banco
     */
    public static void excluirUnidade(int codUnidade) {
        executarTransacao(conn -> excluirUnidade(conn, codUnidade));
    }

    /**
     * Exclui uma unidade utilizando a conexão compartilhada.
     *
     * @param conn conexão da transação
     * @param codUnidade código da unidade
     * @throws SQLException se ocorrer falha no banco
     */
    private static void excluirUnidade(Connection conn, int codUnidade) throws SQLException {
        conferirRegistro(conn, "unidade", "Unidade", codUnidade);
        // Apaga os estoques antes dos gestores, pois possuem chave estrangeira.
        executarDelete(conn, "DELETE FROM estoque WHERE cod_unidade = ?", codUnidade);
        executarDelete(conn, "DELETE FROM gestor WHERE cod_unidade = ?", codUnidade);
        executarDelete(conn, "DELETE FROM endereco WHERE cod_unidade = ?", codUnidade);
        executarDelete(conn, "DELETE FROM unidade WHERE codigo = ?", codUnidade);
    }

    /**
     * Exclui uma organização, suas unidades e suas assinaturas.
     *
     * @param codOrganizacao código da organização
     * @throws NotFoundException se a organização não existir
     * @throws ConnectionFailedException se ocorrer falha no banco
     */
    public static void excluirOrganizacao(int codOrganizacao) {
        executarTransacao(conn -> {
            conferirRegistro(conn, "organizacao", "Organização", codOrganizacao);
            List<Integer> unidades = new ArrayList<>();
            String sql = "SELECT codigo FROM unidade WHERE cod_organizacao = ? ORDER BY codigo";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, codOrganizacao);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        unidades.add(rs.getInt("codigo"));
                    }
                }
            }
            // Usa a mesma transação para todas as unidades da organização.
            for (Integer codigo : unidades) {
                excluirUnidade(conn, codigo);
            }
            executarDelete(conn, "DELETE FROM assinatura WHERE cod_organizacao = ?", codOrganizacao);
            executarDelete(conn, "DELETE FROM organizacao WHERE codigo = ?", codOrganizacao);
        });
    }

    /**
     * Exclui um plano e as assinaturas relacionadas.
     *
     * @param codPlano código do plano
     * @throws NotFoundException se o plano não existir
     * @throws ConnectionFailedException se ocorrer falha no banco
     */
    public static void excluirPlano(int codPlano) {
        executarTransacao(conn -> {
            conferirRegistro(conn, "plano", "Plano", codPlano);
            executarDelete(conn, "DELETE FROM assinatura WHERE cod_plano = ?", codPlano);
            executarDelete(conn, "DELETE FROM plano WHERE codigo = ?", codPlano);
        });
    }

    /**
     * Verifica se existem unidades vinculadas à organização.
     *
     * @param codOrganizacao código da organização
     * @return true se existir alguma unidade associada
     * @throws ConnectionFailedException se ocorrer falha no banco
     */
    public static boolean organizacaoTemUnidades(int codOrganizacao) {
        String sql = "SELECT 1 FROM unidade WHERE cod_organizacao = ? LIMIT 1";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, codOrganizacao);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new ConnectionFailedException("Erro ao consultar unidades da organização.", e);
        }
    }
}
