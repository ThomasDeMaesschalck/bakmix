package be.bakmix.eindproject.ingredienttracing.business;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Ingredienttracing entity. Stored in database in Ingredient_tracing table
 */
@Entity
@Table(name="Ingredient_tracing")
@Data
public class IngredienttracingEntity {

    /**
     * Generated unique id value
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * The orderline of this tracing
     */
    private Long orderlineId;

    /**
     * The ingredient that gets linked to the orderline
     */
    private Long ingredientId;
}
