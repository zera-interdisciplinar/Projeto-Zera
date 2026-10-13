package br.com.zera.dao;

import br.com.zera.exception.ConnectionFailedException;
import br.com.zera.exception.NotFoundException;
import br.com.zera.model.Estoque;
import br.com.zera.util.Conexao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe DAO responsável pelas operações de acesso ao banco de dados
 * para a entidade {@link Estoque}.
 *
 * @author Mayte B
 */
public class EstoqueDAO {

    /**
     * Insere um novo estoque no banco de dados.
     *
     * @param estoque Objeto {@link Estoque} contendo os dados a serem persistidos
     * @throws ConnectionFailedException se ocorrer falha na conexão ou execução do SQL
     */
    public void insert(Estoque estoque) {
        String sql = "INSERT INTO estoque (espaco_na_unidade, cod_responsavel, cod_unidade) VALUES (?, ?, ?)";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, estoque.getEspacoNaUnidade());
            stmt.setInt(2, estoque.getCodResponsavel());
            stmt.setInt(3, estoque.getCodUnidade());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new ConnectionFailedException("Erro ao cadastrar o estoque no banco de dados.", e);
        }
    }

    /**
     * Atualiza os dados de um estoque já existente no banco de dados,
     * identificado pelo seu código.
     *
     * @param estoque Objeto {@link Estoque} com os dados atualizados (o código deve estar preenchido)
     * @throws NotFoundException se nenhum estoque com o código informado for encontrado
     * @throws ConnectionFailedException se ocorrer falha na conexão ou execução do SQL
     */
    public void update(Estoque estoque) {
        String sql = "UPDATE estoque SET espaco_na_unidade = ?, cod_responsavel = ?, cod_unidade = ? WHERE codigo = ?";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, estoque.getEspacoNaUnidade());
            stmt.setInt(2, estoque.getCodResponsavel());
            stmt.setInt(3, estoque.getCodUnidade());
            stmt.setInt(4, estoque.getCodigo());

            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas == 0) {
                throw new NotFoundException("Estoque", estoque.getCodigo());
            }

        } catch (SQLException e) {
            throw new ConnectionFailedException("Erro ao atualizar os dados do estoque.", e);
        }
    }

    /**
     * Busca um estoque no banco de dados a partir do seu código identificador.
     *
     * @param codigo Código (chave primária) do estoque a ser buscado
     * @return o {@link Estoque} correspondente ao código informado
     * @throws NotFoundException se nenhum estoque com o código informado for encontrado
     * @throws ConnectionFailedException se ocorrer falha na conexão ou execução do SQL
     */
    public Estoque findByCodigo(int codigo) {
        String sql = "SELECT * FROM estoque WHERE codigo = ?";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, codigo);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Estoque(
                            rs.getInt("codigo"),
                            rs.getString("espaco_na_unidade"),
                            rs.getInt("cod_responsavel"),
                            rs.getInt("cod_unidade")
                    );
                } else {
                    throw new NotFoundException("Estoque", codigo);
                }
            }

        } catch (SQLException e) {
            throw new ConnectionFailedException("Erro ao buscar o estoque pelo código.", e);
        }
    }

    /**
     * Lista todos os estoques cadastrados no banco de dados.
     *
     * @return uma {@link List} com todos os {@link Estoque} encontrados;
     *         lista vazia caso não existam registros
     * @throws ConnectionFailedException se ocorrer falha na conexão ou execução do SQL
     */
    public List<Estoque> findAll() {
        String sql = "SELECT * FROM estoque";
        List<Estoque> lista = new ArrayList<>();

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(new Estoque(
                        rs.getInt("codigo"),
                        rs.getString("espaco_na_unidade"),
                        rs.getInt("cod_responsavel"),
                        rs.getInt("cod_unidade")
                ));
            }

        } catch (SQLException e) {
            throw new ConnectionFailedException("Erro ao listar os estoques.", e);
        }

        return lista;
    }

    /**
     * Remove um estoque do banco de dados utilizando o seu código identificador.
     *
     * @param codigo Código (chave primária) do estoque a ser deletado
     * @throws NotFoundException se nenhum estoque com o código informado for encontrado
     * @throws ConnectionFailedException se ocorrer falha na conexão ou execução do SQL
     */
    public void delete(int codigo) {
        String sql = "DELETE FROM estoque WHERE codigo = ?";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, codigo);

            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas == 0) {
                throw new NotFoundException("Estoque", codigo);
            }

        } catch (SQLException e) {
            throw new ConnectionFailedException("Erro ao deletar o estoque do banco de dados.", e);
        }
    }
}