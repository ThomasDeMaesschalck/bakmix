package be.bakmix.eindproject.ingredient.service;

import be.bakmix.eindproject.ingredient.business.IngredientEntity;
import be.bakmix.eindproject.ingredient.business.repository.IngredientRepository;
import be.bakmix.eindproject.ingredient.service.dto.Ingredient;
import be.bakmix.eindproject.ingredient.service.dto.Ingredienttracing;
import be.bakmix.eindproject.ingredient.service.mapper.IngredientMapper;
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
public class IngredientServiceTest {

    @InjectMocks
    IngredientService ingredientService;

    @Mock
    IngredientRepository ingredientRepository;

    @Mock
    IngredientMapper ingredientMapper;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    KeycloakRestTemplate keycloakRestTemplate;

    @Value("http://localhost:7771/api/ingredienttracings/")
    private String urlIngredienttracings;

    IngredientEntity i1;
    IngredientEntity i2;
    Ingredient i1DTO;
    Ingredient i2DTO;
    Ingredienttracing[] tracingArray;
    Ingredienttracing t1;
    Ingredienttracing t2;
    Ingredienttracing t3;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        ingredientService = new IngredientService(ingredientRepository, ingredientMapper, keycloakRestTemplate);

        i1 = new IngredientEntity();
        i1.setId(Long.parseLong("1"));
        i1.setUniqueCode("AAA");
        i1.setAvailable(true);
        i1.setExpiry(LocalDate.now().minusDays(1));
        i1.setBrand("Bakshop");

        i2 = new IngredientEntity();
        i2.setUniqueCode("BBB");
        i2.setAvailable(true);
        i2.setExpiry(LocalDate.now().plusDays(1000));
        i2.setId(Long.parseLong("2"));

        i1DTO = new Ingredient();
        i1DTO.setBrand("Bakshop");
        i1DTO.setUniqueCode("AAA");
        i1DTO.setAvailable(true);
        i1DTO.setExpiry(LocalDate.now().minusDays(1));
        i1DTO.setId(Long.parseLong("1"));

        i2DTO = new Ingredient();
        i2DTO.setUniqueCode("BBB");
        i1DTO.setAvailable(true);
        i2DTO.setExpiry(LocalDate.now().plusDays(1000));
        i2DTO.setId(Long.parseLong("2"));

        t1 = new Ingredienttracing();
        t1.setId(Long.parseLong("1"));
        t1.setIngredientId(Long.parseLong("1"));
        t1.setOrderlineId(Long.parseLong("5"));

        t2 = new Ingredienttracing();
        t2.setId(Long.parseLong("2"));
        t2.setIngredientId(Long.parseLong("1"));
        t2.setOrderlineId(Long.parseLong("10"));

