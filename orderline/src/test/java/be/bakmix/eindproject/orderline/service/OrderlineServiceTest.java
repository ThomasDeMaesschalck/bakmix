package be.bakmix.eindproject.orderline.service;

import be.bakmix.eindproject.orderline.business.OrderlineEntity;
import be.bakmix.eindproject.orderline.business.repository.OrderlineRepository;
import be.bakmix.eindproject.orderline.service.dto.Orderline;
import be.bakmix.eindproject.orderline.service.dto.Product;
import be.bakmix.eindproject.orderline.service.mapper.OrderlineMapper;
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


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@SpringBootTest()
public class OrderlineServiceTest {

    @InjectMocks
    OrderlineService orderlineService;

    @Mock
    OrderlineRepository orderlineRepository;

    @Mock
    OrderlineMapper orderlineMapper;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    KeycloakRestTemplate keycloakRestTemplate;

    Product product;
    OrderlineEntity o1;
    OrderlineEntity o2;
    OrderlineEntity o3;
    Orderline o1DTO;
    Orderline o2DTO;
    Orderline o3DTO;

    @Value("http://localhost:7778/api/products/")
    private String urlProducts;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        orderlineService = new OrderlineService(orderlineRepository, orderlineMapper, keycloakRestTemplate);

        o1 = new OrderlineEntity();
        o1.setId(Long.parseLong("1"));
        o1.setOrderId(Long.parseLong("5"));
        o1.setQty(Long.parseLong("2"));
        o1.setPurchasePrice(BigDecimal.valueOf(5.50));
        o1.setProductId(Long.parseLong("1"));

        o2 = new OrderlineEntity();
        o2.setId(Long.parseLong("2"));
        o2.setOrderId(Long.parseLong("5"));
        o2.setQty(Long.parseLong("2"));
        o2.setPurchasePrice(BigDecimal.valueOf(5.50));
        o2.setProductId(Long.parseLong("1"));

        o3 = new OrderlineEntity();
        o3.setId(Long.parseLong("3"));
        o3.setOrderId(Long.parseLong("10"));
        o3.setQty(Long.parseLong("2"));
        o3.setPurchasePrice(BigDecimal.valueOf(5.50));
        o3.setProductId(Long.parseLong("1"));

        product = new Product();
        product.setId(Long.parseLong("1"));
        product.setPrice(BigDecimal.valueOf(10));
        product.setName("Bloem");
        product.setVat(6);

        o1DTO = new Orderline();
        o1DTO.setId(Long.parseLong("1"));
        o1DTO.setOrderId(Long.parseLong("5"));
        o1DTO.setQty(Long.parseLong("2"));
        o1DTO.setPurchasePrice(BigDecimal.valueOf(5.50));
        o1DTO.setProductId(Long.parseLong("1"));

        o2DTO = new Orderline();
        o2DTO.setId(Long.parseLong("2"));
        o2DTO.setOrderId(Long.parseLong("5"));
        o2DTO.setQty(Long.parseLong("2"));
        o2DTO.setPurchasePrice(BigDecimal.valueOf(5.50));
        o2DTO.setProductId(Long.parseLong("1"));

        o3DTO = new Orderline();
        o3DTO.setId(Long.parseLong("3"));
        o3DTO.setOrderId(Long.parseLong("10"));
        o3DTO.setQty(Long.parseLong("2"));
        o3DTO.setPurchasePrice(BigDecimal.valueOf(5.50));
        o3DTO.setProductId(Long.parseLong("1"));
    }

    @Test
    public void getAll() {
        List<OrderlineEntity> list = new ArrayList<>();
        list.add(o1);

        Mockito.when(keycloakRestTemplate.getForObject(Mockito.eq(urlProducts+o1.getProductId()), Mockito.eq(Product.class))).thenReturn(product);

        when(orderlineMapper.toDTO(o1)).thenReturn(o1DTO);

        given(orderlineRepository.findAll()).willReturn(list);

        List<Orderline> orderlineList = orderlineService.getAll();

        assertEquals(1, orderlineList.size()); //list size should be 1
    }

     @Test
    public void getById(){

        Mockito.when(keycloakRestTemplate.getForObject(Mockito.eq(urlProducts+o1.getProductId()), Mockito.eq(Product.class))).thenReturn(product);

        when(orderlineMapper.toDTO(o1)).thenReturn(o1DTO);

        given(orderlineRepository.findById(Long.parseLong("1"))).willReturn(java.util.Optional.ofNullable(o1));

        Orderline orderlineFromTest = orderlineService.getById(Long.parseLong("1"));

        assertEquals("Bloem", orderlineFromTest.getProduct().getName()); //product name of the orderline should be Bloem
    }

    @Test
    public void getByOrderId(){
        List<OrderlineEntity> list = new ArrayList<>();

        list.add(o1);
        list.add(o2);
        list.add(o3);

        Mockito.when(keycloakRestTemplate.getForObject(Mockito.eq(urlProducts+o1.getProductId()), Mockito.eq(Product.class))).thenReturn(product);

        when(orderlineMapper.toDTO(o1)).thenReturn(o1DTO);
        when(orderlineMapper.toDTO(o2)).thenReturn(o2DTO);
        //when(orderlineMapper.toDTO(o3)).thenReturn(o3DTO);

        given(orderlineRepository.findAll()).willReturn(list);

        List<Orderline> orderlinesFromTest = orderlineService.getByOrderId(Long.parseLong("5"));

        assertEquals(2, orderlinesFromTest.size()); //method should return two of the three orderlines as the other one belongs to a different order
    }

}
