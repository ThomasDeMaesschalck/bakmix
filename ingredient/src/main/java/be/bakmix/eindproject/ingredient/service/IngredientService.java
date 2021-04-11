package be.bakmix.eindproject.ingredient.service;

import be.bakmix.eindproject.ingredient.business.IngredientEntity;
import be.bakmix.eindproject.ingredient.business.repository.IngredientRepository;
import be.bakmix.eindproject.ingredient.service.dto.Ingredient;
import be.bakmix.eindproject.ingredient.service.dto.Ingredienttracing;
import be.bakmix.eindproject.ingredient.service.mapper.IngredientMapper;
import lombok.AllArgsConstructor;
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

@Service
public class IngredientService {

    private static List<Ingredient> ingredients = new ArrayList<>();

    private Map ingredientTracingsMap = new HashMap();

    @Autowired
    private KeycloakRestTemplate keycloakRestTemplate;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private IngredientMapper ingredientMapper;

    @Value("http://localhost:7771/api/ingredienttracings/")
    private String urlIngredienttracings;

    public IngredientService(IngredientRepository ingredientRepository, IngredientMapper ingredientMapper)
    {
        this.ingredientRepository = ingredientRepository;
        this.ingredientMapper = ingredientMapper;
    }

    public Page<Ingredient> getAll(Integer pageNo, Integer pageSize, String sortBy){
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.DESC, sortBy));
        ingredientTracings();
        Page<Ingredient> ingredients = ingredientRepository.findAll(paging).map(ingredientMapper::toDTO);
        for (Ingredient i: ingredients)
        {
            if (ingredientTracingsMap.containsKey(i.getId()))
            {
                i.setLinked(true); //boolean geeft aan dat ingrediënt gekoppeld is aan orderlijnen
            }
        }
        return ingredients;
    }

    public List<Ingredient> getAllNoPagination(){
        List<Ingredient> ingredients = StreamSupport
                .stream(ingredientRepository.findAll().spliterator(), false)
                .map(i -> ingredientMapper.toDTO(i))
                .collect(Collectors.toList());
        return ingredients;
    }

    public Ingredient getById(Long id){
        Optional<IngredientEntity> ingredientEntityOptional = ingredientRepository.findById(id);
        if(ingredientEntityOptional.isPresent()){
            return ingredientMapper.toDTO(ingredientEntityOptional.get());
        }
        return null;
    }

    public void save(Ingredient ingredient) {
        IngredientEntity contactToSave = ingredientMapper.toEntity(ingredient);
       if((!findDuplicateUniqueIdBoolean(ingredient.getUniqueCode()) || (ingredient.getId() > 0)))
       {ingredientRepository.save(contactToSave);}
    }

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

    public boolean findDuplicateUniqueIdBoolean(String uniqueId)
    {
        boolean duplicate = false;
        List<Ingredient> checkDuplicate = getAllNoPagination().stream().filter(i -> i.getUniqueCode().equals(uniqueId)).collect(Collectors.toList());
        if(checkDuplicate.size() > 0)
        {
            duplicate = true;
           return duplicate;
        }
        return duplicate;
    }

    public boolean delete(Long id) {

        Optional<IngredientEntity> ingredientEntityOptional = ingredientRepository.findById(id);
        if(ingredientEntityOptional.isPresent()) {
            ingredientRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Ingredient> expiredIngredients(){
        LocalDate today = LocalDate.now();
        List<Ingredient> ingredients =  StreamSupport
                .stream(ingredientRepository.findAll().spliterator(), false)
                .filter(i -> i.getAvailable())
                .filter(i -> !i.getExpiry().isAfter(today))
                .map(i -> ingredientMapper.toDTO(i))
                .collect(Collectors.toList());
        return ingredients;
    }

    public Map<Long, Long> ingredientTracings(){ //stuk code om aan te geven dat een ingrediënt gekoppeld is aan orderlijnen
        Ingredienttracing[] ingredienttracings = keycloakRestTemplate.getForObject(urlIngredienttracings, Ingredienttracing[].class);
        Arrays.stream(ingredienttracings).forEach(i ->
                ingredientTracingsMap.put(i.getIngredientId(), i.getOrderlineId()));
        return ingredientTracingsMap;
    }


    }
