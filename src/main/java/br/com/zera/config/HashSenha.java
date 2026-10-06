package br.com.zera.config;

import br.com.zera.exception.ZeraException;
import org.mindrot.jbcrypt.BCrypt;

/**
 * Gera e verifica hashes de senha usando BCrypt.
 *
 * @author Pedro Rufino
 */
public class HashSenha {

    // Custo do BCrypt
    private static final int custo = 10;

    /**
     * Construtor privado para impedir a criação de objetos desta classe.
     */
    private HashSenha() {}

    /**
     * Gera o hash de uma senha.
     *
     * @param senha senha em texto puro
     * @return hash da senha
     * @throws ZeraException se a senha for nula ou vazia
     */
    public static String gerarHash(String senha) {
        if (senha == null || senha.isBlank()) {
            throw new ZeraException("A senha não pode ser vazia.");
        }
        return BCrypt.hashpw(senha, BCrypt.gensalt(custo));
    }

    /**
     * Verifica se a senha informada corresponde ao hash salvo.
     *
     * @param senhaDigitada senha em texto puro
     * @param hashSalvo     hash salvo no banco
     * @return true se a senha estiver correta, false caso contrário
     * @throws ZeraException se o hash salvo for inválido
     */
    public static boolean verificar(String senhaDigitada, String hashSalvo) {
        if (senhaDigitada == null || hashSalvo == null) {
            return false;
        }
        try {
            return BCrypt.checkpw(senhaDigitada, hashSalvo);
        } catch (IllegalArgumentException e) {
            // Lançada pelo BCrypt quando o hash não está no formato esperado
            throw new ZeraException("Hash de senha inválido.", e);
        }
    }
}