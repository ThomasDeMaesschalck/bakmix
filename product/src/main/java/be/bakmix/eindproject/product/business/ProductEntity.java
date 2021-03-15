package be.bakmix.eindproject.product.business;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name="Products")
@Data
public class ProductEntity {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private BigDecimal price;
    private int vat;
}
