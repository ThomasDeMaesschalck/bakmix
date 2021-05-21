package be.bakmix.eindproject.order.service.dto;

import lombok.Data;

/**
 * Ingredienttracing DTO
 */
@Data
public class Ingredienttracing {
    private Long id;
    private Long orderlineId;
    private Long ingredientId;
}
