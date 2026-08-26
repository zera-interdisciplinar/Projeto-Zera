package br.com.zera.dao;

import br.com.zera.exception.*;
import br.com.zera.model.Gestor;
import br.com.zera.model.Unidade;
import br.com.zera.util.Conexao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe DAO responsável pelas operações de acesso ao banco de dados
 * para a entidade {@link Gestor}.
 *
 * @author Mayte B
 */
public class GestorDAO {

    /**
     * Adiciona um Gestor da empresa no banco de dados.
     *
     * @param gestor Objeto {@link Gestor} contendo os dados a serem atualizados
     * @throws ConnectionFailedException se ocorrer falha de conexão ou execução sql
     */
    public void insert(Gestor gestor) {
        String sql = "insert into gestor values(?, ?, ?, ?, ?, ?)";

        try(Connection conn = Conexao.getConexao();
        PreparedStatement pstm = conn.prepareStatement(sql)){

            pstm.setInt(1, gestor.getCodigo());
            pstm.setString(2, gestor.getNome());
            pstm.setString(3, gestor.getEmail());
            pstm.setString(4, gestor.getSenha());
            pstm.setString(5, gestor.getTelefone());
            pstm.setInt(6, gestor.getCodUnidade());

            ResultSet rs = pstm.executeQuery();
            while(rs.next()){
                int retorno = pstm.executeUpdate();
            }

        } catch (SQLException sqle){
            throw new ConnectionFailedException(sqle.getMessage());
        }
    }

    /**
     * Adiciona um Gestor ao banco de dados.
     *
     * @param gestor Objeto {@link Gestor} contendo os dados a serem atualizados
     * @throws ConnectionFailedException se ocorrer falha de conexão ou execução sql
     */
    public void update(Gestor gestor) {
        String sql = "update Unidade set codigo = ?, nome =  ?, email = ?, senha = ?, telefone = ?, cod_unidade = ? where codigo = ?";

        try(Connection conn = Conexao.getConexao();
            PreparedStatement pstm = conn.prepareStatement(sql)){

            pstm.setInt(1, gestor.getCodigo());
            pstm.setString(2, gestor.getNome());
            pstm.setString(3, gestor.getEmail());
            pstm.setString(4, gestor.getSenha());
            pstm.setString(5, gestor.getTelefone());
            pstm.setInt(6, gestor.getCodUnidade());
            pstm.setInt(7, gestor.getCodigo());

            ResultSet rs = pstm.executeQuery();
            while(rs.next()){
                int retorno = pstm.executeUpdate();
            }
        }catch(SQLException sqle){
            throw new ConnectionFailedException(sqle.getMessage());
        }
    }

    /**
     * Consulta gestores no banco de dados
     *
     * @param gestor Codigo (Primary Key) da organização procurada
     *
     * @return o {@link Gestor} correspondente ao código
     * @throws NotFoundException() para informações não encontradas
     * @throws ConnectionFailedException() para erros de conexão com o banco
     */
    public Gestor findByCodigo(Gestor gestor){
        String sql = "select * from Gestor where codigo = ?";

        try(Connection conn = Conexao.getConexao();
            PreparedStatement pstm = conn.prepareStatement(sql)){

            pstm.setInt(1, gestor.getCodigo());

            try {
                ResultSet rs = pstm.executeQuery();
                if (rs.next()) {
                    return new Gestor(
                            rs.getInt("codigo"),
                            rs.getString("nome"),
                            rs.getString("email"),
                            rs.getString("senha"),
                            rs.getString("telefone"),
                            rs.getInt("cod_unidade")
                    );
                } else {
                    throw new NotFoundException("Nenhum registro encontrado", gestor.getCodigo());
                }
            } catch (SQLException sqle) {
                throw new ConnectionFailedException(sqle.getMessage());
            }
        } catch (SQLException sqle){
            throw new ConnectionFailedException(sqle.getMessage());
        }
    }

    /**
     * Consulta todos os registros da tabela Gestor
     *
     * @throws NotFoundException() para retornos vazios no retorno de dados
     * @throws ConnectionFailedException() para erros de conexão com o banco
     * @return uma {@link List} com todas as {@link Gestor} encontradas;
     */
    public List<Gestor> findAll(){
        String sql = "select * from Unidade";
        List<Gestor> gestores = new ArrayList<>();

        try(Connection conn = Conexao.getConexao();
            PreparedStatement pstm = conn.prepareStatement(sql)){

            try(ResultSet rs = pstm.executeQuery()){
                if (rs.next()) {

                    gestores.add(new Gestor(
                            rs.getInt("codigo"),
                            rs.getString("nome"),
                            rs.getString("email"),
                            rs.getString("senha"),
                            rs.getString("telefone"),
                            rs.getInt("cod_unidade")
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
        return gestores;
    }

    /**
     * Apaga registros do gestor no banco de dados baseado no código
     *
     * @param gestor Objeto {@link Gestor} contendo os dados a serem excluídos
     * @throws ConnectionFailedException se ocorrer falha de conexão ou execução sql
     */
    public void delete(Gestor gestor) {
        String sql = "delete from Gestor where codigo = ?";

        try(Connection conn = Conexao.getConexao();
            PreparedStatement pstm = conn.prepareStatement(sql)){

            pstm.setInt(1, gestor.getCodigo());

            ResultSet rs = pstm.executeQuery();
            while(rs.next()){
                int retorno = pstm.executeUpdate();
            }
        }catch(SQLException sqle){
            throw new ConnectionFailedException(sqle.getMessage());
        }
    }
}
