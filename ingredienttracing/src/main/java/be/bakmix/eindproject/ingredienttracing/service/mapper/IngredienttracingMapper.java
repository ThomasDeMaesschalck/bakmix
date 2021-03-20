package be.bakmix.eindproject.ingredienttracing.service.mapper;

import be.bakmix.eindproject.ingredienttracing.business.IngredienttracingEntity;
import be.bakmix.eindproject.ingredienttracing.service.dto.Ingredienttracing;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IngredienttracingMapper {
    Ingredienttracing toDTO(IngredienttracingEntity ingredienttracingEntity);
    IngredienttracingEntity toEntity(Ingredienttracing ingredienttracing);
}
