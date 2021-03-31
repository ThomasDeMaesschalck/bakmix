package be.bakmix.eindproject.ingredient.service;

import be.bakmix.eindproject.ingredient.business.IngredientEntity;
import be.bakmix.eindproject.ingredient.business.repository.IngredientRepository;
import be.bakmix.eindproject.ingredient.service.dto.Ingredient;
import be.bakmix.eindproject.ingredient.service.mapper.IngredientMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.google.common.collect.MoreCollectors.onlyElement;

@Service
@AllArgsConstructor
public class IngredientService {

    private static List<Ingredient> ingredients = new ArrayList<>();

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private IngredientMapper ingredientMapper;



    public Page<Ingredient> getAll(Integer pageNo, Integer pageSize, String sortBy){
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.DESC, sortBy));

        Page<Ingredient> ingredients = ingredientRepository.findAll(paging).map(ingredientMapper::toDTO);

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

    }
