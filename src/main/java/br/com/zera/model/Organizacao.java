package br.com.zera.model;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Modelo que representa a entidade Organizacao no sistema.
 * Cada instância corresponde a um registro na tabela "Organizacao"
 *
 * @author Maytê B
 */

//classe Organização
public class Organizacao{

    //atributos
    private int codigo;
    private String cnpj;
    private String nome;
    private String email;
    private LocalDate dataCadastro;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;

    //construtor padrão vazio
    public Organizacao(){}

    /**
     * Construtor da Organização.
     *
     * @param codigo identificador único do endereco no banco
     * @param cnpj cnpj do cliente
     * @param nome nome do cliente
     * @param email email da organização
     * @param dataCadastro data de cadastro na tabela
     */
    public Organizacao(int codigo, String cnpj, String nome, String email, LocalDate dataCadastro) {
        this.codigo = codigo;
        this.cnpj = cnpj;
        this.nome = nome;
        this.email = email;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDate dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }

    public LocalDateTime getAtualizadoEm() {
        return atualizadoEm;
    }

    public void setAtualizadoEm(LocalDateTime atualizadoEm) {
        this.atualizadoEm = atualizadoEm;
    }

    //saída em texto da tabela Organizacao
    public String toString(){
        return "Organizacao /n"+
                "Código: "+getCodigo()+
                "/nCNPJ: "+getCnpj()+
                "/nNome: "+getNome()+
                "/nEmail: "+getEmail()+
                "/nData de cadastro: "+getDataCadastro()+
                "/nCriado em: "+getCriadoEm()+
                "/nAtualizado em: "+getAtualizadoEm();
    }
}