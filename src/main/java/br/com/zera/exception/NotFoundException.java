package br.com.zera.exception;

/**
 * Exceção lançada quando uma busca por ID ou atributo não encontra nenhum registro no banco de dados.
 */
public class NotFoundException extends ZeraException {

    /**
     * Constrói a exceção com uma mensagem direta.
     *
     * @param message Mensagem descrevendo o item não encontrado.
     */
    public NotFoundException(String message) {
        super(message);
    }

    /**
     * Constrói a mensagem de erro formatada informando a entidade e o código buscado.
     *
     * @param entidade Nome da tabela ou entidade (ex: "Endereco", "Unidade").
     * @param codigo Código identificador que não foi localizado.
     */
    public NotFoundException(String entidade, int codigo) {
        super(entidade + " com o código " + codigo + " não foi encontrado(a) no sistema.");
    }
}