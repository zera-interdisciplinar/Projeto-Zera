package br.com.zera.model;
import java.time.LocalDate;

/**
 * Modelo que representa a entidade Organizacao no sistema.
 * Cada instância corresponde a um registro na tabela "Organizacao"
 *
 * @author Maytê Bastos
 */

//classe Organização
public class Organizacao{

    //atributos
    private int codigo;
    private String cnpj;
    private String nome;
    private LocalDate dataCadastro;

    //construtor padrão vazio
    public Organizacao(){}

    /**
     * Construtor de Assinatura.
     *
     * @param codigo identificador único do endereco no banco
     * @param cnpj cnpj do cliente
     * @param nome nome do cliente
     * @param dataCadastro data de cadastro na tabela
    */
    public Organizacao(int codigo, String cnpj, String nome, LocalDate dataCadastro) {
        this.codigo = codigo;
        this.cnpj = cnpj;
        this.nome = nome;
        this.dataCadastro = dataCadastro;
    }

    //getters e setters
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

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDate getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDate dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    //saída em texto da tabela Organizacao
    public String toString(){
        return "Organizacao /n"+
        "Código: "+this.codigo+
        "/nCNPJ: "+this.cnpj+
        "/nNome: "+this.nome+
        "/nData de cadastro: "+this.dataCadastro;
    }
}
