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
     * Adiciona um Gestor da empresa no banco de dados.
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
     * @param gestor objeto {@link Gestor} contendo o código a ser buscado
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

            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    Gestor encontrado = new Gestor(
                            rs.getInt("codigo"),
                            rs.getString("nome"),
                            rs.getString("email"),
                            rs.getString("senha"),
                            rs.getString("telefone"),
                            rs.getInt("cod_unidade")
                    );

                    Timestamp criadoEm = rs.getTimestamp("criado_em");
                    if (criadoEm != null) encontrado.setCriadoEm(criadoEm.toLocalDateTime());

                    Timestamp atualizadoEm = rs.getTimestamp("atualizado_em");
                    if (atualizadoEm != null) encontrado.setAtualizadoEm(atualizadoEm.toLocalDateTime());

                    return encontrado;
                } else {
                    throw new NotFoundException("Nenhum registro encontrado", gestor.getCodigo());
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
                Gestor gestor = new Gestor(
                        rs.getInt("codigo"),
                        rs.getString("nome"),
                        rs.getString("email"),
                        rs.getString("senha"),
                        rs.getString("telefone"),
                        rs.getInt("cod_unidade")
                );

                Timestamp criadoEm = rs.getTimestamp("criado_em");
                if (criadoEm != null) gestor.setCriadoEm(criadoEm.toLocalDateTime());

                Timestamp atualizadoEm = rs.getTimestamp("atualizado_em");
                if (atualizadoEm != null) gestor.setAtualizadoEm(atualizadoEm.toLocalDateTime());

                gestores.add(gestor);
            }

        } catch(SQLException sqle){
            throw new ConnectionFailedException(sqle.getMessage());
        }
        return gestores;
    }

    /**
     * Apaga um registro de gestor no banco de dados baseado no código.
     *
     * @param gestor Objeto {@link Gestor} contendo o código a ser excluído
     * @throws ConnectionFailedException se ocorrer falha de conexão ou execução sql
     */
    public void delete(Gestor gestor) {
        String sql = "delete from Gestor where codigo = ?";

        try(Connection conn = Conexao.getConexao();
            PreparedStatement pstm = conn.prepareStatement(sql)){

            pstm.setInt(1, gestor.getCodigo());

            pstm.executeUpdate();

        }catch(SQLException sqle){
            throw new ConnectionFailedException(sqle.getMessage());
        }
    }
}