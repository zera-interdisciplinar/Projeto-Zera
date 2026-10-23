package br.com.zera.exception;

/**
 * Exceção lançada quando não é possível enviar um e-mail
 * (servidor SMTP fora do ar, senha de app errada, variáveis não configuradas).
 *
 * @author Pedro Rufino
 */
public class EmailFailedException extends ZeraException {

    /**
     * Constrói a exceção informando uma mensagem de erro.
     *
     * @param message descrição da falha no envio
     */
    public EmailFailedException(String message) {
        super(message);
    }

    /**
     * Constrói a exceção informando uma mensagem e a causa original.
     *
     * @param message descrição da falha no envio
     * @param cause exceção original do Jakarta Mail
     */
    public EmailFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}