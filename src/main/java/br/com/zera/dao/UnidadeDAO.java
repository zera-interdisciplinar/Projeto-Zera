package br.com.zera.dao;

import br.com.zera.exception.*;
import br.com.zera.model.Unidade;
import br.com.zera.util.Conexao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe DAO responsável pelas operações de acesso ao banco de dados
 * para a entidade {@link Unidade}.
 *
 * @author Mayte B
 */
public class UnidadeDAO {

    /**
     * Insere nova unidade no banco de dados.
     *
     * @param unidade Objeto {@link Unidade} contendo as informações a serem persistidas
     * @throws ConnectionFailedException para caso de erro na conexão com o banco de dados
     */
    public void insert(Unidade unidade) {
        String sql = "insert into unidade (cnpj, email, cod_organizacao) values (?, ?, ?)";

        try(Connection conn = Conexao.getConexao();
            PreparedStatement pstm = conn.prepareStatement(sql)){

            pstm.setString(1, unidade.getCnpj());
            pstm.setString(2, unidade.getEmail());
            pstm.setInt(3, unidade.getCodOrganizacao());

            pstm.executeUpdate();

        } catch(SQLException sqle){
            throw new ConnectionFailedException(sqle.getMessage());
        }
    }

    /**
     * Atualiza a Unidade no banco de dados.
     *
     * @param unidade Objeto {@link Unidade} contendo os dados a serem atualizados
     * @throws ConnectionFailedException se ocorrer falha de conexão ou execução sql
     */
    public void update(Unidade unidade) {
        String sql = "update Unidade set cnpj = ?, email = ?, cod_organizacao = ? where codigo = ?";

        try(Connection conn = Conexao.getConexao();
            PreparedStatement pstm = conn.prepareStatement(sql)){

            pstm.setString(1, unidade.getCnpj());
            pstm.setString(2, unidade.getEmail());
            pstm.setInt(3, unidade.getCodOrganizacao());
            pstm.setInt(4, unidade.getCodigo());

            pstm.executeUpdate();

        }catch(SQLException sqle){
            throw new ConnectionFailedException(sqle.getMessage());
        }
    }

    /**
     * Consulta uma unidade no banco de dados pelo código.
     *
     * @param unidade objeto {@link Unidade} contendo o código a ser buscado
     *
     * @return o {@link Unidade} correspondente ao código
     * @throws NotFoundException() para informações não encontradas
     * @throws ConnectionFailedException() para erros de conexão com o banco
     */
    public Unidade findByCodigo(Unidade unidade){
        String sql = "select * from Unidade where codigo = ?";

        try(Connection conn = Conexao.getConexao();
            PreparedStatement pstm = conn.prepareStatement(sql)){

            pstm.setInt(1, unidade.getCodigo());

            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    Unidade encontrada = new Unidade(
                            rs.getInt("codigo"),
                            rs.getString("cnpj"),
                            rs.getString("email"),
                            rs.getInt("cod_organizacao")
                    );

                    Timestamp criadoEm = rs.getTimestamp("criado_em");
                    if (criadoEm != null) encontrada.setCriadoEm(criadoEm.toLocalDateTime());

                    Timestamp atualizadoEm = rs.getTimestamp("atualizado_em");
                    if (atualizadoEm != null) encontrada.setAtualizadoEm(atualizadoEm.toLocalDateTime());

                    return encontrada;
                } else {
                    throw new NotFoundException("Nenhum registro encontrado", unidade.getCodigo());
                }
            }
        } catch (SQLException sqle){
            throw new ConnectionFailedException(sqle.getMessage());
        }
    }

    /**
     * Consulta todos os registros da tabela Unidade.
     *
     * @throws ConnectionFailedException() para erros de conexão com o banco
     * @return uma {@link List} com todas as {@link Unidade} encontradas;
     */
    public List<Unidade> findAll(){
        String sql = "select * from Unidade";
        List<Unidade> unidades = new ArrayList<>();

        try(Connection conn = Conexao.getConexao();
            PreparedStatement pstm = conn.prepareStatement(sql);
            ResultSet rs = pstm.executeQuery()){

            while (rs.next()) {
                Unidade unidade = new Unidade(
                        rs.getInt("codigo"),
                        rs.getString("cnpj"),
                        rs.getString("email"),
                        rs.getInt("cod_organizacao")
                );

                Timestamp criadoEm = rs.getTimestamp("criado_em");
                if (criadoEm != null) unidade.setCriadoEm(criadoEm.toLocalDateTime());

                Timestamp atualizadoEm = rs.getTimestamp("atualizado_em");
                if (atualizadoEm != null) unidade.setAtualizadoEm(atualizadoEm.toLocalDateTime());

                unidades.add(unidade);
            }

        } catch(SQLException sqle){
            throw new ConnectionFailedException(sqle.getMessage());
        }
        return unidades;
    }

    /**
     * Apaga um registro de unidade no banco de dados.
     *
     * @param unidade Objeto {@link Unidade} contendo o código a ser excluído
     * @throws ConnectionFailedException se ocorrer falha de conexão ou execução sql
     */
    public void delete(Unidade unidade) {
        String sql = "delete from Unidade where codigo = ?";

        try(Connection conn = Conexao.getConexao();
            PreparedStatement pstm = conn.prepareStatement(sql)){

            pstm.setInt(1, unidade.getCodigo());

            pstm.executeUpdate();

        }catch(SQLException sqle){
            throw new ConnectionFailedException(sqle.getMessage());
        }
    }
}