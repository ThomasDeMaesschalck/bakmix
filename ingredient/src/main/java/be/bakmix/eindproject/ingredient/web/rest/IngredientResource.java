package be.bakmix.eindproject.ingredient.web.rest;

import be.bakmix.eindproject.ingredient.service.IngredientService;
import be.bakmix.eindproject.ingredient.service.dto.Ingredient;
import be.bakmix.eindproject.ingredient.web.rest.exceptions.IngredientNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.List;

/**
 * Ingredient resource class. Creates the REST API endpoints.
 */
@CrossOrigin
@RestController
@RequestMapping("/api")
@AllArgsConstructor
@Log4j2
public class IngredientResource {


    /**
     * The ingredient service
     */
    @Autowired
    private IngredientService ingredientService;


    /**
     * Get a list of all ingredients, without pagination
     * @return Returns a List of ingredients
     */
    @GetMapping("/ingredients")
    @RolesAllowed("bakmix-admin")
    public ResponseEntity<List<Ingredient>> getAll() {
        List<Ingredient> ingredient = ingredientService.getAllNoPagination();
        log.info("Retrieved all ingredients");
        return ResponseEntity.ok(ingredient);
    }

    /**
     * Get a page of ingredients
     * @param pageNo The page number
     * @param pageSize The page size
     * @param sortBy Sort filter
     * @return Returns the requested Page of ingredients
     */
    @GetMapping("/ingredientspaginated/")
    @RolesAllowed("bakmix-admin")
    public ResponseEntity<Page<Ingredient>> getAllPaginated(@RequestParam(defaultValue = "0") Integer pageNo,
                                                            @RequestParam(defaultValue = "10") Integer pageSize,
                                                            @RequestParam(defaultValue = "id") String sortBy) {
        Page<Ingredient> ingredient = ingredientService.getAll(pageNo, pageSize, sortBy);
        log.info("Retrieved all ingredients");
        return ResponseEntity.ok(ingredient);
    }

    /**
     * Get a specific ingredient
     * @param id The database id of this ingredient
     * @return The requested Ingredient
     */
    @GetMapping("/ingredients/{id}")
    @RolesAllowed("bakmix-admin")
    public ResponseEntity<Ingredient> getById(@PathVariable Long id){
        Ingredient ingredient = ingredientService.getById(id);
        if(ingredient == null){
            log.error("Failed to find ingredient number " + id);

            return ResponseEntity.notFound().build();
        }
        log.info("Retrieved ingredient number " + id);
        return ResponseEntity.ok(ingredient);
    }

    /**
     * Search a specific ingredient based on uniqueCode
     * @param uniqueId The unique code of the Ingredient
     * @return The requested Ingredient
     */
    @GetMapping("/ingredients/uniquecode/{uniqueId}")
    @RolesAllowed("bakmix-admin")
    public ResponseEntity<Ingredient> findByUniqueId(@PathVariable String uniqueId){
        Ingredient result = ingredientService.findByUniqueId(uniqueId);
        if(result == null){
            log.error("Failed to find ingredient number with unique code " + uniqueId);

            return ResponseEntity.notFound().build();
        }
        log.info("Retrieved ingredient with unique code " + uniqueId);
        return ResponseEntity.ok(result);
    }

    /**
     * Persist an ingredient
     * @param ingredient The ingredient that needs to be persisted
     * @return The saved ingredient
     */
    @PostMapping("/ingredients")
    @RolesAllowed("bakmix-admin")
    public ResponseEntity<Ingredient> createContact(@Valid @RequestBody Ingredient ingredient)
    {
        ingredientService.save(ingredient);
        log.info("Saved ingredient " + ingredient);
        return ResponseEntity.ok(ingredient);
    }

    /**
     * Update an existing ingredient
     * @param id The Id of the ingredient that is updated
     * @param ingredienDetails The new ingredient details
     * @return The saved ingredient
     */
    @PutMapping("/ingredients/{id}")
    @RolesAllowed("bakmix-admin")
    public ResponseEntity<Ingredient> replaceIngredient(@PathVariable Long id, @Valid @RequestBody Ingredient ingredienDetails) {

      Ingredient ingredient = ingredientService.getById(id);
      ingredient.setAvailable(ingredienDetails.getAvailable());
      ingredient.setBrand(ingredienDetails.getBrand());
      ingredient.setType(ingredienDetails.getType());
      ingredient.setLotNumber(ingredienDetails.getLotNumber());
      ingredient.setPurchaseDate(ingredienDetails.getPurchaseDate());
      ingredient.setPurchaseLocation(ingredienDetails.getPurchaseLocation());
      ingredient.setUniqueCode(ingredienDetails.getUniqueCode());
      ingredient.setVolume(ingredienDetails.getVolume());
      ingredient.setRecalled(ingredienDetails.getRecalled());
      ingredient.setExpiry(ingredienDetails.getExpiry());
        ingredientService.save(ingredient);
        log.info("Updated ingredient number " + id + ingredient);
     return ResponseEntity.ok(ingredient);
    }

    /**
     * Delete an ingredient from the DB
     * @param id The id of the ingredient that needs to be deleted
     * @return ResponseEntity
     */
    @DeleteMapping("/ingredients/{id}")
    @RolesAllowed("bakmix-admin")
    public ResponseEntity<Ingredient> deleteContact(@PathVariable String id) {
        boolean success =  ingredientService.delete(Long.parseLong(id));
        if (success == false) {
            log.error("Failed to delete ingredient number " + id);
            throw new IngredientNotFoundException("id-" + id);
        }
        log.info("Deleted ingredient number " + id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Retrieve a list of expired ingredients. Service layer checks whether expiry date is before the current system date.
     * @return The List of expired ingredients
     */
    @GetMapping("/expiredingredients")
    @RolesAllowed("bakmix-admin")
    public ResponseEntity<List<Ingredient>> getAlExpired() {
        List<Ingredient> ingredient = ingredientService.expiredIngredients();
        log.info("Retrieved all expired ingredients");
        return ResponseEntity.ok(ingredient);
    }
}
