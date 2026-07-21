package br.com.zera.model;
/**
 * Modelo que representa a entidade Gestor no sistema.
 * Herda todos os atributos e métodos de acesso da classe FuncionarioModel.
 *
 * @author Pedro Rufino
 */
public class GestorModel extends FuncionarioModel {
    /**
     * Construtor do GestorModel.
     * Repassa os parâmetros para a superclasse FuncionarioModel.
     *
     * @param codigo identificador único no banco
     * @param nome nome completo do gestor
     * @param email e-mail de acesso
     * @param senha senha de acesso
     * @param telefone número de contato
     * @param codUnidade chave estrangeira da unidade associada
     */
    public GestorModel(int codigo, String nome, String email, String senha, String telefone, int codUnidade) {
        super(codigo, nome, email, senha, telefone, codUnidade);
    }
}