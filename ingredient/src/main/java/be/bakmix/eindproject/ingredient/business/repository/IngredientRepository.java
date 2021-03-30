package be.bakmix.eindproject.ingredient.business.repository;

import be.bakmix.eindproject.ingredient.business.IngredientEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IngredientRepository extends PagingAndSortingRepository<IngredientEntity, Long> {
}
