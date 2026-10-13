package br.com.zera.dao;

import br.com.zera.exception.ConnectionFailedException;
import br.com.zera.exception.NotFoundException;
import br.com.zera.model.Estoque;
import br.com.zera.util.Conexao;

import java.sql.*;
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
     * Insere um novo item de estoque no banco de dados.
     *
     * @param estoque Objeto {@link Estoque} contendo os dados a serem persistidos
     * @throws ConnectionFailedException se ocorrer falha na conexão ou execução do SQL
     */
    public void insert(Estoque estoque) {
        String sql = "INSERT INTO estoque (espaco_na_unidade, cod_responsavel, cod_unidade) VALUES (?, ?, ?)";

        try(Connection conn = Conexao.getConexao();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, estoque.getEspacoNaUnidade());
            pstmt.setInt(2, estoque.getCodResponsavel());
            pstmt.setInt(3, estoque.getCodUnidade());

            pstmt.executeUpdate();
        } catch (SQLException sqle) {
            throw new ConnectionFailedException(sqle.getMessage());

        }

    }

    /**
     * Atualiza registros de um estoque no banco de dados.
     *
     * @param estoque Objeto {@link Estoque} contendo os dados a serem atualizados
     * @throws NotFoundException se não existir estoque com o código informado
     * @throws ConnectionFailedException se ocorrer falha de conexão ou execução sql
     */
    public void update(Estoque estoque) {
        String sql = "UPDATE estoque SET espaco_na_unidade = ?, cod_responsavel = ?, cod_unidade = ? WHERE codigo = ? ";

        try(Connection conn = Conexao.getConexao();
            PreparedStatement psmt = conn.prepareStatement(sql)){

            psmt.setString(1, estoque.getEspacoNaUnidade());
            psmt.setInt(2, estoque.getCodResponsavel());
            psmt.setInt(3, estoque.getCodUnidade());
            psmt.setInt(4, estoque.getCodigo());

            if (psmt.executeUpdate() == 0) {
                throw new NotFoundException("Estoque", estoque.getCodigo());
            }
        } catch (SQLException sqle){
            throw new ConnectionFailedException(sqle.getMessage());
        }
    }

    /**
     * Consulta registros de estoque no banco de dados
     *
     * @param codigo Codigo (Primary Key) da organização procurada
     *
     * @return o {@link Estoque} correspondente ao código
     * @throws NotFoundException() para informações não encontradas
     * @throws ConnectionFailedException() para erros de conexão com o banco
     */
    public Estoque findByCodigo(int codigo) {
        String sql = "SELECT * FROM estoque WHERE codigo = ?";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement psmt = conn.prepareStatement(sql)) {

            psmt.setInt(1, codigo);

            try (ResultSet rs = psmt.executeQuery()) {
                if (rs.next()) {
                    return new Estoque(
                            rs.getInt("codigo"),
                            rs.getString("espaco_na_unidade"),
                            rs.getInt("cod_responsavel"),
                            rs.getInt("cod_unidade")
                    );
                }
                throw new NotFoundException("Estoque", codigo);
            }
        } catch (SQLException sqle) {
            throw new ConnectionFailedException(sqle.getMessage());
        }
    }

    /**
     * Consulta todos os registros da tabela Estoque
     *
     * @throws NotFoundException() para retornos vazios no retorno de dados
     * @throws ConnectionFailedException() para erros de conexão com o banco
     * @return uma {@link List} com todas as {@link Estoque} encontradas;
     */
    public List<Estoque> findAll() {
        String sql = "SELECT * FROM estoque";
        List<Estoque> retorno = new ArrayList<>();

        try(Connection conn = Conexao.getConexao();
        PreparedStatement psmt =  conn.prepareStatement(sql);
        ResultSet rs = psmt.executeQuery()){
            while (rs.next()) {
                retorno.add(new Estoque(
                        rs.getInt("codigo"),
                        rs.getString("espaco_na_unidade"),
                        rs.getInt("cod_responsavel"),
                        rs.getInt("cod_unidade")
                ));
            }
        } catch(SQLException sqle){
            throw new ConnectionFailedException(sqle.getMessage());
        }
        return retorno;
    }

    /**
     * Apaga registros de estoque no banco de dados.
     *
     * @param codigo Código (chave primária) do estoque a ser deletado     * @throws ConnectionFailedException se ocorrer falha de conexão ou execução sql
     */
    public void delete(int codigo) {
        String sql = "DELETE FROM estoque WHERE codigo = ?";

        try(Connection conn = Conexao.getConexao();
            PreparedStatement psmt = conn.prepareStatement(sql)) {

            psmt.setInt(1, codigo);

            if (psmt.executeUpdate() == 0) {
                throw new NotFoundException("Estoque", codigo);
            }
        } catch (SQLException sqle){
            throw new ConnectionFailedException(sqle.getMessage());
        }
    }
}