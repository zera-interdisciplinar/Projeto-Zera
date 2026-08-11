package br.com.zera.model;

/**
 * Interface padrão com atributos de todas as entidades model
 * 
 * Define um contrato mínimo para que as classes de modelo forneçam
 * um identificador único por meio do método {@link #getCodigo()}.
 * 
 * Essa interface é utilizada pela camada genérica de persistência
 * ("DAO<T extends Model>"), permitindo que o DAO execute
 * operações de forma padronizada sobre qualquer entidade,
 * independentemente da sua estrutura interna.
 * 
 * @author Mayte B
 */

public interface Model {
    /**
     * Retorna o identificador único da entidade.
     * @return objeto que representa o ID da entidade
     */

    Object getCodigo();
}