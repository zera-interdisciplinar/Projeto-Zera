package br.com.zera.util;

import br.com.zera.config.EnvConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {

    private static final String URL;
    private static final String USER;
    private static final String PASSWORD;

    static {
        URL = EnvConfig.getEnv("DB_URL");
        USER = EnvConfig.getEnv("DB_USER");
        PASSWORD = EnvConfig.getEnv("DB_PASSWORD");

        if (URL == null || USER == null || PASSWORD == null) {
            throw new IllegalStateException("Variáveis DB_URL, DB_USER ou DB_PASSWORD não foram configuradas.");
        }

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Erro ao carregar driver PostgreSQL", e);
        }
    }

    public static Connection getConexao() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}