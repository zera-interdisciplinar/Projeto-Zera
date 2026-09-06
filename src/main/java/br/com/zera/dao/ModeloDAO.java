package br.com.zera.dao;

import br.com.zera.exception.ConnectionFailedException;
import br.com.zera.exception.NotFoundException;
import br.com.zera.model.Modelo;
import br.com.zera.util.Conexao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe DAO responsável pelas operações de acesso ao banco de dados
 * para a entidade {@link Modelo}.
 *
 * @author Pedro Rufino
 */
public class ModeloDAO {

    /**
     * Insere um novo modelo no banco de dados.
     *
     * @param modelo Objeto {@link Modelo} contendo os dados a serem persistidos
     * @throws ConnectionFailedException se ocorrer falha na conexão ou execução do SQL
     */
    public void insert(Modelo modelo) {
        String sql = "INSERT INTO Modelo (nome, volume, ano) VALUES (?, ?, ?)";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, modelo.getNome());
            stmt.setBigDecimal(2, modelo.getVolume());
            stmt.setInt(3, modelo.getAno());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new ConnectionFailedException("Erro ao cadastrar o modelo no banco de dados.", e);
        }
    }

    /**
     * Atualiza os dados de um modelo já existente no banco de dados,
     * identificado pelo seu código.
     *
     * @param modelo Objeto {@link Modelo} com os dados atualizados (o código deve estar preenchido)
     * @throws NotFoundException se nenhum modelo com o código informado for encontrado
     * @throws ConnectionFailedException se ocorrer falha na conexão ou execução do SQL
     */
    public void update(Modelo modelo) {
        String sql = "UPDATE Modelo SET nome = ?, volume = ?, ano = ? WHERE codigo = ?";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, modelo.getNome());
            stmt.setBigDecimal(2, modelo.getVolume());
            stmt.setInt(3, modelo.getAno());
            stmt.setInt(4, modelo.getCodigo());

            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas == 0) {
                throw new NotFoundException("Modelo", modelo.getCodigo());
            }

        } catch (SQLException e) {
            throw new ConnectionFailedException("Erro ao atualizar os dados do modelo.", e);
        }
    }

    /**
     * Busca um modelo no banco de dados a partir do seu código identificador.
     *
     * @param codigo Código (chave primária) do modelo a ser buscado
     * @return o {@link Modelo} correspondente ao código informado
     * @throws NotFoundException se nenhum modelo com o código informado for encontrado
     * @throws ConnectionFailedException se ocorrer falha na conexão ou execução do SQL
     */
    public Modelo findByCodigo(int codigo) {
        String sql = "SELECT * FROM Modelo WHERE codigo = ?";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, codigo);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Modelo(
                            rs.getInt("codigo"),
                            rs.getString("nome"),
                            rs.getBigDecimal("volume"),
                            rs.getInt("ano")
                    );
                } else {
                    throw new NotFoundException("Modelo", codigo);
                }
            }

        } catch (SQLException e) {
            throw new ConnectionFailedException("Erro ao buscar o modelo pelo código.", e);
        }
    }

    /**
     * Lista todos os modelos cadastrados no banco de dados.
     *
     * @return uma {@link List} com todos os {@link Modelo} encontrados;
     *         lista vazia caso não existam registros
     * @throws ConnectionFailedException se ocorrer falha na conexão ou execução do SQL
     */
    public List<Modelo> findAll() {
        String sql = "SELECT * FROM Modelo";
        List<Modelo> lista = new ArrayList<>();

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(new Modelo(
                        rs.getInt("codigo"),
                        rs.getString("nome"),
                        rs.getBigDecimal("volume"),
                        rs.getInt("ano")
                ));
            }

        } catch (SQLException e) {
            throw new ConnectionFailedException("Erro ao listar os modelos.", e);
        }

        return lista;
    }

    /**
     * Remove um modelo do banco de dados utilizando o seu código identificador.
     *
     * @param codigo Código (chave primária) do modelo a ser deletado
     * @throws NotFoundException se nenhum modelo com o código informado for encontrado
     * @throws ConnectionFailedException se ocorrer falha na conexão ou execução do SQL
     */
    public void delete(int codigo) {
        String sql = "DELETE FROM Modelo WHERE codigo = ?";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, codigo);

            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas == 0) {
                throw new NotFoundException("Modelo", codigo);
            }

        } catch (SQLException e) {
            throw new ConnectionFailedException("Erro ao deletar o modelo do banco de dados.", e);
        }
    }
}