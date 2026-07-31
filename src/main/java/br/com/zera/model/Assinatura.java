package br.com.zera.model;
import java.time.LocalDate;

/**
 * Modelo que representa a entidade Assinatura no sistema.
 * Cada instância corresponde a um registro na tabela "Assinatura"
 *
 * @author Maytê Bastos
 */

//classe Assinatura
public class Assinatura{

    //atributos
    private int codigo;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private String status;
    private int codOrganizacao;
    private int codPlano;

    //construtor padrão vazio
    public Assinatura(){}

    /**
     * Construtor de Assinatura.
     *
     * @param codigo identificador único do endereco no banco
     * @param dataInicio data de registro da assinatura
     * @param dataFim data de vencimento da assinatura
     * @param status status definido como "Ativa", "Inativa", "Vencida"
     * @param codOrganizacao identificador da tabela Organizacao (chave estrangeira)
     * @param codPlano identificador da tabela Plano (chave estrangeira)
     */
    public Assinatura(int codigo, LocalDate dataInicio, LocalDate dataFim, String status, int codOrganizacao, int codPlano) {
        this.codigo = codigo;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.status = status;
        this.codOrganizacao = codOrganizacao;
        this.codPlano = codPlano;
    }

    //getters e setters
    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCodOrganizacao() {
        return codOrganizacao;
    }

    public void setCodOrganizacao(int codOrganizacao) {
        this.codOrganizacao = codOrganizacao;
    }

    public int getCodPlano() {
        return codPlano;
    }

    public void setCodPlano(int codPlano) {
        this.codPlano = codPlano;
    }

    //saída em texto da tabela Assinatura
    public String toString(){
        return "Assinatura /n"+
        "Código: "+this.codigo+
        "/nData de Início: "+this.dataInicio+
        "/nData de Fim: "+this.dataFim+
        "/nStatus: "+this.status+
        "/nCódigo da tabela Organização: "+this.codOrganizacao+
        "/nCódigo da tabela Plano: "+this.codPlano
    }
}
