package br.com.zera.util;

import br.com.zera.config.EnvConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe utilitária responsável por centralizar e fornecer conexões
 * ativas com o banco de dados PostgreSQL.
 * <p>
 * As credenciais de acesso são obtidas de forma segura através da classe {@link EnvConfig}.
 * </p>
 *
 * @author Pedro Rufino
 */
public class Conexao {

    private static final String URL;
    private static final String USER;
    private static final String PASSWORD;

    static {
        // Obtém as credenciais cadastradas nas variáveis de ambiente
        URL = EnvConfig.getEnv("DB_URL");
        USER = EnvConfig.getEnv("DB_USER");
        PASSWORD = EnvConfig.getEnv("DB_PASSWORD");

        // Valida se todas as credenciais essenciais foram carregadas
        if (URL == null || USER == null || PASSWORD == null) {
            throw new IllegalStateException("Variáveis DB_URL, DB_USER ou DB_PASSWORD não foram configuradas.");
        }

        try {
            // Registra o driver JDBC do PostgreSQL na memória
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Erro ao carregar driver PostgreSQL", e);
        }
    }

    /**
     * Cria e retorna uma nova conexão com o banco de dados.
     * Repassa a responsabilidade do tratamento de erros SQL para quem a invocar (os DAOs).
     *
     * @return uma instância ativa de {@link Connection}
     * @throws SQLException se ocorrer falha ao estabelecer a conexão com o banco
     */
    public static Connection getConexao() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}