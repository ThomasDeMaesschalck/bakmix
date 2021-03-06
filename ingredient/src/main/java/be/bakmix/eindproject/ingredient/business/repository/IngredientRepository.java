package be.bakmix.eindproject.ingredient.business.repository;

import be.bakmix.eindproject.ingredient.business.IngredientEntity;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Boot repository of Ingredient objects. Implements paging support.
 */
@Repository
public interface IngredientRepository extends PagingAndSortingRepository<IngredientEntity, Long> {
}
