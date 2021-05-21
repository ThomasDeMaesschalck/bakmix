package be.bakmix.eindproject.product.service.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Product DTO
 */
@Data
public class Product {
    private Long id;
    private String name;
    private BigDecimal price;
    private int vat;
}
