package be.bakmix.eindproject.order.business;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name="Orders")
@Data
public class OrderEntity {
    @Id
    @GeneratedValue
    private Long id;
    private Long customerId;
    private LocalDate date;
    private Long status;
    private BigDecimal discount;
    private  BigDecimal shippingCost;
}
