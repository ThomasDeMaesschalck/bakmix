package be.bakmix.eindproject.product.business;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * Product Entity. Persisted in database in the Products table.
 */
@Entity
@Table(name="Products")
@Data
public class ProductEntity {

    /**
     * Each Product has a unique id
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * The name of the product
     */
    private String name;

    /**
     * The current price of the product
     */
    private BigDecimal price;

    /**
     * The VAT rate in percent
     */
    private int vat;
}
