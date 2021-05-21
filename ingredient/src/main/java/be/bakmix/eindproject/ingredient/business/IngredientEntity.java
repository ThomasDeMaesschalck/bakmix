package be.bakmix.eindproject.ingredient.business;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Ingredient entity class. Ingredient objects are persisted in the database in the Ingredients table.
 */
@Entity
@Table(name="Ingredients")
@Data
public class IngredientEntity {

    /**
     * Generated id value that is unique for each Ingredient
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * The webshop owner gives each ingredient a uniquye alphanumerical identification String
     */
    private String uniqueCode;

    /**
     * Brand name of the Ingredient
     */
    private String brand;

    /**
     * The type of Ingredient. For example: sugar, salt, chocolate flakes, etc
     */
    private String type;

    /**
     * Purchase date of the ingredient
     */
    private LocalDate purchaseDate;

    /**
     * Where the ingredient was purchased
     */
    private String purchaseLocation;

    /**
     * The lot number of the ingredient
     */
    private String lotNumber;

    /**
     * How much the ingredient weighs or how much volume it has
     */
    private String volume;

    /**
     * This boolean indicates whether the ingredient is available (true) or whether it's fully used up or thrown away (false)
     */
    private Boolean available;

    /**
     * This boolean is set to true if an ingredient was subject to a recall
     */
    private Boolean recalled;

    /**
     * Expiry date of the ingredient
     */
    private LocalDate expiry;
}
