package br.com.zera.exception;

/**
 * Exceção lançada quando ocorre uma falha de conexão ou erro de execução SQL no banco de dados.
 * Encapsula erros do tipo {@link java.sql.SQLException}.
 */
public class ConnectionFailedException extends ZeraException {

    /**
     * Constrói a exceção informando uma mensagem de erro.
     *
     * @param message Descrição da falha de conexão ou execução.
     */
    public ConnectionFailedException(String message) {
        super(message);
    }

    /**
     * Constrói a exceção informando uma mensagem e a causa raiz no banco.
     *
     * @param message Descrição da falha.
     * @param cause Exceção original do SQL.
     */
    public ConnectionFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}