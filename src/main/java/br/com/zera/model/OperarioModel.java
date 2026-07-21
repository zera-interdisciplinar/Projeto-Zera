package br.com.zera.model;

/**
 * Modelo que representa a entidade Operário no sistema.
 * Herda todos os atributos e métodos de acesso da classe FuncionarioModel.
 *
 * @autor Pedro Rufino
 */
public class OperarioModel extends FuncionarioModel {
    /**
     * Construtor do OperarioModel.
     * Repassa os parâmetros para a superclasse FuncionarioModel.
     *
     * @param codigo identificador único no banco
     * @param nome nome completo do operário
     * @param email e-mail de acesso
     * @param senha senha de acesso
     * @param telefone número de contato
     * @param codUnidade chave estrangeira da unidade associada
     */
    public OperarioModel(int codigo, String nome, String email, String senha, String telefone, int codUnidade) {
        super(codigo, nome, email, senha, telefone, codUnidade);
    }
}
