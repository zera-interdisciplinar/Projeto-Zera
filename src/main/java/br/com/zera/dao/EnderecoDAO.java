package br.com.zera.dao;

import br.com.zera.exception.ConnectionFailedException;
import br.com.zera.exception.NotFoundException;
import br.com.zera.model.Endereco;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe DAO responsável pelas operações de acesso ao banco de dados
 * para a entidade {@link Endereco}.
 *
 * @author Pedro Rufino
 */
public class EnderecoDAO {

    /**
     * Insere um novo endereço no banco de dados.
     *
     * @param endereco Objeto {@link Endereco} contendo os dados a serem persistidos
     * @throws ConnectionFailedException se ocorrer falha na conexão ou execução do SQL
     */
    public void insert(Endereco endereco) {
        String sql = "INSERT INTO Endereco (logradouro, numero, cep, cod_unidade, cidade, bairro, estado) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, endereco.getLogradouro());
            stmt.setInt(2, endereco.getNumero());
            stmt.setString(3, endereco.getCep());
            stmt.setInt(4, endereco.getCodUnidade());
            stmt.setString(5, endereco.getCidade());
            stmt.setString(6, endereco.getBairro());
            stmt.setString(7, endereco.getEstado());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new ConnectionFailedException("Erro ao cadastrar o endereço no banco de dados.", e);
        }
    }

    /**
     * Atualiza os dados de um endereço já existente no banco de dados,
     * identificado pelo seu código.
     *
     * @param endereco Objeto {@link Endereco} com os dados atualizados (o código deve estar preenchido)
     * @throws NotFoundException se nenhum endereço com o código informado for encontrado
     * @throws ConnectionFailedException se ocorrer falha na conexão ou execução do SQL
     */
    public void update(Endereco endereco) {
        String sql = "UPDATE Endereco SET logradouro = ?, numero = ?, cep = ?, cod_unidade = ?, cidade = ?, bairro = ?, estado = ? WHERE codigo = ?";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, endereco.getLogradouro());
            stmt.setInt(2, endereco.getNumero());
            stmt.setString(3, endereco.getCep());
            stmt.setInt(4, endereco.getCodUnidade());
            stmt.setString(5, endereco.getCidade());
            stmt.setString(6, endereco.getBairro());
            stmt.setString(7, endereco.getEstado());
            stmt.setInt(8, endereco.getCodigo());

            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas == 0) {
                throw new NotFoundException("Endereço", endereco.getCodigo());
            }

        } catch (SQLException e) {
            throw new ConnectionFailedException("Erro ao atualizar os dados do endereço.", e);
        }
    }

    /**
     * Busca um endereço no banco de dados a partir do seu código identificador.
     *
     * @param codigo Código (chave primária) do endereço a ser buscado
     * @return o {@link Endereco} correspondente ao código informado
     * @throws NotFoundException se nenhum endereço com o código informado for encontrado
     * @throws ConnectionFailedException se ocorrer falha na conexão ou execução do SQL
     */
    public Endereco findByCodigo(int codigo) {
        String sql = "SELECT * FROM Endereco WHERE codigo = ?";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, codigo);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Endereco(
                            rs.getInt("codigo"),
                            rs.getString("logradouro"),
                            rs.getInt("numero"),
                            rs.getString("cep"),
                            rs.getInt("cod_unidade"),
                            rs.getString("cidade"),
                            rs.getString("bairro"),
                            rs.getString("estado")
                    );
                } else {
                    throw new NotFoundException("Endereco", codigo);
                }
            }

        } catch (SQLException e) {
            throw new ConnectionFailedException("Erro ao buscar o endereço pelo código.", e);
        }
    }

    /**
     * Lista todos os endereços cadastrados no banco de dados.
     *
     * @return uma {@link List} com todos os {@link Endereco} encontrados;
     *         lista vazia caso não existam registros
     * @throws ConnectionFailedException se ocorrer falha na conexão ou execução do SQL
     */
    public List<Endereco> findAll() {
        String sql = "SELECT * FROM Endereco";
        List<Endereco> lista = new ArrayList<>();

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Endereco endereco = new Endereco(
                        rs.getInt("codigo"),
                        rs.getString("logradouro"),
                        rs.getInt("numero"),
                        rs.getString("cep"),
                        rs.getInt("cod_unidade"),
                        rs.getString("cidade"),
                        rs.getString("bairro"),
                        rs.getString("estado")
                );
                lista.add(endereco);
            }

        } catch (SQLException e) {
            throw new ConnectionFailedException("Erro ao listar o endereço.", e);
        }

        return lista;
    }

    /**
     * Remove um endereço do banco de dados utilizando o seu código identificador.
     *
     * @param codigo Código (chave primária) do endereço a ser deletado
     * @throws NotFoundException se nenhum endereço com o código informado for encontrado
     * @throws ConnectionFailedException se ocorrer falha na conexão ou execução do SQL
     */
    public void delete(int codigo) {
        String sql = "DELETE FROM Endereco WHERE codigo = ?";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, codigo);

            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas == 0) {
                throw new NotFoundException("Endereço", codigo);
            }

        } catch (SQLException e) {
            throw new ConnectionFailedException("Erro ao deletar o endereço do banco de dados.", e);
        }
    }
}