package br.com.zera.dao;

import br.com.zera.exception.ConnectionFailedException;
import br.com.zera.exception.NotFoundException;
import br.com.zera.model.Organizacao;
import br.com.zera.util.Conexao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe DAO responsável pelas operações de acesso ao banco de dados
 * para a entidade {@link Organizacao}.
 *
 * @author Pedro Rufino
 */
public class OrganizacaoDAO {

    /**
     * Insere uma nova organização no banco de dados.
     *
     * @param organizacao Objeto {@link Organizacao} contendo os dados a serem persistidos
     * @throws ConnectionFailedException se ocorrer falha na conexão ou execução do SQL
     */
    public void insert(Organizacao organizacao) {
        String sql = "INSERT INTO Organizacao (cnpj, nome, email, codCadastro) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, organizacao.getCnpj());
            stmt.setString(2, organizacao.getNome());
            stmt.setString(3, organizacao.getEmail());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new ConnectionFailedException("Erro ao inserir a organização no banco de dados.", e);
        }
    }

    /**
     * Atualiza os dados de uma organização já existente no banco de dados,
     * identificada pelo seu código.
     *
     * @param organizacao Objeto {@link Organizacao} com os dados atualizados
     * @throws NotFoundException se nenhuma organização com o código informado for encontrada
     * @throws ConnectionFailedException se ocorrer falha na conexão ou execução do SQL
     */
    public void update(Organizacao organizacao) {
        String sql = "UPDATE Organizacao SET cnpj = ?, nome = ?, email = ? WHERE codigo = ?";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, organizacao.getCnpj());
            stmt.setString(2, organizacao.getNome());
            stmt.setString(3, organizacao.getEmail());
            stmt.setInt(5, organizacao.getCodigo());

            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas == 0) {
                throw new NotFoundException("Organização", organizacao.getCodigo());
            }

        } catch (SQLException e) {
            throw new ConnectionFailedException("Erro ao atualizar os dados da organização.", e);
        }
    }

    /**
     * Busca uma organização no banco de dados a partir do seu código identificador.
     *
     * @param codigo Código (chave primária) da organização a ser buscada
     * @return a {@link Organizacao} correspondente ao código informado
     * @throws NotFoundException se nenhuma organização com o código informado for encontrada
     * @throws ConnectionFailedException se ocorrer falha na conexão ou execução do SQL
     */
    public Organizacao findByCodigo(int codigo) {
        String sql = "SELECT * FROM Organizacao WHERE codigo = ?";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, codigo);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearOrganizacao(rs);
                } else {
                    throw new NotFoundException("Organização", codigo);
                }
            }

        } catch (SQLException e) {
            throw new ConnectionFailedException("Erro ao buscar a organização pelo código.", e);
        }
    }

    /**
     * Busca uma organização no banco de dados a partir do seu código identificador.
     *
     * @param cnpj cnpj da organização a ser buscada
     * @return a {@link Organizacao} correspondente ao código informado
     * @throws NotFoundException se nenhuma organização com o código informado for encontrada
     * @throws ConnectionFailedException se ocorrer falha na conexão ou execução do SQL
     */
    public Organizacao findByCnpj(int cnpj) {
        String sql = "SELECT * FROM Organizacao WHERE cnpj = ?";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, cnpj);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearOrganizacao(rs);
                } else {
                    throw new NotFoundException("Organização", cnpj);
                }
            }

        } catch (SQLException e) {
            throw new ConnectionFailedException("Erro ao buscar a organização pelo cnpj.", e);
        }
    }

    /**
     * Lista todas as organizações cadastradas no banco de dados.
     *
     * @return uma {@link List} com todas as {@link Organizacao} encontradas;
     * @throws ConnectionFailedException se ocorrer falha na conexão ou execução do SQL
     */
    public List<Organizacao> findAll() {
        String sql = "SELECT * FROM Organizacao";
        List<Organizacao> lista = new ArrayList<>();

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearOrganizacao(rs));
            }

        } catch (SQLException e) {
            throw new ConnectionFailedException("Erro ao listar as organizações.", e);
        }

        return lista;
    }

    /**
     * Remove uma organização do banco de dados utilizando o seu código identificador.
     *
     * @param codigo Código (chave primária) da organização a ser deletada
     * @throws NotFoundException se nenhuma organização com o código informado for encontrada
     * @throws ConnectionFailedException se ocorrer falha na conexão ou execução do SQL
     */
    public void delete(int codigo) {
        String sql = "DELETE FROM Organizacao WHERE codigo = ?";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, codigo);

            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas == 0) {
                throw new NotFoundException("Organização", codigo);
            }

        } catch (SQLException e) {
            throw new ConnectionFailedException("Erro ao deletar a organização do banco de dados.", e);
        }
    }

    /**
     * Converte a linha atual do {@link ResultSet} num objeto {@link Organizacao},
     * incluindo os campos de auditoria (criado_em/atualizado_em), que podem ser nulos.
     *
     * @param rs ResultSet posicionado na linha a ser convertida
     * @return a {@link Organizacao} correspondente
     * @throws SQLException se ocorrer erro ao ler os dados do ResultSet
     */
    private Organizacao mapearOrganizacao(ResultSet rs) throws SQLException {
        Organizacao organizacao = new Organizacao(
                rs.getInt("codigo"),
                rs.getString("cnpj"),
                rs.getString("nome"),
                rs.getString("email")
                );
        return organizacao;
    }
}