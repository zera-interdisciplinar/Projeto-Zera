package br.com.zera.model;

/**
 * Representa um administrador do sistema Zera.
 *
 * @author Pedro Rufino
 */
public class Admin {
    // Atributos
    private int codigo;
    private String nome;
    private String email;
    private String senha;

    //Construtor padrão vazio
    public Admin() {}

    /**
     * Construtor para um novo admin, ainda não salvo no banco (sem código).
     *
     * @param nome  nome do administrador
     * @param email email do administrador
     * @param senha senha do administrador
     */
    public Admin(String nome, String email, String senha) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }

    /**
     * Construtor completo, para um admin que já existe no banco.
     *
     * @param codigo código gerado pelo banco
     * @param nome   nome do administrador
     * @param email  email do administrador
     * @param senha  senha do administrador (em hash)
     */
    public Admin(int codigo, String nome, String email, String senha) {
        this.codigo = codigo;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }

    // getters e setters
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

    //Retorna uma representação em texto do admin, sem expor a senha.
    @Override
    public String toString() {
        return "Admin" +
                "\nCódigo: " + getCodigo()+
                "\nNome: " + getNome() +
                "\nEmail: " + getEmail();
    }
}