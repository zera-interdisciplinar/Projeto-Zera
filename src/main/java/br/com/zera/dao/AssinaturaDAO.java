package br.com.zera.dao;

import br.com.zera.exception.ConnectionFailedException;
import br.com.zera.exception.NotFoundException;
import br.com.zera.model.Assinatura;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe DAO responsável pelas operações de acesso ao banco de dados
 * para a entidade {@link Assinatura}.
 *
 * @author Pedro Rufino
 */
public class AssinaturaDAO {

    /**
     * Insere uma nova Assinatura no banco de dados.
     *
     * @param assinatura Objeto {@link Assinatura} contendo os dados a serem persistidos
     * @throws ConnectionFailedException se ocorrer falha na conexão ou execução do SQL
     */
    public void insert(Assinatura assinatura) {
        String sql = "INSERT INTO Assinatura (data_inicio, data_fim, status, cod_organizacao, cod_plano) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, assinatura.getDataInicio());
            stmt.setObject(2, assinatura.getDataFim());
            stmt.setString(3, assinatura.getStatus());
            stmt.setInt(4, assinatura.getCodOrganizacao());
            stmt.setInt(5, assinatura.getCodPlano());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new ConnectionFailedException("Erro ao cadastrar a assinatura no banco de dados.", e);
        }
    }

    /**
     * Atualiza os dados de uma assinatura já existente no banco de dados,
     * identificada pelo seu código.
     *
     * @param assinatura Objeto {@link Assinatura} com os dados atualizados
     * @throws NotFoundException se nenhuma assinatura com o código informado for encontrada
     * @throws ConnectionFailedException se ocorrer falha na conexão ou execução do SQL
     */
    public void update(Assinatura assinatura) {

        String sql = "UPDATE Assinatura SET data_inicio = ?, data_fim = ?, status = ?, cod_organizacao = ?, cod_plano = ? WHERE codigo = ?";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, assinatura.getDataInicio());
            stmt.setObject(2, assinatura.getDataFim());
            stmt.setString(3, assinatura.getStatus());
            stmt.setInt(4, assinatura.getCodOrganizacao());
            stmt.setInt(5, assinatura.getCodPlano());
            stmt.setInt(6, assinatura.getCodigo());

            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas == 0) {
                throw new NotFoundException("Assinatura", assinatura.getCodigo());
            }

        } catch (SQLException e) {
            throw new ConnectionFailedException("Erro ao atualizar os dados da assinatura.", e);
        }
    }

    /**
     * Busca uma assinatura no banco de dados a partir do seu código identificador.
     *
     * @param codigo Código (chave primária) da assinatura a ser buscada
     * @return a {@link Assinatura} correspondente ao código informado
     * @throws NotFoundException se nenhuma assinatura com o código informado for encontrada
     * @throws ConnectionFailedException se ocorrer falha na conexão ou execução do SQL
     */
    public Assinatura findByCodigo(int codigo) {
        String sql = "SELECT * FROM Assinatura WHERE codigo = ?";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, codigo);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Assinatura(
                            rs.getInt("codigo"),
                            rs.getObject("data_inicio", LocalDate.class),
                            rs.getObject("data_fim", LocalDate.class),
                            rs.getString("status"),
                            rs.getInt("cod_organizacao"),
                            rs.getInt("cod_plano")
                    );
                } else {
                    throw new NotFoundException("Assinatura", codigo);
                }
            }

        } catch (SQLException e) {
            throw new ConnectionFailedException("Erro ao buscar a assinatura pelo código.", e);
        }
    }

    /**
     * Lista todas as assinaturas cadastradas no banco de dados.
     *
     * @return uma {@link List} com todas as {@link Assinatura} encontradas;
     * @throws ConnectionFailedException se ocorrer falha na conexão ou execução do SQL
     */
    public List<Assinatura> findAll() {
        String sql = "SELECT * FROM Assinatura";
        List<Assinatura> lista = new ArrayList<>();

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Assinatura assinatura = new Assinatura(
                        rs.getInt("codigo"),
                        rs.getObject("data_inicio", LocalDate.class),
                        rs.getObject("data_fim", LocalDate.class),
                        rs.getString("status"),
                        rs.getInt("cod_organizacao"),
                        rs.getInt("cod_plano")
                );
                lista.add(assinatura);
            }

        } catch (SQLException e) {
            throw new ConnectionFailedException("Erro ao listar as assinaturas.", e);
        }

        return lista;
    }

    /**
     * Remove uma assinatura do banco de dados utilizando o seu código identificador.
     *
     * @param codigo Código (chave primária) da assinatura a ser deletada
     * @throws NotFoundException se nenhuma assinatura com o código informado for encontrada
     * @throws ConnectionFailedException se ocorrer falha na conexão ou execução do SQL
     */
    public void delete(int codigo) {
        String sql = "DELETE FROM Assinatura WHERE codigo = ?";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, codigo);

            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas == 0) {
                throw new NotFoundException("Assinatura", codigo);
            }

        } catch (SQLException e) {
            throw new ConnectionFailedException("Erro ao deletar a assinatura do banco de dados.", e);
        }
    }
}