package be.bakmix.eindproject.mail.service.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Product {
    private Long id;
    private String name;
    private BigDecimal price;
    private int vat;
}
