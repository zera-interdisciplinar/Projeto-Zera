package br.com.zera.dao;

import br.com.zera.exception.ConnectionFailedException;
import br.com.zera.exception.NotFoundException;
import br.com.zera.model.Item;
import br.com.zera.util.Conexao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe DAO responsável pelas operações de acesso ao banco de dados
 * para a entidade {@link Item}.
 *
 * @author Pedro Rufino
 */
public class ItemDAO {

    /**
     * Insere um novo item no banco de dados.
     *
     * @param item Objeto {@link Item} contendo os dados a serem persistidos
     * @throws ConnectionFailedException se ocorrer falha na conexão ou execução do SQL
     */
    public void insert(Item item) {
        String sql = "INSERT INTO Item (tempo_vida, cod_estoque, cod_modelo, cod_categoria, status, data_chegada) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, java.sql.Date.valueOf(item.getTempoVida()));
            stmt.setInt(2, item.getCodEstoque());
            stmt.setInt(3, item.getCodModelo());
            stmt.setInt(4, item.getCodCategoria());
            stmt.setString(5, item.getStatus());
            stmt.setDate(6, java.sql.Date.valueOf(item.getDataChegada()));
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new ConnectionFailedException("Erro ao cadastrar o item no banco de dados.", e);
        }
    }

    /**
     * Atualiza os dados de um item já existente no banco de dados,
     * identificado pelo seu código.
     *
     * @param item Objeto {@link Item} com os dados atualizados (o código deve estar preenchido)
     * @throws NotFoundException se nenhum item com o código informado for encontrado
     * @throws ConnectionFailedException se ocorrer falha na conexão ou execução do SQL
     */
    public void update(Item item) {
        String sql = "UPDATE Item SET tempo_vida = ?, cod_estoque = ?, cod_modelo = ?, " +
                "cod_categoria = ?, status = ?, data_chegada = ? WHERE codigo = ?";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, java.sql.Date.valueOf(item.getTempoVida()));
            stmt.setInt(2, item.getCodEstoque());
            stmt.setInt(3, item.getCodModelo());
            stmt.setInt(4, item.getCodCategoria());
            stmt.setString(5, item.getStatus());
            stmt.setDate(6, java.sql.Date.valueOf(item.getDataChegada()));
            stmt.setInt(7, item.getCodigo());

            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas == 0) {
                throw new NotFoundException("Item", item.getCodigo());
            }

        } catch (SQLException e) {
            throw new ConnectionFailedException("Erro ao atualizar os dados do item.", e);
        }
    }

    /**
     * Busca um item no banco de dados a partir do seu código identificador.
     *
     * @param codigo Código (chave primária) do item a ser buscado
     * @return o {@link Item} correspondente ao código informado
     * @throws NotFoundException se nenhum item com o código informado for encontrado
     * @throws ConnectionFailedException se ocorrer falha na conexão ou execução do SQL
     */
    public Item findByCodigo(int codigo) {
        String sql = "SELECT * FROM Item WHERE codigo = ?";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, codigo);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearItem(rs);
                } else {
                    throw new NotFoundException("Item", codigo);
                }
            }

        } catch (SQLException e) {
            throw new ConnectionFailedException("Erro ao buscar o item pelo código.", e);
        }
    }

    /**
     * Lista todos os itens cadastrados no banco de dados.
     *
     * @return uma {@link List} com todos os {@link Item} encontrados;
     *         lista vazia caso não existam registros
     * @throws ConnectionFailedException se ocorrer falha na conexão ou execução do SQL
     */
    public List<Item> findAll() {
        String sql = "SELECT * FROM Item";
        List<Item> lista = new ArrayList<>();

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearItem(rs));
            }

        } catch (SQLException e) {
            throw new ConnectionFailedException("Erro ao listar os itens.", e);
        }

        return lista;
    }

    /**
     * Remove um item do banco de dados utilizando o seu código identificador.
     *
     * @param codigo Código (chave primária) do item a ser deletado
     * @throws NotFoundException se nenhum item com o código informado for encontrado
     * @throws ConnectionFailedException se ocorrer falha na conexão ou execução do SQL
     */
    public void delete(int codigo) {
        String sql = "DELETE FROM Item WHERE codigo = ?";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, codigo);

            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas == 0) {
                throw new NotFoundException("Item", codigo);
            }

        } catch (SQLException e) {
            throw new ConnectionFailedException("Erro ao deletar o item do banco de dados.", e);
        }
    }

    /**
     * Converte a linha atual de um {@link ResultSet} em um objeto {@link Item}.
     * Método privado de apoio para evitar repetição entre findByCodigo e findAll.
     *
     * @param rs ResultSet posicionado em uma linha válida da tabela Item
     * @return o {@link Item} montado a partir da linha atual
     * @throws SQLException se ocorrer erro ao ler alguma coluna do ResultSet
     */
    private Item mapearItem(ResultSet rs) throws SQLException {
        return new Item(
                rs.getInt("codigo"),
                rs.getDate("tempo_vida").toLocalDate(),
                rs.getInt("cod_estoque"),
                rs.getInt("cod_modelo"),
                rs.getInt("cod_categoria"),
                rs.getString("status"),
                rs.getDate("data_chegada").toLocalDate()
        );
    }
}
