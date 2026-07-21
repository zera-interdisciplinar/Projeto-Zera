package br.com.zera.model;

/**
 * Modelo que representa a entidade Endereço no sistema.
 *  Cada instância corresponde a um registro na tabela "Endereco"
 *
 * @author Pedro Rufino
 */
public class EnderecoModel {
//    Atributos
    private int codigo;
    private String bairro;
    private int numero;
    private String cep;
    private String logradouro;
    private String cidade;
    private String estado;
    private int codUnidade;

    /**
     * Construtor do Endereço.
     *
     * @param codigo identificador único do endereco no banco
     * @param bairro bairro da empresa
     * @param numero número da empresa
     * @param cep cep da empresa
     * @param logradouro logradouro da empresa
     * @param cidade cidade da empresa
     * @param estado estado da empresa
     * @param codUnidade chave estrangeira da unidade pertencente
     */
    public EnderecoModel(int codigo, String bairro, int numero, String cep, String logradouro, String cidade, String estado, int codUnidade) {
        this.codigo = codigo;
        this.bairro = bairro;
        this.numero = numero;
        this.cep = cep;
        this.logradouro = logradouro;
        this.cidade = cidade;
        this.estado = estado;
        this.codUnidade = codUnidade;
    }

//    Getters e Setters

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getCodUnidade() {
        return codUnidade;
    }

    public void setCodUnidade(int codUnidade) {
        this.codUnidade = codUnidade;
    }
}
