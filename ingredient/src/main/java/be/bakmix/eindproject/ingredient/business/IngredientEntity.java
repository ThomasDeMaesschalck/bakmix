package be.bakmix.eindproject.ingredient.business;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class IngredientEntity {
    @Id
    @GeneratedValue
    private Long id;
    private String uniqueCode;
    private String brand;
    private String type;
    private String purchaseDate;
    private String purchaseLocation;
    private String lotNumber;
    private String volume;
    private Boolean available;

}
