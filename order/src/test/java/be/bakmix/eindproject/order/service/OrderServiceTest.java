package be.bakmix.eindproject.order.service;

import be.bakmix.eindproject.order.business.OrderEntity;
import be.bakmix.eindproject.order.business.repository.OrderRepository;
import be.bakmix.eindproject.order.service.dto.*;
import be.bakmix.eindproject.order.service.mapper.OrderMapper;
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
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@SpringBootTest()
public class OrderServiceTest {

    @InjectMocks
    OrderService orderService;

    @Mock
    OrderRepository orderRepository;

    @Mock
    OrderMapper orderMapper;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    KeycloakRestTemplate keycloakRestTemplate;

    Order o1DTO;
    Order o2DTO;
    Order o3DTO;

    OrderEntity o1;
    OrderEntity o2;
    OrderEntity o3;

    Product product;
    Orderline ol1;
    Orderline ol2;
    Orderline ol3;
    Customer c1;
    Ingredient i1;
    Ingredient i2;
    Ingredient i3;
    Ingredient i4;
    Ingredienttracing t1;
    Ingredienttracing t2;


    @Value("http://localhost:7771/api/ingredienttracings/")
    private String urlIngredienttracings;

    @Value("http://localhost:7777/api/ingredients/")
    private String urlIngredients;

    @Value("http://localhost:7773/api/orderlines/getbyorder/")
    private String urlOrderlines;

    @Value("http://localhost:7773/api/orderlines/")
    private String urlOrderlinesById;

    @Value("http://localhost:7779/api/customers/")
    private String urlCustomers;


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        orderService = new OrderService(orderRepository, orderMapper, keycloakRestTemplate);

        o1 = new OrderEntity();
        o1.setId(Long.parseLong("1"));
        o1.setStatus(Long.parseLong("0"));
        o1.setCustomerId(Long.parseLong("1"));
        o1.setDate(LocalDate.now());
        o1.setDiscount(BigDecimal.valueOf(0));
        o1.setShippingCost(BigDecimal.valueOf(5));

        o2 = new OrderEntity();
        o2.setId(Long.parseLong("1"));
        o2.setStatus(Long.parseLong("0"));
        o2.setCustomerId(Long.parseLong("1"));
        o2.setDate(LocalDate.now());
        o2.setDiscount(BigDecimal.valueOf(0));
        o2.setShippingCost(BigDecimal.valueOf(5));


        o1DTO = new Order();
        o1DTO.setId(Long.parseLong("1"));
        o1DTO.setStatus(Long.parseLong("0"));
        o1DTO.setCustomerId(Long.parseLong("1"));
        o1DTO.setDate(LocalDate.now());
        o1DTO.setDiscount(BigDecimal.valueOf(0));
        o1DTO.setShippingCost(BigDecimal.valueOf(5));

        o2DTO = new Order();
        o2DTO.setId(Long.parseLong("1"));
        o2DTO.setStatus(Long.parseLong("0"));
        o2DTO.setCustomerId(Long.parseLong("1"));
        o2DTO.setDate(LocalDate.now());
        o2DTO.setDiscount(BigDecimal.valueOf(0));
        o2DTO.setShippingCost(BigDecimal.valueOf(5));

        ol1 = new Orderline();
        ol1.setId(Long.parseLong("1"));
        ol1.setOrderId(Long.parseLong("1"));
        ol1.setQty(Long.parseLong("2"));
        ol1.setPurchasePrice(BigDecimal.valueOf(5.50));
        ol1.setProductId(Long.parseLong("1"));

        ol2 = new Orderline();
        ol2.setId(Long.parseLong("2"));
        ol2.setOrderId(Long.parseLong("1"));
        ol2.setQty(Long.parseLong("2"));
        ol2.setPurchasePrice(BigDecimal.valueOf(5.50));
        ol2.setProductId(Long.parseLong("1"));

        ol3 = new Orderline();
        ol3.setId(Long.parseLong("3"));
        ol3.setOrderId(Long.parseLong("2"));
        ol3.setQty(Long.parseLong("2"));
        ol3.setPurchasePrice(BigDecimal.valueOf(5.50));
        ol3.setProductId(Long.parseLong("1"));

