package br.com.zera.model;

/**
 * Modelo que representa a entidade Unidade no sistema.
 *  Cada instância corresponde a um registro na tabela "Unidade"
 *
 * @author Pedro Rufino
 */
public class UnidadeModel {
//    Atributos
    private int codigo;
    private String cnpj;
    private String email;
    private int codOrganizacao;

    /**
     * Construtor da Unidade.
     *
     * @param codigo identificador único da unidade no banco
     * @param cnpj cnpj da unidade
     * @param email email da unidade
     * @param codOrganizacao chave estrangeira da organização pertencente
     */
    public UnidadeModel(int codigo, String cnpj, String email, int codOrganizacao) {
        this.codigo = codigo;
        this.cnpj = cnpj;
        this.email = email;
        this.codOrganizacao = codOrganizacao;
    }

//    Getters e Setters
    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getCodOrganizacao() {
        return codOrganizacao;
    }

    public void setCodOrganizacao(int codOrganizacao) {
        this.codOrganizacao = codOrganizacao;
    }
}
