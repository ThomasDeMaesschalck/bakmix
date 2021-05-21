package be.bakmix.eindproject.order.business;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Order Entity. Orders are persisted in the database in the Orders table.
 */
@Entity
@Table(name="Orders")
@Data
public class OrderEntity {

    /**
     * Id is an automatically generated unique value for each Order.
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * The id of the customer that made the order
     */
    private Long customerId;

    /**
     * Date the order was made
     */
    private LocalDate date;

    /**
     * Order status
     */
    private Long status;

    /**
     * Amount of discount the order received
     */
    private BigDecimal discount;

    /**
     * Shipping cost of the order.
     */
    private  BigDecimal shippingCost;
}
