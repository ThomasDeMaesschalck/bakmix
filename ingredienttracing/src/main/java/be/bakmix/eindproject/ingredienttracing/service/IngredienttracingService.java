package be.bakmix.eindproject.ingredienttracing.service;

import be.bakmix.eindproject.ingredienttracing.business.IngredienttracingEntity;
import be.bakmix.eindproject.ingredienttracing.business.repository.IngredienttracingRepository;
import be.bakmix.eindproject.ingredienttracing.service.dto.Ingredienttracing;
import be.bakmix.eindproject.ingredienttracing.service.mapper.IngredienttracingMapper;
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
public class IngredienttracingService {
    private static List<Ingredienttracing> ingredienttracings = new ArrayList<>();

    @Autowired
    private IngredienttracingRepository ingredienttracingRepository;

    @Autowired
    private IngredienttracingMapper ingredienttracingMapper;

    public List<Ingredienttracing> getAll(){
        List<Ingredienttracing> ingredienttracings = StreamSupport
                .stream(ingredienttracingRepository.findAll().spliterator(), false)
                .map(e -> ingredienttracingMapper.toDTO(e))
                .collect(Collectors.toList());
        return ingredienttracings;
    }

    public Ingredienttracing getById(Long id){
        Optional<IngredienttracingEntity> ingredienttracingEntityOptional = ingredienttracingRepository.findById(id);
        if(ingredienttracingEntityOptional.isPresent()){
            return ingredienttracingMapper.toDTO(ingredienttracingEntityOptional.get());
        }
        return null;
    }

    public List<Ingredienttracing> getByOrderlineId(Long id){
        List<Ingredienttracing> ingredienttracings = StreamSupport
                .stream(ingredienttracingRepository.findAll().spliterator(), false)
                .filter(i -> i.getOrderlineId() == id)
                .map (e -> ingredienttracingMapper.toDTO(e))
                .collect(Collectors.toList());

        return ingredienttracings;
    }

    public void save(Ingredienttracing ingredienttracing) {
        IngredienttracingEntity ingredientToSave = ingredienttracingMapper.toEntity(ingredienttracing);
        ingredienttracingRepository.save(ingredientToSave);
    }


    public Ingredienttracing delete(Long id) {
        Iterator<IngredienttracingEntity> it = ingredienttracingRepository.findAll().iterator();
        while (it.hasNext())   {
            IngredienttracingEntity ingredienttracing = it.next();
            if(ingredienttracing.getId() == id)
            {
                ingredienttracingRepository.delete(ingredienttracing);
                it.remove();
                return ingredienttracingMapper.toDTO(ingredienttracing);
            }
        }
        return null;
    }

}
