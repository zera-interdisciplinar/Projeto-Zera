package br.com.zera.dao;

import br.com.zera.exception.*;
import br.com.zera.model.Estoque;
import br.com.zera.model.Unidade;
import br.com.zera.util.Conexao;

import java.sql.*;

/**
 * Classe DAO responsável pelas operações de acesso ao banco de dados
 * para a entidade {@link Unidade}.
 *
 * @author Mayte B
 */
public class UnidadeDAO {

    public void insert(Unidade unidade) {
        String sql = "insert into unidade values(?, ?, ?, ?)";

        try(Connection conn = Conexao.getConexao();
            PreparedStatement pstm = conn.prepareStatement(){

                pstm.setInt(1,unidade.getCodigo());
                pstm.setString(2, unidade.getCnpj());
                pstm.setString(3, unidade.getEmail());
                pstm.setInt(4, unidade.getCodOrganizacao());

                ResultSet rs = pstm.executeQuery();
                while(rs.next()){
                    int retorno = pstm.executeUpdate();
                }
        } catch(){

        }
    }
}
