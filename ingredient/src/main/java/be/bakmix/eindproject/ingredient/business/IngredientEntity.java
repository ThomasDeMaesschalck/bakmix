package be.bakmix.eindproject.ingredient.business;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name="Ingredients")
@Data
public class IngredientEntity {
    @Id
    @GeneratedValue
    private Long id;
    private String uniqueCode;
    private String brand;
    private String type;
    private LocalDate purchaseDate;
    private String purchaseLocation;
    private String lotNumber;
    private String volume;
    private Boolean available;

}
