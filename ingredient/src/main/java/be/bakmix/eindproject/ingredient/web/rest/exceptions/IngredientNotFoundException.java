package be.bakmix.eindproject.ingredient.web.rest.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom error message when Ingredient is not found
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class IngredientNotFoundException extends RuntimeException{
    public IngredientNotFoundException(String s) {
        super(s);
    }

}
