package be.bakmix.eindproject.ingredienttracing.service;

import be.bakmix.eindproject.ingredienttracing.business.IngredienttracingEntity;
import be.bakmix.eindproject.ingredienttracing.business.repository.IngredienttracingRepository;
import be.bakmix.eindproject.ingredienttracing.service.dto.Ingredienttracing;
import be.bakmix.eindproject.ingredienttracing.service.mapper.IngredienttracingMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Service layer of the Ingredienttracing microservice
 */
@Service
@AllArgsConstructor
public class IngredienttracingService {

      /**
     * The ingredienttracing repository
     */
    @Autowired
    private final IngredienttracingRepository ingredienttracingRepository;

    /**
     * The ingredienttracing mapping class
     */
    @Autowired
    private final IngredienttracingMapper ingredienttracingMapper;

    /**
     * Retrieve a List of all persisted ingredienttracings
     * @return List of all ingredienttracings
     */
    public List<Ingredienttracing> getAll(){
        List<Ingredienttracing> ingredienttracings = StreamSupport
                .stream(ingredienttracingRepository.findAll().spliterator(), false)
                .map(ingredienttracingMapper::toDTO)
                .collect(Collectors.toList());
        return ingredienttracings;
    }

    /**
     * Get all tracings for a specific Orderline
     * @param id The id of the Orderline
     * @return A List of ingredienttracings for the specified Orderline id.
     */
    public List<Ingredienttracing> getByOrderlineId(Long id){
        List<Ingredienttracing> ingredienttracings = StreamSupport
                .stream(ingredienttracingRepository.findAll().spliterator(), false)
                .filter(i -> i.getOrderlineId() == id)
                .map (ingredienttracingMapper::toDTO)
                .collect(Collectors.toList());

        return ingredienttracings;
    }

    /**
     * Save an Ingredienttracing to the DB.
     * @param ingredienttracing The ingredienttracing that needs to be persisted
     */
    public void save(Ingredienttracing ingredienttracing) {
        IngredienttracingEntity ingredientToSave = ingredienttracingMapper.toEntity(ingredienttracing);
        ingredienttracingRepository.save(ingredientToSave);
    }

    /**
     * Delete an Ingredienttracing
     * @param id The Ingredienttracing id that needs to be deleted
     * @return Boolean true if successful
     */
    public boolean delete(Long id) {

        Optional<IngredienttracingEntity> ingredienttracingEntityOptional = ingredienttracingRepository.findById(id);
        if(ingredienttracingEntityOptional.isPresent()) {
            ingredienttracingRepository.deleteById(id);
        return true;
        }
        return false;
    }

}
