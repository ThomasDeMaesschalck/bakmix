package be.bakmix.eindproject.orderline.service.dto;

import lombok.Data;

import java.math.BigDecimal;

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
}
