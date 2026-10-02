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
     * @param codigo Código (Primary Key) da unidade procurada
     *
     * @return o {@link Unidade} correspondente ao código
     * @throws NotFoundException() para informações não encontradas
     * @throws ConnectionFailedException() para erros de conexão com o banco
     */
    public Unidade findByCodigo(int codigo){
        String sql = "select * from Unidade where codigo = ?";

        try(Connection conn = Conexao.getConexao();
            PreparedStatement pstm = conn.prepareStatement(sql)){

            pstm.setInt(1, codigo);

            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    return new Unidade(
                            rs.getInt("codigo"),
                            rs.getString("cnpj"),
                            rs.getString("email"),
                            rs.getInt("cod_organizacao")
                    );
                } else {
                    throw new NotFoundException("Nenhum registro encontrado", codigo);
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
                unidades.add(new Unidade(
                        rs.getInt("codigo"),
                        rs.getString("cnpj"),
                        rs.getString("email"),
                        rs.getInt("cod_organizacao")
                ));
            }

        } catch(SQLException sqle){
            throw new ConnectionFailedException(sqle.getMessage());
        }
        return unidades;
    }

    /**
     * Apaga um registro de unidade no banco de dados.
     *
     * @param codigo Código (Primary Key) da unidade a ser excluída
     * @throws ConnectionFailedException se ocorrer falha de conexão ou execução sql
     */
    public void delete(int codigo) {
        String sql = "delete from Unidade where codigo = ?";

        try(Connection conn = Conexao.getConexao();
            PreparedStatement pstm = conn.prepareStatement(sql)){

            pstm.setInt(1, codigo);

            pstm.executeUpdate();

        }catch(SQLException sqle){
            throw new ConnectionFailedException(sqle.getMessage());
        }
    }
}