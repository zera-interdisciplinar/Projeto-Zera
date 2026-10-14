package br.com.zera.model;

import java.time.LocalDateTime;

/**
 * Modelo que representa a entidade Organizacao no sistema.
 * Cada instância corresponde a um registro na tabela "Organizacao".
 *
 * @author Maytê B
 */
public class Organizacao {

    //atributos
    private int codigo;
    private String cnpj;
    private String nome;
    private String email;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;

    //construtor padrão vazio
    public Organizacao(){}

    /**
     * Construtor de uma organização nova, ainda não salva no banco.
     * O código e as datas não são informados porque são gerados pelo banco.
     *
     * @param cnpj cnpj da organização
     * @param nome nome da organização
     * @param email email da organização
     */
    public Organizacao(String cnpj, String nome, String email) {
        this.cnpj = cnpj;
        this.nome = nome;
        this.email = email;
    }

    /**
     * Construtor da Organização com o código, usado na atualização.
     *
     * @param codigo identificador único da organização no banco
     * @param cnpj cnpj da organização
     * @param nome nome da organização
     * @param email email da organização
     */
    public Organizacao(int codigo, String cnpj, String nome, String email) {
        this.codigo = codigo;
        this.cnpj = cnpj;
        this.nome = nome;
        this.email = email;
    }

    /**
     * Construtor completo da Organização, usado ao ler um registro do banco.
     *
     * @param codigo identificador único da organização no banco
     * @param cnpj cnpj da organização
     * @param nome nome da organização
     * @param email email da organização
     * @param criadoEm data e hora em que a organização foi cadastrada
     * @param atualizadoEm data e hora da última atualização
     */
    public Organizacao(int codigo, String cnpj, String nome, String email,
                       LocalDateTime criadoEm, LocalDateTime atualizadoEm) {
        this.codigo = codigo;
        this.cnpj = cnpj;
        this.nome = nome;
        this.email = email;
        this.criadoEm = criadoEm;
        this.atualizadoEm = atualizadoEm;
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
    @Override
    public String toString(){
        return "Organizacao \n" +
                "Código: " + getCodigo() +
                "\nCNPJ: " + getCnpj() +
                "\nNome: " + getNome() +
                "\nEmail: " + getEmail() +
                "\nCriado em: " + getCriadoEm() +
                "\nAtualizado em: " + getAtualizadoEm();
    }
}