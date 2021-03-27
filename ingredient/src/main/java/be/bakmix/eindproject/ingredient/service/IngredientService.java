package be.bakmix.eindproject.ingredient.service;

import be.bakmix.eindproject.ingredient.business.IngredientEntity;
import be.bakmix.eindproject.ingredient.business.repository.IngredientRepository;
import be.bakmix.eindproject.ingredient.service.dto.Ingredient;
import be.bakmix.eindproject.ingredient.service.mapper.IngredientMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@AllArgsConstructor
public class IngredientService {

    private static List<Ingredient> ingredients = new ArrayList<>();

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private IngredientMapper ingredientMapper;

    public List<Ingredient> getAll(){
        List<Ingredient> ingredients = StreamSupport
                .stream(ingredientRepository.findAll().spliterator(), false)
                .map(e -> ingredientMapper.toDTO(e))
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
        ingredientRepository.save(contactToSave);
    }

    public Ingredient findDuplicateUniqueId(String uniqueId)
    {
        Ingredient duplicate = new Ingredient();
        List<Ingredient> checkDuplicate = getAll().stream().filter(i -> i.getUniqueCode().equals(uniqueId)).collect(Collectors.toList());
        if(checkDuplicate.size() > 0)
        {
            duplicate.setUniqueCode("duplicate");
            return duplicate;
        }
        return null;
    }

    public Ingredient delete(Long id) {
        Iterator<IngredientEntity> it = ingredientRepository.findAll().iterator();
        while (it.hasNext())   {
            IngredientEntity ingredient = it.next();
            if(ingredient.getId() == id)
            {
                ingredientRepository.delete(ingredient);
                it.remove();
                return ingredientMapper.toDTO(ingredient);
            }
        }
        return null;
    }

    }
