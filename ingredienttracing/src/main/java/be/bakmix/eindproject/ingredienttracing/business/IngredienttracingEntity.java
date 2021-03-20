package be.bakmix.eindproject.ingredienttracing.business;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name="Ingredient_tracing")
@Data
public class IngredienttracingEntity {
    @Id
    @GeneratedValue
    private Long id;
    private Long orderlineId;
    private Long ingredientId;
}
