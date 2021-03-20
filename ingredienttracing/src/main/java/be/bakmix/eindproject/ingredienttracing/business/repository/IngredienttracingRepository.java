package be.bakmix.eindproject.ingredienttracing.business.repository;

import be.bakmix.eindproject.ingredienttracing.business.IngredienttracingEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IngredienttracingRepository extends CrudRepository<IngredienttracingEntity, Long> {
}