        t3 = new Ingredienttracing();
        t3.setId(Long.parseLong("3"));
        t3.setIngredientId(Long.parseLong("2"));
        t3.setOrderlineId(Long.parseLong("20"));
    }

    @Test
    public void getAllTest() {
        List<IngredientEntity> list = new ArrayList<>();
        list.add(i1);

        List<Ingredienttracing> listOfTracings = new ArrayList<>();
        listOfTracings.add(t1);
        listOfTracings.add(t2);
        listOfTracings.add(t3);
        tracingArray = listOfTracings.toArray(new Ingredienttracing[0]);

        Mockito.when(keycloakRestTemplate.getForObject(Mockito.eq(urlIngredienttracings), Mockito.eq(Ingredienttracing[].class))).thenReturn(tracingArray);

        when(ingredientMapper.toDTO(i1)).thenReturn(i1DTO);
        when(ingredientMapper.toDTO(i2)).thenReturn(i2DTO);

        Page<IngredientEntity> pagedList = new PageImpl<>(list);
        Pageable paging = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "id"));
        given(ingredientRepository.findAll(paging)).willReturn(pagedList);
        Page<Ingredient> ingredientsPage = ingredientService.getAll(0, 10, "id");

        //page should have 1 object
        assertEquals(1, ingredientsPage.toList().size());

        list.add(i2);
        Page<IngredientEntity> pagedList2 = new PageImpl<>(list);
        given(ingredientRepository.findAll(paging)).willReturn(pagedList2);
        Page<Ingredient> ingredientsPage2 = ingredientService.getAll(0, 10, "id");

        //size should be 2 now
        assertEquals(2, ingredientsPage2.toList().size());
    }

    @Test
    public void getAllNoPaginationTest() {
        List<IngredientEntity> list = new ArrayList<>();
        list.add(i1);

        when(ingredientMapper.toDTO(i1)).thenReturn(i1DTO);
        when(ingredientMapper.toDTO(i2)).thenReturn(i2DTO);

        given(ingredientRepository.findAll()).willReturn(list);
        List<Ingredient> listFromService = ingredientService.getAllNoPagination();

        //size should be 1 now
        assertEquals(1, listFromService.size());

        list.add(i2);
        given(ingredientRepository.findAll()).willReturn(list);
        List<Ingredient> listFromService2 = ingredientService.getAllNoPagination();

        //size should increase to 2 now
        assertEquals(2, listFromService2.size());
    }

  @Test
    public void getByIdTest(){

        when(ingredientMapper.toDTO(i1)).thenReturn(i1DTO);
        given(ingredientRepository.findById(Long.parseLong("1"))).willReturn(java.util.Optional.ofNullable(i1));

        Ingredient ingredientFromTest = ingredientService.getById(Long.parseLong("1"));

        //check if brand name is indeed Bakshop
        assertEquals("Bakshop", ingredientFromTest.getBrand());
    }

    @Test
    public void findByUniqueIdTest(){
        List<IngredientEntity> list = new ArrayList<>();
        list.add(i1);
        list.add(i2);

        given(ingredientRepository.findAll()).willReturn(list);

        when(ingredientMapper.toDTO(i1)).thenReturn(i1DTO);
        when(ingredientMapper.toDTO(i2)).thenReturn(i2DTO);

        Ingredient ingredientFromTest = ingredientService.findByUniqueId("AAA");

        //ingredient id should be 1
        assertEquals(1, ingredientFromTest.getId().intValue());

        Ingredient ingredientFromTest2 = ingredientService.findByUniqueId("BBB");

        //ingredient id should be 2 here
        assertEquals(2, ingredientFromTest2.getId().intValue());
    }

    @Test
    public void findDuplicateUniqueIdBooleanTest(){
        List<IngredientEntity> list = new ArrayList<>();
        list.add(i1);
        list.add(i2);

        given(ingredientRepository.findAll()).willReturn(list);

        when(ingredientMapper.toDTO(i1)).thenReturn(i1DTO);
        when(ingredientMapper.toDTO(i2)).thenReturn(i2DTO);

        boolean check1 = ingredientService.findDuplicateUniqueIdBoolean("AAA");
        assertEquals(true, check1); //should return true

        boolean check2 = ingredientService.findDuplicateUniqueIdBoolean("BBB");
        assertEquals(true, check2); //should return true


        boolean check3 = ingredientService.findDuplicateUniqueIdBoolean("CCC");
        assertEquals(false, check3); //should return false, this is not a duplicate

    }


    @Test
    public void expiredIngredientsTest(){
        List<IngredientEntity> list = new ArrayList<>();
        list.add(i1);
        list.add(i2);

        given(ingredientRepository.findAll()).willReturn(list);

        when(ingredientMapper.toDTO(i1)).thenReturn(i1DTO);
//      when(ingredientMapper.toDTO(i2)).thenReturn(i2DTO);

        List<Ingredient> expiredIngredients = ingredientService.expiredIngredients();
        assertEquals(1, expiredIngredients.size()); //there should be one ingredient with a date beyond expiry date
    }

    @Test
    public void deleteTest(){

        given(ingredientRepository.findById(Long.parseLong("1"))).willReturn(java.util.Optional.ofNullable(i1));
        boolean result =  ingredientService.delete(Long.parseLong("1"));
        assertEquals(true, result); //should return true as this element exists

        given(ingredientRepository.findById(Long.parseLong("555"))).willReturn(java.util.Optional.ofNullable(null));
        boolean result2 =  ingredientService.delete(Long.parseLong("555"));
        assertEquals(false, result2); //should return false as this element is not present in repository
    }
}
