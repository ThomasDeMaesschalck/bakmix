package be.bakmix.eindproject.ingredienttracing.service.mapper;

import be.bakmix.eindproject.ingredienttracing.business.IngredienttracingEntity;
import be.bakmix.eindproject.ingredienttracing.service.dto.Ingredienttracing;
import org.mapstruct.Mapper;

/**
 * Mapstruct mapping class. Maps from DTO to Entity and the other way around for the Ingredienttracing.
 */
@Mapper(componentModel = "spring")
public interface IngredienttracingMapper {
    Ingredienttracing toDTO(IngredienttracingEntity ingredienttracingEntity);
    IngredienttracingEntity toEntity(Ingredienttracing ingredienttracing);
}
