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
    int codigo;
    LocalDate dataChegadaItem;
    String statusItem;
    int codUnidade;

    //construtor padrão vazio
    public Estoque(){}
    
    /**
     * Construtor de Assinatura.
     *
     * @param codigo identificador único do endereco no banco
     * @param dataChegadaItem data de entrada no Item no Estoque
     * @param statusItem status do Item em Estoque 
     * @param codUnidade identificador da tabela Unidade (chave estrangeira)
    */
    public Estoque(int codigo, LocalDate dataChegadaItem, String statusItem, int codUnidade) {
        this.codigo = codigo;
        this.dataChegadaItem = dataChegadaItem;
        this.statusItem = statusItem;
        this.codUnidade = codUnidade;
    }

    //getters e setters
    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public LocalDate getDataChegadaItem() {
        return dataChegadaItem;
    }

    public void setDataChegadaItem(LocalDate dataChegadaItem) {
        this.dataChegadaItem = dataChegadaItem;
    }

    public String getStatusItem() {
        return statusItem;
    }

    public void setStatusItem(String statusItem) {
        this.statusItem = statusItem;
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
        "/nData de chegada do Item: "+this.dataChegadaItem+
        "/nStatus do Item: "+this.statusItem+
        "/nCódigo da tabela Unidade: "+this.codUnidade;
    }
}
