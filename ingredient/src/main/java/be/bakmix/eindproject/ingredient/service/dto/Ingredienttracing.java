package be.bakmix.eindproject.ingredient.service.dto;

import lombok.Data;

/**
 * Ingredient Tracing object DTO
 */
@Data
public class Ingredienttracing {
    private Long id;
    private Long orderlineId;
    private Long ingredientId;
}
