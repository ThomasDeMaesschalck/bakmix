package be.bakmix.eindproject.ingredienttracing.web.rest.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom exception for Ingredienttracing
 */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public class IngredienttracingNotFoundException extends RuntimeException{
        public IngredienttracingNotFoundException(String s) {
            super(s);
        }

    }

