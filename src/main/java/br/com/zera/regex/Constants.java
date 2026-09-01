package br.com.zera.regex;

/**
 * Classe responsável por armazenar constantes reutilizáveis.
 *
 * @author Mayte B
 */
public class Constants {

    //proibe a classe Constants de ser instanciada
    private Constants(){
        throw new UnsupportedOperationException("Classe Constants não deve ser instanciada");
    }

    //e-mail, CNPJ, CEP, telefone

    //Formato padrão de recebimento de email
    public static final String EMAIL_REGEX = "^[áéíóúâêîôûãõça-zA-Z0-9._%+-]+@[áéíóúâêîôûãõça-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    //Formato padrão de recebimento de telefone
    public static final String TELEFONE_REGEX = "^\\(?\\d{2}\\)?\\s?\\d{4,5}-?\\d{4}$";

    //Formato padrão de recebimento de nome
    public static final String NOME_REGEX = "^[A-Za-záéíóúâêîôûãõç]$";

    //Formato padrão de recebimento de CEP
    public static final String CEP_REGEX = "^[0-9]{5}\\-?[0-9]{3}$";

    //Formato padrão de recebimento de CNPJ
    public static final String CNPJ_REGEX = "^[0-9A-Z]{2}\\.?[0-9A-Z]{3}\\.?[0-9A-Z]{3}\\/?[0-9A-Z]{4}\\-?[0-9]{2}$";

    // Tela de erro
    public static final String ERROR_PAGE = "^/WEB-INF/Erro.html";
}
