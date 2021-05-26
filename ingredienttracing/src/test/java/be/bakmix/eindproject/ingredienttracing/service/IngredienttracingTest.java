package be.bakmix.eindproject.ingredienttracing.service;

import be.bakmix.eindproject.ingredienttracing.business.IngredienttracingEntity;
import be.bakmix.eindproject.ingredienttracing.business.repository.IngredienttracingRepository;
import be.bakmix.eindproject.ingredienttracing.service.dto.Ingredienttracing;
import be.bakmix.eindproject.ingredienttracing.service.mapper.IngredienttracingMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@SpringBootTest()
public class IngredienttracingTest {

    @InjectMocks
    IngredienttracingService ingredientTracingService;

    @Mock
    IngredienttracingRepository ingredienttracingRepository;

    @Mock
    IngredienttracingMapper ingredienttracingMapper;

    IngredienttracingEntity t1;
    IngredienttracingEntity t2;
    IngredienttracingEntity t3;
    IngredienttracingEntity t4;

    Ingredienttracing t1DTO;
    Ingredienttracing t2DTO;
    Ingredienttracing t3DTO;
    Ingredienttracing t4DTO;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        ingredientTracingService = new IngredienttracingService(ingredienttracingRepository, ingredienttracingMapper);


        t1 = new IngredienttracingEntity();
        t1.setId(Long.parseLong("1"));
        t1.setIngredientId(Long.parseLong("1"));
        t1.setOrderlineId(Long.parseLong("5"));

        t2 = new IngredienttracingEntity();
        t2.setId(Long.parseLong("2"));
        t2.setIngredientId(Long.parseLong("1"));
        t2.setOrderlineId(Long.parseLong("10"));

        t3 = new IngredienttracingEntity();
        t3.setId(Long.parseLong("3"));
        t3.setIngredientId(Long.parseLong("2"));
        t3.setOrderlineId(Long.parseLong("20"));

        t4 = new IngredienttracingEntity();
        t4.setId(Long.parseLong("4"));
        t4.setIngredientId(Long.parseLong("10"));
        t4.setOrderlineId(Long.parseLong("5"));

        t1DTO = new Ingredienttracing();
        t1DTO.setId(Long.parseLong("1"));
        t1DTO.setIngredientId(Long.parseLong("1"));
        t1DTO.setOrderlineId(Long.parseLong("5"));

        t2DTO = new Ingredienttracing();
        t2DTO.setId(Long.parseLong("2"));
        t2DTO.setIngredientId(Long.parseLong("1"));
        t2DTO.setOrderlineId(Long.parseLong("10"));

        t3DTO = new Ingredienttracing();
        t3DTO.setId(Long.parseLong("3"));
        t3DTO.setIngredientId(Long.parseLong("2"));
        t3DTO.setOrderlineId(Long.parseLong("20"));

        t4DTO = new Ingredienttracing();
        t4DTO.setId(Long.parseLong("4"));
        t4DTO.setIngredientId(Long.parseLong("10"));
        t4DTO.setOrderlineId(Long.parseLong("5"));
    }

    @Test
    public void getAllTest(){
        List<IngredienttracingEntity> list = new ArrayList<>();
        list.add(t1);
        list.add(t2);
        list.add(t3);

        when(ingredienttracingMapper.toDTO(t1)).thenReturn(t1DTO);
        when(ingredienttracingMapper.toDTO(t2)).thenReturn(t2DTO);
        when(ingredienttracingMapper.toDTO(t3)).thenReturn(t3DTO);

        given(ingredienttracingRepository.findAll()).willReturn(list);
        List<Ingredienttracing> listFromService = ingredientTracingService.getAll();

        assertEquals(3, listFromService.size());

    }

    @Test
    public void getByOrderlineIdTest(){
        List<IngredienttracingEntity> list = new ArrayList<>();
        list.add(t1);
        list.add(t2);
        list.add(t3);

        when(ingredienttracingMapper.toDTO(t1)).thenReturn(t1DTO);
        //when(ingredienttracingMapper.toDTO(t2)).thenReturn(t2DTO);
        //when(ingredienttracingMapper.toDTO(t3)).thenReturn(t3DTO);

        given(ingredienttracingRepository.findAll()).willReturn(list);
        List<Ingredienttracing> listFromService = ingredientTracingService.getByOrderlineId(Long.parseLong("5"));

        assertEquals(1, listFromService.size());

        list.add(t4);
        when(ingredienttracingMapper.toDTO(t4)).thenReturn(t4DTO);
        given(ingredienttracingRepository.findAll()).willReturn(list);
        List<Ingredienttracing> listFromService2 = ingredientTracingService.getByOrderlineId(Long.parseLong("5"));

        assertEquals(2, listFromService2.size());

    }


}
