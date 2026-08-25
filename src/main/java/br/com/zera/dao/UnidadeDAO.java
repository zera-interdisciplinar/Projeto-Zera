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
     * Insere novo registro de informações em {@link Unidade}
     *
     * @param unidade Objeto {@link Unidade} contendo as informações a serem persistidas
     * @throws ConnectionFailedException para caso de erro na conexão com o banco de dados
     */
    public void insert(Unidade unidade) {
        String sql = "insert into unidade values(?, ?, ?, ?)";

        try(Connection conn = Conexao.getConexao();
            PreparedStatement pstm = conn.prepareStatement(sql)){

                pstm.setInt(1,unidade.getCodigo());
                pstm.setString(2, unidade.getCnpj());
                pstm.setString(3, unidade.getEmail());
                pstm.setInt(4, unidade.getCodOrganizacao());

                ResultSet rs = pstm.executeQuery();
                while(rs.next()){
                    int retorno = pstm.executeUpdate();
                }
        } catch(SQLException sqle){
            throw new ConnectionFailedException(sqle.getMessage());
        }
    }

    /**
     * Atualiza a Unidade da empresa no banco de dados.
     *
     * @param unidade Objeto {@link Unidade} contendo os dados a serem atualizados
     * @throws ConnectionFailedException se ocorrer falha de conexão ou execução sql
     */
    public void update(Unidade unidade) {
        String sql = "update Unidade set codigo = ?, cnpj =  ?, email = ?, cod_organizacao = ? where codigo = ?";

        try(Connection conn = Conexao.getConexao();
        PreparedStatement pstm = conn.prepareStatement(sql)){

            pstm.setInt(1, unidade.getCodigo());
            pstm.setString(2, unidade.getCnpj());
            pstm.setString(3, unidade.getEmail());
            pstm.setInt(4, unidade.getCodOrganizacao());
            pstm.setInt(1, unidade.getCodigo());

            ResultSet rs = pstm.executeQuery();
            while(rs.next()){
                int retorno = pstm.executeUpdate();
            }
        }catch(SQLException sqle){
            throw new ConnectionFailedException(sqle.getMessage());
        }
    }

    /**
     * Consulta unidades no banco de dados
     *
     * @param unidade Codigo (Primary Key) da organização procurada
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

            try {
                ResultSet rs = pstm.executeQuery();
                if (rs.next()) {
                    return new Unidade(
                            rs.getInt("codigo"),
                            rs.getString("CNPJ"),
                            rs.getString("email"),
                            rs.getInt("cod_organizacao")
                    );
                } else {
                    throw new NotFoundException("Nenhum registro encontrado", unidade.getCodigo());
                }
            } catch (SQLException sqle) {
                throw new ConnectionFailedException(sqle.getMessage());
            }
        } catch (SQLException sqle){
            throw new ConnectionFailedException(sqle.getMessage());
        }
    }

    /**
     * Consulta todos os registros da tabela Unidade
     *
     * @throws NotFoundException() para retornos vazios no retorno de dados
     * @throws ConnectionFailedException() para erros de conexão com o banco
     * @return uma {@link List} com todas as {@link Unidade} encontradas;
     */
    public List<Unidade> findAll(){
        String sql = "select * from Unidade";
        List<Unidade> unidades = new ArrayList<>();

        try(Connection conn = Conexao.getConexao();
        PreparedStatement pstm = conn.prepareStatement(sql)){

            try(ResultSet rs = pstm.executeQuery()){
                if (rs.next()) {

                    unidades.add(new Unidade(
                            rs.getInt("codigo"),
                            rs.getString("Cnpj"),
                            rs.getString("Email"),
                            rs.getInt("cod_organizacao")
                    ));

                } else {
                    throw new NotFoundException("Nenhum item encontrado");
                }
            } catch(ConnectionFailedException cfe){
                throw new ConnectionFailedException(cfe.getMessage());
            }

        } catch(SQLException sqle){
            throw new ConnectionFailedException(sqle.getMessage());
        }
        return unidades;
    }

    /**
     * Apaga registros da unidade da empresa no banco de dados.
     *
     * @param unidade Objeto {@link Unidade} contendo os dados a serem excluídos
     * @throws ConnectionFailedException se ocorrer falha de conexão ou execução sql
     */
    public void delete(Unidade unidade) {
        String sql = "delete from Unidade where codigo = ?";

        try(Connection conn = Conexao.getConexao();
        PreparedStatement pstm = conn.prepareStatement(sql)){

            pstm.setInt(1, unidade.getCodigo());

            ResultSet rs = pstm.executeQuery();
            while(rs.next()){
                int retorno = pstm.executeUpdate();
            }
        }catch(SQLException sqle){
            throw new ConnectionFailedException(sqle.getMessage());
        }
    }
}
