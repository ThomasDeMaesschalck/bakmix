package be.bakmix.eindproject.mail.service.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Orderline DTO
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