        product = new Product();
        product.setId(Long.parseLong("1"));
        product.setPrice(BigDecimal.valueOf(10));
        product.setName("Bloem");
        product.setVat(6);

        c1 = new Customer();
        c1.setId(Long.parseLong("1"));
        c1.setFirstName("Tom");
        c1.setLastName("Van Lokeren");

        i1 = new Ingredient();
        i1.setBrand("Bakshop");
        i1.setUniqueCode("AAA");
        i1.setAvailable(true);
        i1.setExpiry(LocalDate.now().minusDays(1));
        i1.setId(Long.parseLong("1"));

        i2 = new Ingredient();
        i2.setBrand("Notenshop");
        i2.setUniqueCode("BBB");
        i2.setAvailable(true);
        i2.setExpiry(LocalDate.now().minusDays(1));
        i2.setId(Long.parseLong("2"));

        i3 = new Ingredient();
        i3.setBrand("Amazon");
        i3.setUniqueCode("CCC");
        i3.setAvailable(true);
        i3.setExpiry(LocalDate.now().plusDays(1));
        i3.setId(Long.parseLong("3"));

        i4 = new Ingredient();
        i4.setBrand("Colruyt");
        i4.setUniqueCode("DDD");
        i4.setAvailable(true);
        i4.setExpiry(LocalDate.now().plusDays(100));
        i4.setId(Long.parseLong("4"));

        t1 = new Ingredienttracing();
        t1.setId(Long.parseLong("1"));
        t1.setIngredientId(Long.parseLong("1"));
        t1.setOrderlineId(Long.parseLong("1"));

