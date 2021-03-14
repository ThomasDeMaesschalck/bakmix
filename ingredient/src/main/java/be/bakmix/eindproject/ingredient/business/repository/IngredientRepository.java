package be.bakmix.eindproject.ingredient.business.repository;

import be.bakmix.eindproject.ingredient.business.IngredientEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IngredientRepository extends CrudRepository<IngredientEntity, Long> {
}
