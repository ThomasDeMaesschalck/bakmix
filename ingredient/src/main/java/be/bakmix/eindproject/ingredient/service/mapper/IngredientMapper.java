package be.bakmix.eindproject.ingredient.service.mapper;

import be.bakmix.eindproject.ingredient.business.IngredientEntity;
import be.bakmix.eindproject.ingredient.service.dto.Ingredient;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IngredientMapper {
    Ingredient toDTO(IngredientEntity ingredientEntity);
    IngredientEntity toEntity(Ingredient ingredient);
}
