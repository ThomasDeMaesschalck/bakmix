package be.bakmix.eindproject.pdf.service;

import be.bakmix.eindproject.pdf.service.dto.*;
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Service layer of the PDF microservice
 */
@Service
public class PdfService {

    /**
     * Keycloak rest template. Used for authenticated communication with other secured REST APIs.
     */
    @Autowired
    private KeycloakRestTemplate keycloakRestTemplate;

    @Value("http://localhost:7772/api/orders/")
    private String urlOrders;

    @Value("http://localhost:7778/api/products/")
    private String urlProducts;

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

    /**
     * Retrieve an Order and set its Orderlines.
     * @param id The id of the Order.
     * @return The processed Order.
     */

    public Order getById(Long id) {

        Order order = keycloakRestTemplate.getForObject(urlOrders + id, Order.class);
        Customer customer = keycloakRestTemplate.getForObject(urlCustomers + order.getCustomerId(), Customer.class);
        order.setCustomer(customer);
        Orderline[] orderlines = keycloakRestTemplate.getForObject(urlOrderlines + order.getId(), Orderline[].class);
        order.setOrderlines(Arrays.asList(orderlines));
        return order;
    }
}

