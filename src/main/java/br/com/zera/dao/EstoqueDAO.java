package br.com.zera.dao;

import br.com.zera.exception.ConnectionFailedException;
import br.com.zera.exception.NotFoundException;
import br.com.zera.model.Estoque;
import br.com.zera.util.Conexao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
     * Insere um estoque.
     *
     * @param estoque objeto a ser cadastrado
     * @throws ConnectionFailedException em caso de falha no banco
     */
    public void insert(Estoque estoque) {
        String sql = "INSERT INTO estoque (espaco_na_unidade, cod_responsavel, cod_unidade) VALUES (?, ?, ?)";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, estoque.getEspacoNaUnidade());
            stmt.setInt(2, estoque.getCodResponsavel());
            stmt.setInt(3, estoque.getCodUnidade());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new ConnectionFailedException("Erro ao cadastrar estoque.", e);
        }
    }

    /**
     * Atualiza um estoque pelo código.
     *
     * @param estoque dados atualizados
     * @throws NotFoundException se não houver registro
     * @throws ConnectionFailedException em caso de falha no banco
     */
    public void update(Estoque estoque) {
        String sql = "UPDATE estoque SET espaco_na_unidade = ?, cod_responsavel = ?, cod_unidade = ? WHERE codigo = ?";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, estoque.getEspacoNaUnidade());
            stmt.setInt(2, estoque.getCodResponsavel());
            stmt.setInt(3, estoque.getCodUnidade());
            stmt.setInt(4, estoque.getCodigo());
            if (stmt.executeUpdate() == 0) {
                throw new NotFoundException("Estoque", estoque.getCodigo());
            }
        } catch (SQLException e) {
            throw new ConnectionFailedException("Erro ao atualizar estoque.", e);
        }
    }

    /**
     * Atualiza um estoque somente quando pertence à unidade informada.
     *
     * @param estoque dados atualizados
     * @param codUnidade unidade autorizada
     * @throws NotFoundException se não houver registro correspondente
     * @throws ConnectionFailedException em caso de falha no banco
     */
    public void updateByUnidade(Estoque estoque, int codUnidade) {
        String sql = "UPDATE estoque SET espaco_na_unidade = ?, cod_responsavel = ? WHERE codigo = ? AND cod_unidade = ?";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, estoque.getEspacoNaUnidade());
            stmt.setInt(2, estoque.getCodResponsavel());
            stmt.setInt(3, estoque.getCodigo());
            stmt.setInt(4, codUnidade);
            if (stmt.executeUpdate() == 0) {
                throw new NotFoundException("Estoque", estoque.getCodigo());
            }
        } catch (SQLException e) {
            throw new ConnectionFailedException("Erro ao atualizar estoque da unidade.", e);
        }
    }

    /**
     * Busca um estoque pelo código.
     *
     * @param codigo identificador do estoque
     * @return estoque encontrado
     * @throws NotFoundException se não houver registro
     * @throws ConnectionFailedException em caso de falha no banco
     */
    public Estoque findByCodigo(int codigo) {
        String sql = "SELECT * FROM estoque WHERE codigo = ?";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, codigo);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Estoque(rs.getInt("codigo"), rs.getString("espaco_na_unidade"),
                            rs.getInt("cod_responsavel"), rs.getInt("cod_unidade"));
                }
                throw new NotFoundException("Estoque", codigo);
            }
        } catch (SQLException e) {
            throw new ConnectionFailedException("Erro ao buscar estoque.", e);
        }
    }

    /**
     * Lista todos os estoques.
     *
     * @return lista de estoques
     * @throws ConnectionFailedException em caso de falha no banco
     */
    public List<Estoque> findAll() {
        String sql = "SELECT * FROM estoque ORDER BY codigo";
        List<Estoque> lista = new ArrayList<>();
        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(new Estoque(rs.getInt("codigo"), rs.getString("espaco_na_unidade"),
                        rs.getInt("cod_responsavel"), rs.getInt("cod_unidade")));
            }
        } catch (SQLException e) {
            throw new ConnectionFailedException("Erro ao listar estoques.", e);
        }
        return lista;
    }

    /**
     * Consulta estoques com filtros opcionais e combináveis.
     *
     * @param busca código ou parte do espaço
     * @param codUnidade unidade selecionada
     * @param codResponsavel responsável selecionado
     * @param codOrganizacao organização selecionada
     * @return estoques correspondentes
     * @throws ConnectionFailedException em caso de falha no banco
     */
    public List<Estoque> findByFiltros(String busca, Integer codUnidade,
                                       Integer codResponsavel, Integer codOrganizacao) {
        boolean temBusca = busca != null && !busca.isBlank();
        StringBuilder sql = new StringBuilder(
                "SELECT e.* FROM estoque e INNER JOIN unidade u ON u.codigo = e.cod_unidade WHERE 1=1");

        if (temBusca) sql.append(" AND (CAST(e.codigo AS TEXT) = ? OR e.espaco_na_unidade ILIKE ?)");
        if (codUnidade != null) sql.append(" AND e.cod_unidade = ?");
        if (codResponsavel != null) sql.append(" AND e.cod_responsavel = ?");
        if (codOrganizacao != null) sql.append(" AND u.cod_organizacao = ?");
        sql.append(" ORDER BY e.codigo");

        List<Estoque> lista = new ArrayList<>();
        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            int indice = 1;
            if (temBusca) {
                stmt.setString(indice++, busca.trim());
                stmt.setString(indice++, "%" + busca.trim() + "%");
            }
            if (codUnidade != null) stmt.setInt(indice++, codUnidade);
            if (codResponsavel != null) stmt.setInt(indice++, codResponsavel);
            if (codOrganizacao != null) stmt.setInt(indice, codOrganizacao);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Estoque(rs.getInt("codigo"), rs.getString("espaco_na_unidade"),
                            rs.getInt("cod_responsavel"), rs.getInt("cod_unidade")));
                }
            }
        } catch (SQLException e) {
            throw new ConnectionFailedException("Erro ao filtrar estoques.", e);
        }
        return lista;
    }

    /**
     * Consulta estoques de uma unidade por código ou espaço.
     *
     * @param codUnidade unidade do gestor
     * @param busca texto pesquisado
     * @return estoques encontrados
     * @throws ConnectionFailedException em caso de falha no banco
     */
    public List<Estoque> findByUnidade(int codUnidade, String busca) {
        return findByFiltros(busca, codUnidade, null, null);
    }

    /**
     * Consulta estoques de uma unidade com dois filtros combináveis.
     *
     * @param codUnidade unidade do gestor
     * @param busca código ou espaço
     * @param codResponsavel responsável selecionado
     * @return estoques encontrados
     * @throws ConnectionFailedException em caso de falha no banco
     */
    public List<Estoque> findByUnidade(int codUnidade, String busca, Integer codResponsavel) {
        return findByFiltros(busca, codUnidade, codResponsavel, null);
    }

    /**
     * Exclui um estoque pelo código.
     *
     * @param codigo identificador do estoque
     * @throws NotFoundException se não houver registro
     * @throws ConnectionFailedException em caso de falha no banco
     */
    public void delete(int codigo) {
        String sql = "DELETE FROM estoque WHERE codigo = ?";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, codigo);
            if (stmt.executeUpdate() == 0) {
                throw new NotFoundException("Estoque", codigo);
            }
        } catch (SQLException e) {
            throw new ConnectionFailedException("Erro ao excluir estoque.", e);
        }
    }

    /**
     * Exclui um estoque somente quando pertence à unidade indicada.
     *
     * @param codigo identificador do estoque
     * @param codUnidade unidade autorizada
     * @throws NotFoundException se não houver registro correspondente
     * @throws ConnectionFailedException em caso de falha no banco
     */
    public void deleteByUnidade(int codigo, int codUnidade) {
        String sql = "DELETE FROM estoque WHERE codigo = ? AND cod_unidade = ?";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, codigo);
            stmt.setInt(2, codUnidade);
            if (stmt.executeUpdate() == 0) {
                throw new NotFoundException("Estoque", codigo);
            }
        } catch (SQLException e) {
            throw new ConnectionFailedException("Erro ao excluir estoque da unidade.", e);
        }
    }
}