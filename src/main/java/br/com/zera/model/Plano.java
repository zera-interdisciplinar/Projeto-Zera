package br.com.zera.model;
import java.math.BigDecimal;

/**
 * Modelo que representa a entidade Organizacao no sistema.
 * Cada instância corresponde a um registro na tabela "Organizacao"
 *
 * @author Maytê Bastos
 */

//classe Plano
public class Plano{

    //atributos
    private int codigo;
    private BigDecimal valor;
    private String nome;
    private String tempo;

    //construtorpadrão vazio
    public Plano(){}

    /**
     * Construtor do Plano.
     * O código não é informado porque é gerado automaticamente (serial).
     *
     * @param valor custo do plano
     * @param nome nome cadastrado no plano
     * @param tempo duração do plano
     */
    public Plano(BigDecimal valor, String nome, String tempo) {
        this.valor = valor;
        this.nome = nome;
        this.tempo = tempo;
    }

    /**
     * Construtor do Plano.
     * 
     * @param codigo identificador único do endereco no banco
     * @param valor custo do plano
     * @param nome nome cadastrado no plano
     * @param tempo duração do plano
     */
    public Plano(int codigo, BigDecimal valor, String nome, String tempo) {
        this.codigo = codigo;
        this.valor = valor;
        this.nome = nome;
        this.tempo = tempo;
    }

    //getters e setters
    public String getTempo() {
        return tempo;
    }

    public void setTempo(String tempo) {
        this.tempo = tempo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    //saída em texto da tabela Organizacao
    @Override
    public String toString(){
        return "Plano: \n"+
        "Código: "+getCodigo()+
        "\nValor: "+getValor()+
        "\nNome: "+getNome()+
        "\nTempo: "+getTempo();
    }
}
