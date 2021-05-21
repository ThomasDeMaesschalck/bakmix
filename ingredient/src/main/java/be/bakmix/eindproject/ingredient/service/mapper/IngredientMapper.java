package be.bakmix.eindproject.ingredient.service.mapper;

import be.bakmix.eindproject.ingredient.business.IngredientEntity;
import be.bakmix.eindproject.ingredient.service.dto.Ingredient;
import org.mapstruct.Mapper;

/**
 * Mapstruct mapping class, maps from Ingredient to Ingredient Entity and the other way around.
 */
@Mapper(componentModel = "spring")
public interface IngredientMapper {
    Ingredient toDTO(IngredientEntity ingredientEntity);
    IngredientEntity toEntity(Ingredient ingredient);
}
