package br.com.zera.dao;

import br.com.zera.exception.*;
import br.com.zera.model.Gestor;
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
     * Adiciona um Gestor no banco de dados.
     *
     * @param gestor Objeto {@link Gestor} contendo os dados a serem inseridos
     * @throws ConnectionFailedException se ocorrer falha de conexão ou execução sql
     */
    public void insert(Gestor gestor) {
        String sql = "insert into gestor (nome, email, senha, telefone, cod_unidade) values (?, ?, ?, ?, ?)";

        try(Connection conn = Conexao.getConexao();
            PreparedStatement pstm = conn.prepareStatement(sql)){

            pstm.setString(1, gestor.getNome());
            pstm.setString(2, gestor.getEmail());
            pstm.setString(3, gestor.getSenha());
            pstm.setString(4, gestor.getTelefone());
            pstm.setInt(5, gestor.getCodUnidade());

            pstm.executeUpdate();

        } catch (SQLException sqle){
            throw new ConnectionFailedException(sqle.getMessage());
        }
    }

    /**
     * Atualiza os dados de um Gestor já existente no banco de dados.
     *
     * @param gestor Objeto {@link Gestor} contendo os dados a serem atualizados
     * @throws ConnectionFailedException se ocorrer falha de conexão ou execução sql
     */
    public void update(Gestor gestor) {
        String sql = "update Gestor set nome = ?, email = ?, senha = ?, telefone = ?, cod_unidade = ? where codigo = ?";

        try(Connection conn = Conexao.getConexao();
            PreparedStatement pstm = conn.prepareStatement(sql)){

            pstm.setString(1, gestor.getNome());
            pstm.setString(2, gestor.getEmail());
            pstm.setString(3, gestor.getSenha());
            pstm.setString(4, gestor.getTelefone());
            pstm.setInt(5, gestor.getCodUnidade());
            pstm.setInt(6, gestor.getCodigo());

            pstm.executeUpdate();

        }catch(SQLException sqle){
            throw new ConnectionFailedException(sqle.getMessage());
        }
    }

    /**
     * Consulta um gestor no banco de dados pelo código.
     *
     * @param codigo Código (Primary Key) do gestor procurado
     *
     * @return o {@link Gestor} correspondente ao código
     * @throws NotFoundException() para informações não encontradas
     * @throws ConnectionFailedException() para erros de conexão com o banco
     */
    public Gestor findByCodigo(int codigo){
        String sql = "select * from Gestor where codigo = ?";

        try(Connection conn = Conexao.getConexao();
            PreparedStatement pstm = conn.prepareStatement(sql)){

            pstm.setInt(1, codigo);

            try (ResultSet rs = pstm.executeQuery()) {
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
                    throw new NotFoundException("Nenhum registro encontrado", codigo);
                }
            }
        } catch (SQLException sqle){
            throw new ConnectionFailedException(sqle.getMessage());
        }
    }

    /**
     * Consulta todos os registros da tabela Gestor.
     *
     * @throws ConnectionFailedException() para erros de conexão com o banco
     * @return uma {@link List} com todos os {@link Gestor} encontrados;
     */
    public List<Gestor> findAll(){
        String sql = "select * from Gestor";
        List<Gestor> gestores = new ArrayList<>();

        try(Connection conn = Conexao.getConexao();
            PreparedStatement pstm = conn.prepareStatement(sql);
            ResultSet rs = pstm.executeQuery()){

            while (rs.next()) {
                gestores.add(new Gestor(
                        rs.getInt("codigo"),
                        rs.getString("nome"),
                        rs.getString("email"),
                        rs.getString("senha"),
                        rs.getString("telefone"),
                        rs.getInt("cod_unidade")
                ));
            }

        } catch(SQLException sqle){
            throw new ConnectionFailedException(sqle.getMessage());
        }
        return gestores;
    }

    /**
     * Apaga um registro de gestor no banco de dados baseado no código.
     *
     * @param codigo Código (Primary Key) do gestor a ser excluído
     * @throws ConnectionFailedException se ocorrer falha de conexão ou execução sql
     */
    public void delete(int codigo) {
        String sql = "delete from Gestor where codigo = ?";

        try(Connection conn = Conexao.getConexao();
            PreparedStatement pstm = conn.prepareStatement(sql)){

            pstm.setInt(1, codigo);

            pstm.executeUpdate();

        }catch(SQLException sqle){
            throw new ConnectionFailedException(sqle.getMessage());
        }
    }
}