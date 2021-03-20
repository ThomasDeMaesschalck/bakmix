package be.bakmix.eindproject.ingredienttracing.web.rest;

import be.bakmix.eindproject.ingredienttracing.service.IngredienttracingService;
import be.bakmix.eindproject.ingredienttracing.service.dto.Ingredienttracing;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<List<Ingredienttracing>> getAll() {
        List<Ingredienttracing> ingredienttracings = ingredienttracingService.getAll();
        log.info("Retrieved all ingredienttracings");
        return ResponseEntity.ok(ingredienttracings);
    }

    @GetMapping("/ingredienttracings/{id}")
    public ResponseEntity<Ingredienttracing> getById(@PathVariable Long id){
        Ingredienttracing ingredienttracing = ingredienttracingService.getById(id);
        if(ingredienttracing == null){
            log.error("Failed to find ingredienttracing number " + id);

            return ResponseEntity.notFound().build();
        }
        log.info("Retrieved ingredienttracing number " + id);
        return ResponseEntity.ok(ingredienttracing);
    }
}
