package br.com.zera.model;
import java.math.BigDecimal;
import java.time.LocalDate;

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
    private LocalDate tempo;

    //construtorpadrão vazio
    public Plano(){}

    /**
     * Construtor de Assinatura.
     * 
     * @param codigo identificador único do endereco no banco
     * @param valor custo do plano
     * @param nome nome cadastrado no plano
     * @param tempo duração do plano
     */
    public Plano(int codigo, BigDecimal valor, String nome, LocalDate tempo) {
        this.codigo = codigo;
        this.valor = valor;
        this.nome = nome;
        this.tempo = tempo;
    }

    //getters e setters
    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDate getTempo() {
        return tempo;
    }

    public void setTempo(LocalDate tempo) {
        this.tempo = tempo;
    }

    //saída em texto da tabela Organizacao
    public String toString(){
        return "Plano: /n"+
        "Código: "+this.codigo+
        "/nValor: "+this.valor+
        "/nNome: "+this.nome+
        "/nTempo: "+this.tempo
    }
}
