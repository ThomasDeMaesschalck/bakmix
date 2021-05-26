package be.bakmix.eindproject.orderline.service;

import be.bakmix.eindproject.orderline.business.OrderlineEntity;
import be.bakmix.eindproject.orderline.business.repository.OrderlineRepository;
import be.bakmix.eindproject.orderline.service.dto.Orderline;
import be.bakmix.eindproject.orderline.service.dto.Product;
import be.bakmix.eindproject.orderline.service.mapper.OrderlineMapper;
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Service layer for the orderline microservice
 */
@Service
 public class OrderlineService {

     /**
     * Orderline repository
     */
    @Autowired
    private final OrderlineRepository orderlineRepository;

    /**
     * Orderline mapper class
     */
    @Autowired
    private final OrderlineMapper orderlineMapper;

    /**
     * Keycloak rest template. Used for authenticated communication with other REST APIs.
     */
    @Autowired
    private KeycloakRestTemplate keycloakRestTemplate;

    @Value("http://localhost:7778/api/products/")
    private String urlProducts;

    /**
     * Constructor.
     * @param orderlineRepository Orderline repo
     * @param orderlineMapper Orderline mapper
     */
    public OrderlineService(OrderlineRepository orderlineRepository, OrderlineMapper orderlineMapper, KeycloakRestTemplate keycloakRestTemplate)
    {
        this.orderlineRepository = orderlineRepository;
        this.orderlineMapper = orderlineMapper;
        this.keycloakRestTemplate = keycloakRestTemplate;
    }

    /**
     * Get a list of all Orderlines.
     * For each orderline, the Product is set.
     * @return List of Orderlines
     */
    public List<Orderline> getAll(){
        List<Orderline> orderlines = StreamSupport
                .stream(orderlineRepository.findAll().spliterator(), false)
                .map(orderlineMapper::toDTO)
                .collect(Collectors.toList());

        orderlines.forEach(orderline -> {
             Product product = keycloakRestTemplate.getForObject(urlProducts+orderline.getProductId(), Product.class);
            orderline.setProductId(product.getId());
            orderline.setProduct(product);
        });
        return orderlines;
    }

    /**
     * Get all Orderlines that belong to a specific Order.
     * Product is retrieved and is set for each Orderline.
     * @param id The id of the Order.
     * @return All orderlines that belong to the specified Order.
     */
    public List<Orderline> getByOrderId(Long id){
        List<Orderline> orderlines = StreamSupport
                .stream(orderlineRepository.findAll().spliterator(), false)
                .filter(o -> o.getOrderId() == id)
                .map(orderlineMapper::toDTO)
                .collect(Collectors.toList());

        orderlines.forEach(orderline -> {
             Product product = keycloakRestTemplate.getForObject(urlProducts+orderline.getProductId(), Product.class);
            orderline.setProductId(product.getId());
            orderline.setProduct(product);
        });
        return orderlines;
    }

    /**
     * Get an orderline by its id.
     * The Product is retrieved and set for this orderline.
     * @param id The id of the orderline that needs to be retrieved.
     * @return The requested Orderline.
     */
    public Orderline getById(Long id){
        Optional<OrderlineEntity> orderlineEntityOptional = orderlineRepository.findById(id);
        if(orderlineEntityOptional.isPresent()){
            Orderline orderline = orderlineMapper.toDTO(orderlineEntityOptional.get());
             Product product = keycloakRestTemplate.getForObject(urlProducts+orderline.getProductId(), Product.class);
            orderline.setProductId(product.getId());
            orderline.setProduct(product);
            return orderline;
        }
        return null;
    }

}
