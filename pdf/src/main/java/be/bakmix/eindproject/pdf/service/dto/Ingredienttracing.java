package be.bakmix.eindproject.pdf.service.dto;

import lombok.Data;

@Data
public class Ingredienttracing {
    private Long id;
    private Long orderlineId;
    private Long ingredientId;
}
