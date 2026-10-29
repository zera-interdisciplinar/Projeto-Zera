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
    public static final String NOME_REGEX = "^[A-Za-záàâãéêíóôõúçÁÀÂÃÉÊÍÓÔÕÚÇ ]+$";

    //Formato padrão de recebimento de CEP
    public static final String CEP_REGEX = "^[0-9]{5}\\-[0-9]{3}$";

    //Formato padrão de recebimento de CNPJ
    public static final String CNPJ_REGEX = "^[0-9A-Z]{2}\\.?[0-9A-Z]{3}\\.?[0-9A-Z]{3}\\/?[0-9A-Z]{4}\\-?[0-9]{2}$";

    // Tela de erro
    public static final String ERROR_PAGE = "/WEB-INF/Erro.jsp";

    //Senha: pelo menos 8 caracteres, um número e um caractere especial
    public static final String SENHA_REGEX = "^(?=.*[0-9])(?=.*[^A-Za-z0-9]).{8,}$";

    // Nome do atributo da sessão que guarda a unidade do gestor logado
    public static final String SESSAO_UNIDADE = "unidadeLogada";

    // Nome do atributo da sessão que guarda o gestor logado
    public static final String SESSAO_GESTOR = "gestorLogado";

    // Nome do atributo da sessão que guarda o admin logado
    public static final String SESSAO_ADMIN = "adminLogado";

    // Formato padrão da entrada de data
    public static final String DATA = "^[0-9]{2}/[0-9]{2}/[0-9]{4}$";

    // Chaves da sessão usadas durante o cadastro
    public static final String CADASTRO_GESTOR = "cadastroGestor";
    public static final String CADASTRO_ORGANIZACAO = "cadastroOrganizacao";
    public static final String CADASTRO_UNIDADE = "cadastroUnidade";
    public static final String CADASTRO_ENDERECO = "cadastroEndereco";
    public static final String CADASTRO_PLANO = "cadastroPlano";

    // Chaves da sessão usadas no fluxo de "Esqueci minha senha"
    public static final String RECUPERACAO_EMAIL = "recuperacaoEmail";
    public static final String RECUPERACAO_VALIDADA = "recuperacaoValidada";
    public static final String RECUPERACAO_CODIGO = "recuperacaoCodigo";
    public static final String RECUPERACAO_EXPIRA = "recuperacaoExpira";
    public static final String RECUPERACAO_TENTATIVAS = "recuperacaoTentativas";

    // Status de assinatura
    public static final String STATUS_ATIVA = "Ativa";
    public static final String STATUS_CANCELADA = "Cancelada";
}

