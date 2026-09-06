package br.com.zera.dao;

import br.com.zera.exception.ConnectionFailedException;
import br.com.zera.exception.NotFoundException;
import br.com.zera.model.Categoria;
import br.com.zera.util.Conexao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe DAO responsável pelas operações de acesso ao banco de dados
 * para a entidade {@link Categoria}.
 *
 * @author Pedro Rufino
 */
public class CategoriaDAO {

    /**
     * Insere uma nova categoria no banco de dados.
     *
     * @param categoria Objeto {@link Categoria} contendo os dados a serem persistidos
     * @throws ConnectionFailedException se ocorrer falha na conexão ou execução do SQL
     */
    public void insert(Categoria categoria) {
        String sql = "INSERT INTO Categoria (nome) VALUES (?)";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, categoria.getNome());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new ConnectionFailedException("Erro ao cadastrar a categoria no banco de dados.", e);
        }
    }

    /**
     * Atualiza os dados de uma categoria já existente no banco de dados,
     * identificada pelo seu código.
     *
     * @param categoria Objeto {@link Categoria} com os dados atualizados (o código deve estar preenchido)
     * @throws NotFoundException se nenhuma categoria com o código informado for encontrada
     * @throws ConnectionFailedException se ocorrer falha na conexão ou execução do SQL
     */
    public void update(Categoria categoria) {
        String sql = "UPDATE Categoria SET nome = ? WHERE codigo = ?";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, categoria.getNome());
            stmt.setInt(2, categoria.getCodigo());

            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas == 0) {
                throw new NotFoundException("Categoria", categoria.getCodigo());
            }

        } catch (SQLException e) {
            throw new ConnectionFailedException("Erro ao atualizar os dados da categoria.", e);
        }
    }

    /**
     * Busca uma categoria no banco de dados a partir do seu código identificador.
     *
     * @param codigo Código (chave primária) da categoria a ser buscada
     * @return a {@link Categoria} correspondente ao código informado
     * @throws NotFoundException se nenhuma categoria com o código informado for encontrada
     * @throws ConnectionFailedException se ocorrer falha na conexão ou execução do SQL
     */
    public Categoria findByCodigo(int codigo) {
        String sql = "SELECT * FROM Categoria WHERE codigo = ?";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, codigo);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Categoria(rs.getInt("codigo"), rs.getString("nome"));
                } else {
                    throw new NotFoundException("Categoria", codigo);
                }
            }

        } catch (SQLException e) {
            throw new ConnectionFailedException("Erro ao buscar a categoria pelo código.", e);
        }
    }

    /**
     * Lista todas as categorias cadastradas no banco de dados.
     *
     * @return uma {@link List} com todas as {@link Categoria} encontradas;
     *         lista vazia caso não existam registros
     * @throws ConnectionFailedException se ocorrer falha na conexão ou execução do SQL
     */
    public List<Categoria> findAll() {
        String sql = "SELECT * FROM Categoria";
        List<Categoria> lista = new ArrayList<>();

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(new Categoria(rs.getInt("codigo"), rs.getString("nome")));
            }

        } catch (SQLException e) {
            throw new ConnectionFailedException("Erro ao listar as categorias.", e);
        }

        return lista;
    }

    /**
     * Remove uma categoria do banco de dados utilizando o seu código identificador.
     *
     * @param codigo Código (chave primária) da categoria a ser deletada
     * @throws NotFoundException se nenhuma categoria com o código informado for encontrada
     * @throws ConnectionFailedException se ocorrer falha na conexão ou execução do SQL
     */
    public void delete(int codigo) {
        String sql = "DELETE FROM Categoria WHERE codigo = ?";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, codigo);

            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas == 0) {
                throw new NotFoundException("Categoria", codigo);
            }

        } catch (SQLException e) {
            throw new ConnectionFailedException("Erro ao deletar a categoria do banco de dados.", e);
        }
    }
}