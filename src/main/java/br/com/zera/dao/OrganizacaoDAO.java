package br.com.zera.dao;

import br.com.zera.exception.ConnectionFailedException;
import br.com.zera.exception.NotFoundException;
import br.com.zera.model.Organizacao;
import br.com.zera.util.Conexao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
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
        String sql = "INSERT INTO Organizacao (cnpj, nome, email) VALUES (?, ?, ?)";

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
     * identificada pelo seu código. A coluna atualizado_em recebe a data e hora atuais.
     *
     * @param organizacao Objeto {@link Organizacao} com os dados atualizados
     * @throws NotFoundException se nenhuma organização com o código informado for encontrada
     * @throws ConnectionFailedException se ocorrer falha na conexão ou execução do SQL
     */
    public void update(Organizacao organizacao) {
        String sql = "UPDATE Organizacao SET cnpj = ?, nome = ?, email = ?, atualizado_em = CURRENT_TIMESTAMP WHERE codigo = ?";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, organizacao.getCnpj());
            stmt.setString(2, organizacao.getNome());
            stmt.setString(3, organizacao.getEmail());
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
                    return new Organizacao(
                            rs.getInt("codigo"),
                            rs.getString("cnpj"),
                            rs.getString("nome"),
                            rs.getString("email"),
                            lerDataHora(rs, "criado_em"),
                            lerDataHora(rs, "atualizado_em")
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
     * Busca uma organização no banco de dados a partir do seu CNPJ.
     *
     * @param cnpj CNPJ da organização a ser buscada
     * @return a {@link Organizacao} correspondente ao CNPJ informado
     * @throws NotFoundException se nenhuma organização com o CNPJ informado for encontrada
     * @throws ConnectionFailedException se ocorrer falha na conexão ou execução do SQL
     */
    public Organizacao findByCnpj(String cnpj) {
        String sql = "SELECT * FROM Organizacao WHERE cnpj = ?";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cnpj);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Organizacao(
                            rs.getInt("codigo"),
                            rs.getString("cnpj"),
                            rs.getString("nome"),
                            rs.getString("email"),
                            lerDataHora(rs, "criado_em"),
                            lerDataHora(rs, "atualizado_em")
                    );
                } else {
                    throw new NotFoundException("Organização com o CNPJ " + cnpj + " não foi encontrada no sistema.");
                }
            }

        } catch (SQLException e) {
            throw new ConnectionFailedException("Erro ao buscar a organização pelo CNPJ.", e);
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
                lista.add(new Organizacao(
                        rs.getInt("codigo"),
                        rs.getString("cnpj"),
                        rs.getString("nome"),
                        rs.getString("email"),
                        lerDataHora(rs, "criado_em"),
                        lerDataHora(rs, "atualizado_em")
                ));
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
     * Lê uma coluna timestamp with time zone e converte para LocalDateTime
     * no fuso do servidor.
     *
     * @param rs resultado da consulta, já posicionado na linha
     * @param coluna nome da coluna de data e hora
     * @return a data e hora convertida, ou null se a coluna estiver vazia
     * @throws SQLException se a coluna não existir ou não puder ser lida
     */
    private LocalDateTime lerDataHora(ResultSet rs, String coluna) throws SQLException {
        OffsetDateTime valor = rs.getObject(coluna, OffsetDateTime.class);
        // atualizado_em pode estar vazio se o registro nunca foi alterado
        if (valor == null) {
            return null;
        }
        return valor.atZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();
    }
}