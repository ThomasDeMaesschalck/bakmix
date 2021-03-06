package be.bakmix.eindproject.pdf.service.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Orderline DTO with Product and Ingredient list
 */
@Data
public class Orderline {
    private Long id;
    private Long orderId;
    private Long productId;
    private Long qty;
    private BigDecimal purchasePrice;

    private Product product;
    private List<Ingredient> ingredients;
}
