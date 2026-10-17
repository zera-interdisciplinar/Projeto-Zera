package br.com.zera.model;

import java.math.BigDecimal;

/**
 * Modelo que representa a entidade Plano no sistema.
 * Cada instância corresponde a um registro na tabela "Plano".
 *
 * @author Maytê Bastos
 */
public class Plano {

    //atributos
    private int codigo;
    private BigDecimal valor;
    private String nome;
    private String tempo;

    //construtor padrão vazio
    public Plano(){}

    /**
     * Construtor de um plano novo, ainda não salvo no banco.
     * O código não é informado porque é gerado automaticamente (serial).
     *
     * @param valor custo do plano
     * @param nome nome do plano
     * @param tempo duração do plano
     */
    public Plano(BigDecimal valor, String nome, String tempo) {
        this.valor = valor;
        this.nome = nome;
        this.tempo = tempo;
    }

    /**
     * Construtor completo do Plano, usado ao ler um registro do banco.
     *
     * @param codigo identificador único do plano no banco
     * @param valor custo do plano
     * @param nome nome do plano
     * @param tempo duração do plano
     */
    public Plano(int codigo, BigDecimal valor, String nome, String tempo) {
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

    public String getTempo() {
        return tempo;
    }

    public void setTempo(String tempo) {
        this.tempo = tempo;
    }

    /**
     * Converte o campo tempo em quantidade de meses, para calcular a data de fim
     * da assinatura. Aceita "Mensal", "Anual" ou um número de meses
     *
     * @return a duração do plano em meses
     */
    public int getDuracaoEmMeses() {
        if (tempo == null || tempo.isBlank()) {
            return 1;
        }
        String t = tempo.trim().toLowerCase();
        if (t.startsWith("anual")) {
            return 12;
        }
        if (t.startsWith("mensal")) {
            return 1;
        }
        try {
            return Math.max(1, Integer.parseInt(t));
        } catch (NumberFormatException nfe) {
            return 1;
        }
    }

    //saída em texto da tabela Plano
    @Override
    public String toString(){
        return "Plano \n" +
                "Código: " + getCodigo() +
                "\nValor: " + getValor() +
                "\nNome: " + getNome() +
                "\nTempo: " + getTempo();
    }
}