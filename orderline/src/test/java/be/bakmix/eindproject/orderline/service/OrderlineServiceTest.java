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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;


import java.math.BigDecimal;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;
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

    List<OrderlineEntity> list;
    Product product;
    OrderlineEntity o1;
    Orderline o1DTO;

    @Value("http://localhost:7778/api/products/")
    private String urlProducts;


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        orderlineService = new OrderlineService(orderlineRepository, orderlineMapper, keycloakRestTemplate);
    }

    @Test
    public void getAll() {
        list = new ArrayList<>();
        o1 = new OrderlineEntity();
        o1.setId(Long.parseLong("1"));
        o1.setOrderId(Long.parseLong("1"));
        o1.setQty(Long.parseLong("2"));
        o1.setPurchasePrice(BigDecimal.valueOf(5.50));
        o1.setProductId(Long.parseLong("1"));
        list.add(o1);

        product = new Product();
        product.setId(Long.parseLong("1"));
        product.setPrice(BigDecimal.valueOf(10));
        product.setName("Bloem");
        product.setVat(6);

        o1DTO = new Orderline();
        o1DTO.setId(Long.parseLong("1"));
        o1DTO.setOrderId(Long.parseLong("1"));
        o1DTO.setQty(Long.parseLong("2"));
        o1DTO.setPurchasePrice(BigDecimal.valueOf(5.50));
        o1DTO.setProductId(Long.parseLong("1"));


        Mockito.when(keycloakRestTemplate.getForObject(Mockito.eq(urlProducts+o1.getProductId()), Mockito.eq(Product.class))).thenReturn(product);

        when(orderlineMapper.toDTO(o1)).thenReturn(o1DTO);

        given(orderlineRepository.findAll()).willReturn(list);


        List<Orderline> orderlineList = orderlineService.getAll();

        assertEquals(1, orderlineList.size());
    }

    /* @Test
    public void getById(){
        productService = new ProductService(productRepository, productMapper);
        ProductEntity p1 = new ProductEntity();
        p1.setId(Long.parseLong("5"));
        p1.setName("Brownies");

        given(productRepository.findById(Long.parseLong("5"))).willReturn(java.util.Optional.of(p1));

        Product product = productService.getById(Long.parseLong("5"));

        assertEquals("Brownies", product.getName());

    }
     */

}
