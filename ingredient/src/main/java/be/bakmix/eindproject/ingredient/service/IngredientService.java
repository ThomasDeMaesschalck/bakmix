package be.bakmix.eindproject.ingredient.service;

import be.bakmix.eindproject.ingredient.business.IngredientEntity;
import be.bakmix.eindproject.ingredient.business.repository.IngredientRepository;
import be.bakmix.eindproject.ingredient.service.dto.Ingredient;
import be.bakmix.eindproject.ingredient.service.dto.Ingredienttracing;
import be.bakmix.eindproject.ingredient.service.mapper.IngredientMapper;
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.google.common.collect.MoreCollectors.onlyElement;

/**
 * Service layer for the Ingredient microservice.
 */
@Service
public class IngredientService {

    /**
     * HashMap used for checking whether an ingredient is linked to an orderline. HashMap as duplicate keys are not needed.
     */
    private Map<Long, Long> ingredientTracingsMap = new HashMap<>();

    /**
     * Rest template with keycloak implementation, used to access the Ingredienttracing microservice
     */
    @Autowired
    private KeycloakRestTemplate keycloakRestTemplate;

    /**
     * The repository of ingredients
     */
    @Autowired
    private final IngredientRepository ingredientRepository;

    /**
     * The ingredient mapping class
     */
    @Autowired
    private final IngredientMapper ingredientMapper;

    /**
     * URL of the Ingredienttracing microservice API
     */
    @Value("http://localhost:7771/api/ingredienttracings/")
    private String urlIngredienttracings;

    /**
     * Constructor class
     * @param ingredientRepository The ingredient repository
     * @param ingredientMapper The ingredient mapper
     */
    public IngredientService(IngredientRepository ingredientRepository, IngredientMapper ingredientMapper)
    {
        this.ingredientRepository = ingredientRepository;
        this.ingredientMapper = ingredientMapper;
    }

    /**
     * Get the requested Page of Ingredients from the database.
     * Gets all the ingredients from the database and adjusts linked boolean to true if the ingredient is found in an existing tracing.
     * Boolean linked is used in the view to disable the delete button as it's not possible to remove ingredients that are used in one or more orderlines.
     * @param pageNo The page number
     * @param pageSize The page size
     * @param sortBy The sort filter
     * @return Returns the requested page.
     */
    public Page<Ingredient> getAll(Integer pageNo, Integer pageSize, String sortBy){
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.DESC, sortBy));
        ingredientTracings();
        Page<Ingredient> ingredients = ingredientRepository.findAll(paging).map(ingredientMapper::toDTO);
        for (Ingredient i: ingredients)
        {
            if (ingredientTracingsMap.containsKey(i.getId()))
            {
                i.setLinked(true);
            }
        }
        return ingredients;
    }

    /**
     * Get a list of all ingredients, without pagination
     * @return A List of all ingredients persisted in the database
     */

    public List<Ingredient> getAllNoPagination(){
        return StreamSupport
                .stream(ingredientRepository.findAll().spliterator(), false)
                .map(ingredientMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get a specific ingredient
     * @param id The unique id of this ingredient
     * @return The requested Ingredient
     */

    public Ingredient getById(Long id){
        Optional<IngredientEntity> ingredientEntityOptional = ingredientRepository.findById(id);
        if(ingredientEntityOptional.isPresent()){
            return ingredientMapper.toDTO(ingredientEntityOptional.get());
        }
        return null;
    }

    /**
     * Save an ingredient. Performs a check to ensure the unique code of the Ingredient is not already present in the DB.
     * @param ingredient The Ingredient that needs to be persisted
     */
    public void save(Ingredient ingredient) {
        IngredientEntity contactToSave = ingredientMapper.toEntity(ingredient);
       if((!findDuplicateUniqueIdBoolean(ingredient.getUniqueCode()) || (ingredient.getId() > 0)))
       {ingredientRepository.save(contactToSave);}
    }

    /**
     * Find an Ingredient based on the uniqueId
     * @param uniqueId The uniqueId of the requested Ingredient
     * @return Returns the requested Ingredient
     */
    public Ingredient findByUniqueId(String uniqueId)
    {
        try {
             Ingredient foundByUniqueId = getAllNoPagination().stream().filter(i -> i.getUniqueCode().equals(uniqueId)).collect(onlyElement());
            return foundByUniqueId;
        }
        catch (Exception e)
        {throw new RuntimeException(e);
        }
    }

    /**
     * Checks whether the DB has an ingredient with the provided uniqueId
     * @param uniqueId The uniqueId of an ingredient that needs to be saved
     * @return A boolean that indicates whether it's a duplicate or not
     */

    public boolean findDuplicateUniqueIdBoolean(String uniqueId)
    {
        List<Ingredient> checkDuplicate = getAllNoPagination().stream().filter(i -> i.getUniqueCode().equals(uniqueId)).collect(Collectors.toList());
        if(checkDuplicate.size() > 0)
        {
           return true;
        }
        return false;
    }

    /**
     * Delete an Ingredient from the database
     * @param id The id of the Ingredient that needs to be deleted
     * @return Returns true if found and deleted, returns false if the Ingredient was not found
     */
    public boolean delete(Long id) {

        Optional<IngredientEntity> ingredientEntityOptional = ingredientRepository.findById(id);
        if(ingredientEntityOptional.isPresent()) {
            ingredientRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Generate a List of expired ingredients.
     * This method iterates over all persisted ingredients with boolean available set to true.
     * A filter is used so only ingredients with an expiry date before today are returned.
     * @return The list of expired ingredients
     */

    public List<Ingredient> expiredIngredients(){
        LocalDate today = LocalDate.now();
        List<Ingredient> ingredients =  StreamSupport
                .stream(ingredientRepository.findAll().spliterator(), false)
                .filter(IngredientEntity::getAvailable)
                .filter(i -> !i.getExpiry().isAfter(today))
                .map(ingredientMapper::toDTO)
                .collect(Collectors.toList());
        return ingredients;
    }

    /**
     * Call the Ingredienttracing microservice to retrieve all tracings. This method generates a Map that's used by the getAll method to check
     * whether an ingredient is linked to orderlines. A Map is used because an ingredient can have multiple tracing entries. Duplicates are irrelevant for the end purpose.
     */
    public void ingredientTracings(){ //stuk code om aan te geven dat een ingrediënt gekoppeld is aan orderlijnen
        Ingredienttracing[] ingredienttracings = keycloakRestTemplate.getForObject(urlIngredienttracings, Ingredienttracing[].class);
        Arrays.stream(ingredienttracings).forEach(i ->
                ingredientTracingsMap.put(i.getIngredientId(), i.getOrderlineId()));
    }


    }
