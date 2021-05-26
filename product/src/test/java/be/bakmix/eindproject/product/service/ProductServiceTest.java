package be.bakmix.eindproject.product.service;

import be.bakmix.eindproject.product.business.ProductEntity;
import be.bakmix.eindproject.product.business.repository.ProductRepository;
import be.bakmix.eindproject.product.service.dto.Product;
import be.bakmix.eindproject.product.service.mapper.ProductMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductServiceTest {

    @Mock
    ProductRepository productRepository;

    @Autowired
    ProductMapper productMapper;

    @InjectMocks
    ProductService productService;


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getAll(){
        productService = new ProductService(productRepository, productMapper);
        List<ProductEntity> list = new ArrayList<>();
        ProductEntity p1 = new ProductEntity();
        p1.setId(Long.parseLong("1"));
        p1.setName("Brownies");
        ProductEntity p2 = new ProductEntity();
        p2.setId(Long.parseLong("2"));
        p2.setName("Cake");
        list.add(p1);
        list.add(p2);

        given(productRepository.findAll()).willReturn(list);

        List<Product> productList = productService.getAll();

        assertEquals(2, productList.size()); //product list size should be 2
    }

    @Test
    public void getById(){
        productService = new ProductService(productRepository, productMapper);
        ProductEntity p1 = new ProductEntity();
        p1.setId(Long.parseLong("5"));
        p1.setName("Brownies");

        given(productRepository.findById(Long.parseLong("5"))).willReturn(java.util.Optional.of(p1));

        Product product = productService.getById(Long.parseLong("5"));

        assertEquals("Brownies", product.getName()); //product name should be Brownies
    }

}
