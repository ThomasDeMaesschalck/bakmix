package be.bakmix.eindproject.order.service;

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
public class OrderServiceTest {


    @Test
    public void getAllTest(){
    }

    @Test
    public void getByIdTest(){
    }


    @Test
    public void orderStatusCheckTest(){
    }

    @Test
    public void getOrderLineWithLinkedIngredientsByIdTest(){
    }


    @Test
    public void getAvailableIngredientsForOrderlineTest(){
    }

    @Test
    public void getAllIngredienttracedOrdersTest(){
    }
}
