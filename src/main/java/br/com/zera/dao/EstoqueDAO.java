package br.com.zera.dao;

import br.com.zera.exception.ConnectionFailedException;
import br.com.zera.exception.NotFoundException;
import br.com.zera.model.Estoque;
import br.com.zera.util.Conexao;

import java.sql.*;
import java.time.LocalDate;
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
        String sql = "insert into estoque (codigo, espaco_na_unidade, cod_responsavel, cod_unidade) values (?, ?, ?, ?)";

        try(Connection conn = Conexao.getConexao();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, estoque.getCodigo());

            pstmt.setString(2, estoque.getEspacoNaUnidade());

            pstmt.setInt(3, estoque.getCodResponsavel());
            pstmt.setInt(4, estoque.getCodUnidade());

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int retorno = pstmt.executeUpdate();
            }
        } catch (SQLException sqle) {
            throw new ConnectionFailedException(sqle.getMessage());

        }

    }
    /**
     * Atualiza registros de estoque no banco de dados.
     *
     * @param estoque Objeto {@link Estoque} contendo os dados a serem atualizados
     * @throws ConnectionFailedException se ocorrer falha de conexão ou execução sql
     */
    public void update(Estoque estoque) {
        String sql = "Update estoque set codigo = ?,  espaco_na_unidade = ?, cod_reponsavel = ?, cod_unidade = ? where codigo = ? ";

        try(Connection conn = Conexao.getConexao();
            PreparedStatement psmt = conn.prepareStatement(sql)){

            psmt.setInt(1, estoque.getCodigo());
            psmt.setString(2, estoque.getEspacoNaUnidade());
            psmt.setInt(3, estoque.getCodResponsavel());
            psmt.setInt(4, estoque.getCodUnidade());

            ResultSet rs = psmt.executeQuery();
            while(rs.next()) {
                int retorno = psmt.executeUpdate();
            }
        } catch (SQLException sqle){
            throw new ConnectionFailedException(sqle.getMessage());
        }
    }

    /**
     * Consulta registros de estoque no banco de dados
     *
     * @param estoque Codigo (Primary Key) da organização procurada
     *
     * @return o {@link Estoque} correspondente ao código
     * @throws NotFoundException() para informações não encontradas
     * @throws ConnectionFailedException() para erros de conexão com o banco
     */
    public Estoque findByCodigo(Estoque estoque) {
        String sql = "select * from estoque where codigo = ?";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement psmt = conn.prepareStatement(sql)) {

            psmt.setInt(1, estoque.getCodigo());

            try (ResultSet rs = psmt.executeQuery()) {
                if (rs.next()) {
                    return new Estoque(
                            rs.getInt("codigo"),
                            rs.getString("espaco_na_unidade"),
                            rs.getInt("cod_reponsavel"),
                            rs.getInt("cod_unidade")
                    );
                } else {
                    throw new NotFoundException("Registro não encontrado em Estoque", estoque.getCodigo());
                }
            } catch (SQLException sqle) {
                throw new ConnectionFailedException(sqle.getMessage());
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
        String sql = "select * from estoque";
        List<Estoque> retorno = new ArrayList<>();
        try(Connection conn = Conexao.getConexao();
        PreparedStatement psmt =  conn.prepareStatement(sql)){

            try(ResultSet rs = psmt.executeQuery()){
                if (rs.next()) {
                    Estoque estoque = new Estoque (
                            rs.getInt("codigo"),
                            rs.getString("espaco_na_unidade"),
                            rs.getInt("cod_reponsavel"),
                            rs.getInt("cod_unidade")
                    );
                    retorno.add(estoque);
                } else {
                    throw new NotFoundException("Nenhum registro encontrado");
                }
            } catch (ConnectionFailedException cfe){
                throw new ConnectionFailedException(cfe.getMessage());
            }
        } catch(SQLException sqle){
            throw new ConnectionFailedException(sqle.getMessage());
        }
        return retorno;
    }

    /**
     * Apaga registros de estoque no banco de dados.
     *
     * @param estoque Objeto {@link Estoque} contendo os dados a serem excluídos
     * @throws ConnectionFailedException se ocorrer falha de conexão ou execução sql
     */
    public void delete(Estoque estoque) {
        String sql = "delete from estoque where codigo = ?";

        try(Connection conn = Conexao.getConexao();
            PreparedStatement psmt = conn.prepareStatement(sql)) {

            psmt.setInt(1, estoque.getCodigo());

            ResultSet rs = psmt.executeQuery();
            while(rs.next()) {
                int retorno = psmt.executeUpdate();
            }
        } catch (SQLException sqle){
            throw new ConnectionFailedException(sqle.getMessage());
        }
    }
}