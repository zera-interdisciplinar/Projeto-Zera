package br.com.zera.email;

import java.security.SecureRandom;

/**
 * Classe utilitária que gera os códigos de verificação enviados por e-mail
 * e monta o texto do e-mail de recuperação de senha.
 *
 * Esta classe não deve ser instanciada.
 *
 * @author Pedro Rufino
 */
public final class CodigoVerificacao {

    /** Quantidade de dígitos do código */
    public static final int TAMANHO = 6;

    /** Minutos que o código continua valendo depois de enviado */
    public static final int MINUTOS_VALIDADE = 10;

    /** Gerador de números aleatórios seguro, próprio para códigos de segurança */
    private static final SecureRandom ALEATORIO = new SecureRandom();

    /**
     * Construtor privado que proíbe a instanciação da classe utilitária.
     */
    private CodigoVerificacao() {
        throw new UnsupportedOperationException("Classe CodigoVerificacao não deve ser instanciada");
    }

    /**
     * Gera um código aleatório de 6 dígitos, completando com zeros à esquerda.
     *
     * @return o código gerado, por exemplo "048213"
     */
    public static String gerar() {
        // número de 0 a 999999; o %06d completa com zeros para ter sempre 6 dígitos
        int numero = ALEATORIO.nextInt(1_000_000);
        return String.format("%06d", numero);
    }

    /**
     * Monta o corpo em HTML do e-mail de recuperação de senha.
     *
     * @param nome nome do gestor
     * @param codigo código de verificação
     * @return o HTML do e-mail
     */
    public static String montarEmailRecuperacao(String nome, String codigo) {
        return "<div style=\"font-family: Arial, sans-serif; color: #152D6B;\">"
                + "<h2>Redefinição de senha - Zera</h2>"
                + "<p>Olá, " + nome + "!</p>"
                + "<p>Use o código abaixo para redefinir sua senha:</p>"
                + "<p style=\"font-size: 32px; font-weight: bold; letter-spacing: 8px;\">" + codigo + "</p>"
                + "<p>O código vale por " + MINUTOS_VALIDADE + " minutos.</p>"
                + "<p>Se você não pediu a troca de senha, ignore este e-mail.</p>"
                + "</div>";
    }
}