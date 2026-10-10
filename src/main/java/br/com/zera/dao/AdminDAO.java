package br.com.zera.dao;

import br.com.zera.exception.ConnectionFailedException;
import br.com.zera.exception.NotFoundException;
import br.com.zera.model.Admin;
import br.com.zera.util.Conexao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe DAO responsável pelas operações de acesso ao banco de dados
 * para a entidade {@link Admin}.
 *
 * @author Pedro Rufino
 */
public class AdminDAO {

    /**
     * Insere um novo Admin no banco de dados.
     *
     * @param admin Objeto {@link Admin} contendo os dados a serem persistidos
     * @throws ConnectionFailedException se ocorrer falha na conexão ou execução do SQL
     */
    public void insert(Admin admin) {
        String sql = "INSERT INTO Admin (nome, email, senha) VALUES (?, ?, ?)";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, admin.getNome());
            stmt.setString(2, admin.getEmail());
            stmt.setString(3, admin.getSenha());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new ConnectionFailedException("Erro ao cadastrar o admin no banco de dados.", e);
        }
    }

    /**
     * Atualiza o nome e o email de um admin já existente no banco de dados,
     * identificado pelo seu código. A senha não é alterada por este método.
     *
     * @param admin Objeto {@link Admin} com os dados atualizados
     * @throws NotFoundException se nenhum admin com o código informado for encontrado
     * @throws ConnectionFailedException se ocorrer falha na conexão ou execução do SQL
     */
    public void update(Admin admin) {
        String sql = "UPDATE Admin SET nome = ?, email = ? WHERE codigo = ?";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, admin.getNome());
            stmt.setString(2, admin.getEmail());
            stmt.setInt(3, admin.getCodigo());

            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas == 0) {
                throw new NotFoundException("Admin", admin.getCodigo());
            }

        } catch (SQLException e) {
            throw new ConnectionFailedException("Erro ao atualizar os dados do admin.", e);
        }
    }

    /**
     * Atualiza apenas a senha de um admin, identificado pelo seu código.
     *
     * @param codigo    Código (chave primária) do admin
     * @param novaSenha nova senha a ser gravada
     * @throws NotFoundException se nenhum admin com o código informado for encontrado
     * @throws ConnectionFailedException se ocorrer falha na conexão ou execução do SQL
     */
    public void updateSenha(int codigo, String novaSenha) {
        String sql = "UPDATE Admin SET senha = ? WHERE codigo = ?";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, novaSenha);
            stmt.setInt(2, codigo);

            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas == 0) {
                throw new NotFoundException("Admin", codigo);
            }

        } catch (SQLException e) {
            throw new ConnectionFailedException("Erro ao atualizar a senha do admin.", e);
        }
    }

    /**
     * Busca um admin no banco de dados a partir do seu código identificador.
     *
     * @param codigo Código (chave primária) do admin a ser buscado
     * @return o {@link Admin} correspondente ao código informado
     * @throws NotFoundException se nenhum admin com o código informado for encontrado
     * @throws ConnectionFailedException se ocorrer falha na conexão ou execução do SQL
     */
    public Admin findByCodigo(int codigo) {
        String sql = "SELECT * FROM Admin WHERE codigo = ?";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, codigo);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Admin(
                            rs.getInt("codigo"),
                            rs.getString("nome"),
                            rs.getString("email"),
                            rs.getString("senha")
                    );
                } else {
                    throw new NotFoundException("Admin", codigo);
                }
            }

        } catch (SQLException e) {
            throw new ConnectionFailedException("Erro ao buscar o admin pelo código.", e);
        }
    }

    /**
     * Verifica se já existe um admin cadastrado com o email informado.
     *
     * @param email email a ser verificado
     * @return true se o email já estiver cadastrado; false caso contrário
     * @throws ConnectionFailedException se ocorrer falha na conexão ou execução do SQL
     */
    public boolean existsByEmail(String email) {
        String sql = "SELECT 1 FROM Admin WHERE email = ?";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            throw new ConnectionFailedException("Erro ao verificar o email do admin.", e);
        }
    }

    /**
     * Lista todos os admins cadastrados no banco de dados.
     *
     * @return uma {@link List} com todos os {@link Admin} encontrados
     * @throws ConnectionFailedException se ocorrer falha na conexão ou execução do SQL
     */
    public List<Admin> findAll() {
        String sql = "SELECT * FROM Admin";
        List<Admin> lista = new ArrayList<>();

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Admin admin = new Admin(
                        rs.getInt("codigo"),
                        rs.getString("nome"),
                        rs.getString("email"),
                        rs.getString("senha")
                );
                lista.add(admin);
            }

        } catch (SQLException e) {
            throw new ConnectionFailedException("Erro ao listar os admins.", e);
        }

        return lista;
    }

    /**
     * Remove um admin do banco de dados utilizando o seu código identificador.
     *
     * @param codigo Código (chave primária) do admin a ser deletado
     * @throws NotFoundException se nenhum admin com o código informado for encontrado
     * @throws ConnectionFailedException se ocorrer falha na conexão ou execução do SQL
     */
    public void delete(int codigo) {
        String sql = "DELETE FROM Admin WHERE codigo = ?";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, codigo);

            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas == 0) {
                throw new NotFoundException("Admin", codigo);
            }

        } catch (SQLException e) {
            throw new ConnectionFailedException("Erro ao deletar o admin do banco de dados.", e);
        }
    }
}