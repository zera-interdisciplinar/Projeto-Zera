package br.com.zera.model;
import java.time.LocalDate;

/**
 * Modelo que representa a entidade Estoque no sistema.
 * Cada instância corresponde a um registro na tabela "Estoque"
 *
 * @author Maytê Bastos
 */

//classe Estoque
public class Estoque{
    private int codigo;
    private String espacoNaUnidade;
    private int codResponsavel;
    private int codUnidade;

    //construtor padrão vazio
    public Estoque(){}

    /**
     * Construtor de Estoque.
     *
     * @param codigo identificador único do endereco no banco
     * @param espacoNaUnidade espaço do item na Unidade
     * @param codResponsavel identificador da tabela Gestor (chave estrangeira)
     * @param codUnidade identificador da tabela Unidade (chave estrangeira)
    */
    public Estoque(int codigo, String espacoNaUnidade, int codResponsavel, int codUnidade) {
        this.codigo = codigo;
        this.espacoNaUnidade = espacoNaUnidade;
        this.codResponsavel = codResponsavel;
        this.codUnidade = codUnidade;
    }

    //getters e setters
    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getEspacoNaUnidade() {
        return espacoNaUnidade;
    }

    public void setEspacoNaUnidade(String espacoNaUnidade) {
        this.espacoNaUnidade = espacoNaUnidade;
    }
""
    public int getCodResponsavel() {
        return codResponsavel;
    }

    public void setCodResponsavel(int codResponsavel) {
        this.codResponsavel = codResponsavel;
    }

    public int getCodUnidade() {
        return codUnidade;
    }

    public void setCodUnidade(int codUnidade) {
        this.codUnidade = codUnidade;
    }

    //saída em texto da tabela Assinatura
    public String toString(){
        return "Estoque /n"+
        "Código: "+this.codigo+
        "/nEspaço na unidade: "+this.espacoNaUnidade+
        "/nCódigo responsável: "+this.codResponsavel+
        "/nCódigo da tabela Unidade: "+this.codUnidade;
    }
}