        t2 = new Ingredienttracing();
        t2.setId(Long.parseLong("2"));
        t2.setIngredientId(Long.parseLong("2"));
        t2.setOrderlineId(Long.parseLong("1"));

    }


    @Test
    public void getAllTest(){
        List<OrderEntity> list = new ArrayList<>();
        list.add(o1);
        list.add(o2);

        Page<OrderEntity> pagedList = new PageImpl<>(list);
        Pageable paging = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "id"));

        when(orderMapper.toDTO(o1)).thenReturn(o1DTO);
        when(orderMapper.toDTO(o2)).thenReturn(o2DTO);

        //Mockito.when(keycloakRestTemplate.getForObject(Mockito.eq(urlCustomers+o1.getId()), Mockito.eq(Customer.class))).thenReturn(c1);
        Mockito.when(keycloakRestTemplate.getForObject(Mockito.eq(urlCustomers+o2.getId()), Mockito.eq(Customer.class))).thenReturn(c1);

        given(orderRepository.findAll(paging)).willReturn(pagedList);

        Page<Order> orderList = orderService.getAll(0, 10, "id");

        assertEquals(2, orderList.toList().size());

        Order testOrder = orderList.toList().get(0);

        //Should return Tom for first name of customer of the first order in the list
        assertEquals("Tom", testOrder.getCustomer().getFirstName());
    }

    @Test
    public void getByIdTest(){
        Long id = Long.parseLong("1");
        given(orderRepository.findById(id)).willReturn(java.util.Optional.ofNullable(o1));

        given(orderMapper.toDTO(o1)).willReturn(o1DTO);

        Mockito.when(keycloakRestTemplate.getForObject(Mockito.eq(urlCustomers+o1.getCustomerId()), Mockito.eq(Customer.class))).thenReturn(c1);

        List<Orderline> listOfOrderlines = new ArrayList<>();
        ol1.setProduct(product);
        listOfOrderlines.add(ol1);
        Orderline[] orderlinessArray = listOfOrderlines.toArray(new Orderline[0]);
        Mockito.when(keycloakRestTemplate.getForObject(Mockito.eq(urlOrderlines+o1.getId()), Mockito.eq(Orderline[].class))).thenReturn(orderlinessArray);

        List<Ingredienttracing> listOfTracings = new ArrayList<>();
        Ingredienttracing[] tracingsArray = listOfTracings.toArray(new Ingredienttracing[0]);
        Mockito.when(keycloakRestTemplate.getForObject(Mockito.eq(urlIngredienttracings+o1.getId()), Mockito.eq(Ingredienttracing[].class))).thenReturn(tracingsArray);

        Long ingredientId = t1.getIngredientId();
        Mockito.when(keycloakRestTemplate.getForObject(Mockito.eq(urlIngredients+ingredientId), Mockito.eq(Ingredient.class))).thenReturn(i1);

        Order orderTest = orderService.getById(id);

        assertEquals(1, orderTest.getId().intValue()); //order id should be 1
        assertEquals(1, orderTest.getCustomerId().intValue()); //customer id should be 1
        assertEquals("Tom", orderTest.getCustomer().getFirstName()); //customer first name should be Tom
        assertEquals(0, orderTest.getStatus().intValue()); //order status should be 0 as there are no linked ingredients

        List<Ingredienttracing> listOfTracings2 = new ArrayList<>();
        listOfTracings2.add(t1);
        Ingredienttracing[] tracingsArray2 = listOfTracings2.toArray(new Ingredienttracing[0]);
        Mockito.when(keycloakRestTemplate.getForObject(Mockito.eq(urlIngredienttracings+ol1.getId()), Mockito.eq(Ingredienttracing[].class))).thenReturn(tracingsArray2);

        Order orderTest2 = orderService.getById(id);
        assertEquals(2, orderTest2.getStatus().intValue()); //order status should be 2 as all orderlines have linked ingredients


        List<Orderline> listOfOrderlines2 = new ArrayList<>();
        ol1.setProduct(product);
        ol2.setProduct(product);
        listOfOrderlines2.add(ol1);
        listOfOrderlines2.add(ol2);
        Orderline[] orderlinessArray2 = listOfOrderlines2.toArray(new Orderline[0]);
        Mockito.when(keycloakRestTemplate.getForObject(Mockito.eq(urlOrderlines+ol1.getId()), Mockito.eq(Orderline[].class))).thenReturn(orderlinessArray2);

        List<Ingredienttracing> listOfTracings3 = new ArrayList<>();
        List<Ingredienttracing> listOfTracings4 = new ArrayList<>();
        listOfTracings3.add(t1);
        Ingredienttracing[] tracingsArray3 = listOfTracings3.toArray(new Ingredienttracing[0]);
        Ingredienttracing[] tracingsArray4 = listOfTracings4.toArray(new Ingredienttracing[0]);
        Mockito.when(keycloakRestTemplate.getForObject(Mockito.eq(urlIngredienttracings+ol1.getId()), Mockito.eq(Ingredienttracing[].class))).thenReturn(tracingsArray3);
        Mockito.when(keycloakRestTemplate.getForObject(Mockito.eq(urlIngredienttracings+ol2.getId()), Mockito.eq(Ingredienttracing[].class))).thenReturn(tracingsArray4);


        Order orderTest3 = orderService.getById(id);
        assertEquals(1, orderTest3.getStatus().intValue()); //order status should be 1 now as only one of two orderlines have linked ingredients

    }

    @Test
    public void getOrderLineWithLinkedIngredientsByIdTest(){

        Long id = Long.parseLong("1");
        Mockito.when(keycloakRestTemplate.getForObject(Mockito.eq(urlOrderlines+id), Mockito.eq(Orderline.class))).thenReturn(ol1);

        Long ingredientId = t1.getIngredientId();
        Mockito.when(keycloakRestTemplate.getForObject(Mockito.eq(urlIngredients+ingredientId), Mockito.eq(Ingredient.class))).thenReturn(i1);

        List<Ingredienttracing> listOfTracings = new ArrayList<>();
        listOfTracings.add(t1);
        Ingredienttracing[] tracingsArray = listOfTracings.toArray(new Ingredienttracing[0]);

        Mockito.when(keycloakRestTemplate.getForObject(Mockito.eq(urlIngredienttracings+o1.getId()), Mockito.eq(Ingredienttracing[].class))).thenReturn(tracingsArray);

        Orderline orderlineTest = orderService.getOrderlineWithLinkedIngredientsById(id);

        assertEquals(1, orderlineTest.getIngredients().size()); //size of the ingredient list should be 1
        assertEquals("Bakshop", orderlineTest.getIngredients().get(0).getBrand()); //the first ingredient in the list should have brandname Bakshop
    }


    @Test
    public void getAvailableIngredientsForOrderlineTest(){

        Long id = Long.parseLong("1");

        List<Ingredienttracing> listOfTracings = new ArrayList<>();
        listOfTracings.add(t1);
        listOfTracings.add(t2);
        Ingredienttracing[] tracingsArray = listOfTracings.toArray(new Ingredienttracing[0]);

        Mockito.when(keycloakRestTemplate.getForObject(Mockito.eq(urlIngredienttracings+id), Mockito.eq(Ingredienttracing[].class))).thenReturn(tracingsArray);

        List<Ingredient> listOfIngredients = new ArrayList<>();
        listOfIngredients.add(i1);
        listOfIngredients.add(i2);
        listOfIngredients.add(i3);
        listOfIngredients.add(i4);
        Ingredient[] ingredientsArray = listOfIngredients.toArray(new Ingredient[0]);

        Mockito.when(keycloakRestTemplate.getForObject(Mockito.eq(urlIngredients), Mockito.eq(Ingredient[].class))).thenReturn(ingredientsArray);

        List<Ingredient> listOfAvailableIngredients = orderService.getAvailableIngredientsForOrderline(id);

        assertEquals(2, listOfAvailableIngredients.size()); //the list size should be two
        assertTrue(listOfAvailableIngredients.contains(i3)); //the list should contain entity i3
        assertTrue(listOfAvailableIngredients.contains(i4)); //the list should contain entity i4
    }

    @Test
    public void getAllIngredienttracedOrdersTest(){
        List<OrderEntity> orderList = new ArrayList<>();
        orderList.add(o1);

        when(orderRepository.findAll()).thenReturn(orderList);
        when(orderMapper.toDTO(o1)).thenReturn(o1DTO);

        List<Orderline> listOfOrderlines1 = new ArrayList<>();
        ol1.setProduct(product);
        listOfOrderlines1.add(ol1);
        ol2.setProduct(product);
        listOfOrderlines1.add(ol2);
        Orderline[] orderlinessArray1 = listOfOrderlines1.toArray(new Orderline[0]);
        Mockito.when(keycloakRestTemplate.getForObject(Mockito.eq(urlOrderlines+o1.getId()), Mockito.eq(Orderline[].class))).thenReturn(orderlinessArray1);

        List<Ingredienttracing> listOfTracings = new ArrayList<>();
        listOfTracings.add(t1);
        Ingredienttracing[] tracingsArray = listOfTracings.toArray(new Ingredienttracing[0]);
        Mockito.when(keycloakRestTemplate.getForObject(Mockito.eq(urlIngredienttracings+ol1.getId()), Mockito.eq(Ingredienttracing[].class))).thenReturn(tracingsArray);

        List<Ingredienttracing> listofTracings2 = new ArrayList<>();
        Ingredienttracing[] tracingsArray2 = listofTracings2.toArray(new Ingredienttracing[0]);
        Mockito.when(keycloakRestTemplate.getForObject(Mockito.eq(urlIngredienttracings+ol2.getId()), Mockito.eq(Ingredienttracing[].class))).thenReturn(tracingsArray2);

        Long ingredientId = t1.getIngredientId();
        Mockito.when(keycloakRestTemplate.getForObject(Mockito.eq(urlIngredients+ingredientId), Mockito.eq(Ingredient.class))).thenReturn(i1);

        Mockito.when(keycloakRestTemplate.getForObject(Mockito.eq(urlCustomers+o1.getId()), Mockito.eq(Customer.class))).thenReturn(c1);

        List<Order> tracedOrderList = orderService.getAllIngredienttracedOrders("AAA");

        assertEquals(1, tracedOrderList.size()); //One order should be found
        assertEquals(1, tracedOrderList.get(0).getOrderlines().size()); //The found order should have one and not two orderlines as only one orderline contains the traced ingredient
    }

}
