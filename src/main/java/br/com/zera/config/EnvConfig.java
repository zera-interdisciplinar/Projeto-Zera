package br.com.zera.config;

import io.github.cdimascio.dotenv.Dotenv;

/**
 * Classe utilitária responsável por buscar variáveis de ambiente,
 * priorizando o arquivo .env (uso local) e usando as variáveis
 * do sistema operacional como alternativa (uso em produção).
 *
 * @author Pedro Rufino
 */
public class EnvConfig {

    private static final Dotenv dotenv;

    static {
        dotenv = Dotenv.configure().ignoreIfMissing().load();
    }

    /**
     * Busca o valor de uma variável de ambiente, priorizando o arquivo .env.
     *
     * @param chave Nome da variável de ambiente
     * @return o valor da variável, ou null se não encontrada em nenhuma das fontes
     */
    public static String getEnv(String chave) {
        String env = dotenv.get(chave);
        if (env == null) {
            return System.getenv(chave);
        }
        return env;
    }
}