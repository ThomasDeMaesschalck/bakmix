package be.bakmix.eindproject.ingredienttracing.web.rest;

import be.bakmix.eindproject.ingredienttracing.business.IngredienttracingEntity;
import be.bakmix.eindproject.ingredienttracing.service.IngredienttracingService;
import be.bakmix.eindproject.ingredienttracing.service.dto.Ingredienttracing;
import be.bakmix.eindproject.ingredienttracing.web.rest.exceptions.IngredienttracingNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api")
@AllArgsConstructor
@Log4j2
public class IngredienttracingResource {

    @Autowired
    private IngredienttracingService ingredienttracingService;

    @GetMapping("/ingredienttracings")
    @RolesAllowed("bakmix-admin")
    public ResponseEntity<List<Ingredienttracing>> getAll() {
        List<Ingredienttracing> ingredienttracings = ingredienttracingService.getAll();
        log.info("Retrieved all ingredienttracings");
        return ResponseEntity.ok(ingredienttracings);
    }

  //  @GetMapping("/ingredienttracings/{id}")
  //  public ResponseEntity<Ingredienttracing> getById(@PathVariable Long id){
 //       Ingredienttracing ingredienttracing = ingredienttracingService.getById(id);
   //     if(ingredienttracing == null){
 //          log.error("Failed to find ingredienttracing number " + id);

    //        return ResponseEntity.notFound().build();
 //       }
    //    log.info("Retrieved ingredienttracing number " + id);
  //      return ResponseEntity.ok(ingredienttracing);
 //   }

    @GetMapping("/ingredienttracings/{id}") //works by orderlineId
    @RolesAllowed("bakmix-admin")
    public ResponseEntity<List<Ingredienttracing>> getByOrderlineId(@PathVariable Long id){
        List<Ingredienttracing> ingredienttracing = ingredienttracingService.getByOrderlineId(id);
        if(ingredienttracing == null){
            log.error("Failed to find ingredienttracing for oderline number " + id);

            return ResponseEntity.notFound().build();
        }
        log.info("Retrieved ingredienttracing for oderline number " + id);
        return ResponseEntity.ok(ingredienttracing);
    }

    @PostMapping("/ingredienttracings")
    @RolesAllowed("bakmix-admin")
    public ResponseEntity<Ingredienttracing> createIngredienttracing(@Valid @RequestBody Ingredienttracing ingredienttracing)
    {
        ingredienttracingService.save(ingredienttracing);
        log.info("Saved ingredienttracing " + ingredienttracing);
        return ResponseEntity.ok(ingredienttracing);
    }

    @DeleteMapping("/ingredienttracings/{id}")
    @RolesAllowed("bakmix-admin")
    public ResponseEntity<Ingredienttracing> deleteIngredienttracing(@PathVariable Long id) {
        boolean success =  ingredienttracingService.delete(id);
        if (success == false) {
            log.error("Failed to delete ingredienttracing number " + id);
            throw new IngredienttracingNotFoundException("id-" + id);
        }
        log.info("Deleted ingredienttracing number " + id);
        return ResponseEntity.noContent().build();
    }

}
