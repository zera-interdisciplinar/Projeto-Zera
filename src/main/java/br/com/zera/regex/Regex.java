package br.com.zera.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Classe utilitária para validação de dados usando expressões regulares.
 *
 * Uso de REGEX
 *
 * Esta classe não deve ser instanciada.
 *
 * @author Mayte B
 */
public class Regex {

    /**
     * Construtor privado que proibe instanciação de classe utilitária
     */
    private Regex(){
        throw new UnsupportedOperationException("Classe Regex não deve ser instanciada");
    }

    /**
     * Valida se a string fornecida é um CEP válido.
     *
     * @param cep a ser validado
     * @return true se o CEP for válido de acordo com o padrão de {@link Constants}
     */
    public static boolean validarCEP(String cep){
        String regex = Constants.CEP_REGEX;
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(cep);
        return matcher.matches();
    }

    /**
     * Valida se a string fornecida é um Telefone válido.
     *
     * @param tel a ser validado
     * @return true se o Telefone for válido de acordo com o padrão de {@link Constants}
     */
    public static boolean validarTel(String tel){
        String regex = Constants.TELEFONE_REGEX;
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(tel);
        return matcher.matches();
    }

    /**
     * Valida se a string fornecida é um email válido.
     *
     * @param email a ser validado
     * @return true se o Email for válido de acordo com o padrão de {@link Constants}
     */
    public static boolean validarEmail(String email){
        String regex = Constants.EMAIL_REGEX;
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /**
     * Valida se a string fornecida é um CNPJ válido.
     *
     * @param cnpj a ser validado
     * @return true se o CNPJ for válido de acordo com o padrão de {@link Constants}
     */
    public static boolean validarCNPJ(String cnpj){
        String regex = Constants.CNPJ_REGEX;
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(cnpj);
        return matcher.matches();
    }

    /**
     * Valida se a string fornecida é um nome válido.
     *
     * @param nome a ser validado
     * @return true se o nome for válido de acordo com o padrão de {@link Constants}
     */
    public static boolean validarNome(String nome){
        String regex = Constants.NOME_REGEX;
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(nome);
        return matcher.matches();
    }
}
