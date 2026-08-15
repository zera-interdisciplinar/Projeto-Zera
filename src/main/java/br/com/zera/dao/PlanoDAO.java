package br.com.zera.dao;

import br.com.zera.exception.ConnectionFailedException;
import br.com.zera.exception.NotFoundException;
import br.com.zera.model.Plano;
import br.com.zera.util.Conexao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe DAO responsável pelas operações de acesso ao banco de dados
 * para a entidade {@link Plano}.
 *
 * @author Pedro Rufino
 */
public class PlanoDAO {

    /**
     * Insere um novo Plano no banco de dados.
     *
     * @param plano Objeto {@link Plano} contendo os dados a serem persistidos
     * @throws ConnectionFailedException se ocorrer falha na conexão ou execução do SQL
     */
    public void insert(Plano plano) {
        String sql = "INSERT INTO Plano (valor, nome, tempo) VALUES (?, ?, ?)";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setBigDecimal(1, plano.getValor());
            stmt.setString(2, plano.getNome());
            stmt.setObject(3, plano.getTempo());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new ConnectionFailedException("Erro ao cadastrar o plano no banco de dados.", e);
        }
    }

    /**
     * Atualiza os dados de um plano já existente no banco de dados,
     * identificado pelo seu código.
     *
     * @param plano Objeto {@link Plano} com os dados atualizados
     * @throws NotFoundException se nenhum plano com o código informado for encontrado
     * @throws ConnectionFailedException se ocorrer falha na conexão ou execução do SQL
     */
    public void update(Plano plano) {
        String sql = "UPDATE Plano SET valor = ?, nome = ?, tempo = ? WHERE codigo = ?";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setBigDecimal(1, plano.getValor());
            stmt.setString(2, plano.getNome());
            stmt.setObject(3, plano.getTempo());
            stmt.setInt(4, plano.getCodigo()); // CORRIGIDO: Faltava enviar o código do WHERE!

            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas == 0) {
                throw new NotFoundException("Plano", plano.getCodigo());
            }

        } catch (SQLException e) {
            throw new ConnectionFailedException("Erro ao atualizar os dados do plano.", e);
        }
    }

    /**
     * Busca um plano no banco de dados a partir do seu código identificador.
     *
     * @param codigo Código (chave primária) do plano a ser buscado
     * @return o {@link Plano} correspondente ao código informado
     * @throws NotFoundException se nenhum plano com o código informado for encontrado
     * @throws ConnectionFailedException se ocorrer falha na conexão ou execução do SQL
     */
    public Plano findByCodigo(int codigo) {
        String sql = "SELECT * FROM Plano WHERE codigo = ?";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, codigo);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Plano(
                            rs.getInt("codigo"),
                            rs.getBigDecimal("valor"),
                            rs.getString("nome"),
                            rs.getObject("tempo", LocalDate.class)
                    );
                } else {
                    throw new NotFoundException("Plano", codigo);
                }
            }

        } catch (SQLException e) {
            throw new ConnectionFailedException("Erro ao buscar o plano pelo código.", e);
        }
    }

    /**
     * Lista todos os planos cadastrados no banco de dados.
     *
     * @return uma {@link List} com todos os {@link Plano} encontrados;
     * @throws ConnectionFailedException se ocorrer falha na conexão ou execução do SQL
     */
    public List<Plano> findAll() {
        String sql = "SELECT * FROM Plano";
        List<Plano> lista = new ArrayList<>();

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Plano plano = new Plano(
                        rs.getInt("codigo"),
                        rs.getBigDecimal("valor"),
                        rs.getString("nome"),
                        rs.getObject("tempo", LocalDate.class)
                );
                lista.add(plano);
            }

        } catch (SQLException e) {
            throw new ConnectionFailedException("Erro ao listar os planos.", e);
        }

        return lista;
    }

    /**
     * Remove um plano do banco de dados utilizando o seu código identificador.
     *
     * @param codigo Código (chave primária) do plano a ser deletado
     * @throws NotFoundException se nenhum plano com o código informado for encontrado
     * @throws ConnectionFailedException se ocorrer falha na conexão ou execução do SQL
     */
    public void delete(int codigo) {
        String sql = "DELETE FROM Plano WHERE codigo = ?";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, codigo);

            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas == 0) {
                throw new NotFoundException("Plano", codigo);
            }

        } catch (SQLException e) {
            throw new ConnectionFailedException("Erro ao deletar o plano do banco de dados.", e);
        }
    }
}