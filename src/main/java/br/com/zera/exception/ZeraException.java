package br.com.zera.exception;

/**
 * Exceção base (mãe) para todas as exceções personalizadas do sistema Zera.
 * Captura qualquer erro de regra de negócio ou infraestrutura do projeto.
 *
 * @author Pedro Rufino
 */
public class ZeraException extends RuntimeException {

    /**
     * Constrói a exceção informando uma mensagem de erro.
     *
     * @param message Detalhes sobre o erro ocorrido.
     */
    public ZeraException(String message) {
        super(message);
    }

    /**
     * Constrói a exceção informando uma mensagem e a causa original do erro.
     *
     * @param message Detalhes sobre o erro ocorrido.
     * @param cause A exceção original que gerou a falha (ex: SQLException).
     */
    public ZeraException(String message, Throwable cause) {
        super(message, cause);
    }
}
