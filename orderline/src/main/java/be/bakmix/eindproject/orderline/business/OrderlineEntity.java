package be.bakmix.eindproject.orderline.business;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * Orderline Entity. Persisted in database in the Orderlines table.
 */
@Entity
@Table(name="Orderlines")
@Data
public class OrderlineEntity {

    /**
     * Unique id for each orderline, generated value.
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * The id of the Order this orderline belongs to
     */
    private Long orderId;

    /**
     * The id of the Product this Orderline contains
     */
    private Long productId;

    /**
     * How many products this orderline contains
     */
    private Long qty;

    /**
     * The purchase price per Product
     */
    private BigDecimal purchasePrice;

}


