package br.com.zera.dao;

import br.com.zera.exception.ConnectionFailedException;
import br.com.zera.exception.NotFoundException;
import br.com.zera.model.Organizacao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
        String sql = "INSERT INTO Organizacao (cnpj, nome, data_cadastro) VALUES (?, ?, ?)";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, organizacao.getCnpj());
            stmt.setString(2, organizacao.getNome());
            stmt.setObject(3, organizacao.getDataCadastro());

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
        String sql = "UPDATE Organizacao SET cnpj = ?, nome = ?, data_cadastro = ? WHERE codigo = ?";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, organizacao.getCnpj());
            stmt.setString(2, organizacao.getNome());
            stmt.setObject(3, organizacao.getDataCadastro());
            stmt.setInt(4, organizacao.getCodigo());

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
                    // Retornando a classe certa (Organizacao) e adicionando o getObject para a data
                    return new Organizacao(
                            rs.getInt("codigo"),
                            rs.getString("cnpj"),
                            rs.getString("nome"), // <-- Faltava a coluna nome
                            rs.getObject("data_cadastro", LocalDate.class)
                    );
                } else {
                    throw new NotFoundException("Organização", codigo);
                }
            }

        } catch (SQLException e) {
            throw new ConnectionFailedException("Erro ao buscar a organização pelo código.", e);
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
                Organizacao organizacao = new Organizacao(
                        rs.getInt("codigo"),
                        rs.getString("cnpj"),
                        rs.getString("nome"),
                        rs.getObject("data_cadastro", LocalDate.class)
                );
                lista.add(organizacao);
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
}