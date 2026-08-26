package br.com.zera.model;

/**
 * Modelo base que representa a entidade Gestor no sistema.
 * Cada instancia corresponde a um registro na tabela "Gestor"
 *
 * @author Pedro Rufino
 */
public class Gestor {
//    Atributos
    private int codigo;
    private String nome;
    private String email;
    private String senha;
    private String telefone;
    private int codUnidade;

    /**
     * Construtor completo para inicialização do Gestor.
     *
     * @param codigo identificador único do funcionário no banco
     * @param nome nome completo do funcionário
     * @param email e-mail para acesso ao sistema
     * @param senha senha de autenticação
     * @param telefone número de contato
     * @param codUnidade chave estrangeira da unidade pertencente
     */
    public Gestor(int codigo, String nome, String email, String senha, String telefone, int codUnidade) {
        this.codigo = codigo;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.telefone = telefone;
        this.codUnidade = codUnidade;
    }

//    Getters e Setters
    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public int getCodUnidade() {
        return codUnidade;
    }

    public void setCodUnidade(int cod_unidade) {
        this.codUnidade = cod_unidade;
    }
}
