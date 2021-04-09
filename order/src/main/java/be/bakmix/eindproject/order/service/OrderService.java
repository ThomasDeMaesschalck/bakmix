package be.bakmix.eindproject.order.service;

import be.bakmix.eindproject.order.business.OrderEntity;
import be.bakmix.eindproject.order.business.repository.OrderRepository;
import be.bakmix.eindproject.order.service.dto.*;
import be.bakmix.eindproject.order.service.mapper.OrderMapper;
import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class OrderService {

    private static List<Order> orders = new ArrayList<>();

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private KeycloakRestTemplate keycloakRestTemplate;

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

    public OrderService(OrderRepository orderRepository, OrderMapper orderMapper)
    {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
    }

    public Page<Order> getAll(boolean index, Integer pageNo, Integer pageSize, String sortBy){
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.DESC, sortBy));

        Page<Order> orders = orderRepository.findAll(paging).map(orderMapper::toDTO);

        orders.forEach(order -> {
                    Customer customer = keycloakRestTemplate.getForObject(urlCustomers + order.getCustomerId(), Customer.class);
                    order.setCustomerId(customer.getId());
                    order.setCustomer(customer);
                }
        );

        if(!index) { //performance enhancement voor de index pagina
            orders.forEach(order ->
            {
                Orderline[] orderlines = keycloakRestTemplate.getForObject(urlOrderlines + order.getId(), Orderline[].class);

                Arrays.stream(orderlines).forEach(orderline -> {
                    Ingredienttracing[] ingredienttracings = keycloakRestTemplate.getForObject(urlIngredienttracings + orderline.getId(), Ingredienttracing[].class);
                    List<Ingredient> ingredients = new ArrayList();

                    Arrays.stream(ingredienttracings).forEach(ingredienttracing -> {
                        Long ingredientId = ingredienttracing.getIngredientId();
                         Ingredient ingredient = keycloakRestTemplate.getForObject(urlIngredients + ingredientId, Ingredient.class);
                        ingredients.add(ingredient);
                        orderline.setIngredients(ingredients);
                    });

                });

                order.setOrderlines(Arrays.asList(orderlines));
            });
        }
        return orders;
    }

    public Order getById(Long id){
        Optional<OrderEntity> optionalOrderEntity = orderRepository.findById(id);
        if(optionalOrderEntity.isPresent()){
            Order order = orderMapper.toDTO(optionalOrderEntity.get());

             Customer customer = keycloakRestTemplate.getForObject(urlCustomers+order.getCustomerId(), Customer.class);
            order.setCustomer(customer);

             Orderline[] orderlines = keycloakRestTemplate.getForObject(urlOrderlines+order.getId(), Orderline[].class);

            for(Orderline orderline: orderlines)
            {
                 Ingredienttracing[] ingredienttracings = keycloakRestTemplate.getForObject(urlIngredienttracings+orderline.getId(), Ingredienttracing[].class);
                List<Ingredient> ingredients = new ArrayList();

                for(Ingredienttracing ingredienttracing: ingredienttracings)
                {
                    Long ingredientId = ingredienttracing.getIngredientId();
                     Ingredient ingredient = keycloakRestTemplate.getForObject(urlIngredients+ingredientId, Ingredient.class);
                    ingredients.add(ingredient);
                    orderline.setIngredients(ingredients);
                }
            }

            order.setOrderlines(Arrays.asList(orderlines));
            order.setSubTotal(subTotal(order));
            order.setVatTotal(vatTotal(order));
            order.setTotal(total(order));
            return order;
        }
        return null;
    }

    public void save(Order order) {
        OrderEntity orderToSave = orderMapper.toEntity(order);
        orderRepository.save(orderToSave);
    }

    public Orderline getOrderlineWithLinkedIngredientsById(Long id){

             Orderline orderline = keycloakRestTemplate.getForObject(urlOrderlinesById+id, Orderline.class);

                 Ingredienttracing[] ingredienttracings = keycloakRestTemplate.getForObject(urlIngredienttracings+orderline.getId(), Ingredienttracing[].class);
                List<Ingredient> ingredients = new ArrayList();

                Arrays.stream(ingredienttracings).forEach(ingredienttracing -> {
                    Long ingredientId = ingredienttracing.getIngredientId();
                     Ingredient ingredient = keycloakRestTemplate.getForObject(urlIngredients+ingredientId, Ingredient.class);
                    ingredient.setIngredienttracingId(ingredienttracing.getId());
                    ingredients.add(ingredient);
                    orderline.setIngredients(ingredients);
                });

            return orderline;
    }

    public List<Ingredient> getAvailableIngredientsForOrderline(Long id) {

         Ingredient[] ingredients = keycloakRestTemplate.getForObject(urlIngredients, Ingredient[].class);

       List<Ingredient> ingredientsList = Arrays.asList(ingredients);

         Ingredienttracing[] ingredienttracings = keycloakRestTemplate.getForObject(urlIngredienttracings + id, Ingredienttracing[].class);

        ingredientsList = ingredientsList.stream().filter(i -> i.getAvailable() == true).collect(Collectors.toList());

        List<Ingredient> finalIngredientsList = ingredientsList;
        Arrays.stream(ingredienttracings).forEach(ingredienttracing -> {
            finalIngredientsList.removeIf(ingredient -> ingredient.getId().equals(ingredienttracing.getIngredientId()));
        });

        return ingredientsList;
    }


    public List<Order> getAllIngredienttracedOrders(String id){
        List<Order> orders = StreamSupport
                .stream(orderRepository.findAll().spliterator(), false)
                .map(e -> orderMapper.toDTO(e))
                .collect(Collectors.toList());

        orders.forEach(order -> {

             Orderline[] orderlines = keycloakRestTemplate.getForObject(urlOrderlines+order.getId(), Orderline[].class);
            List<Orderline> filteredOrderlines = new ArrayList();
            boolean found = false;

            Arrays.stream(orderlines).forEach(orderline -> {

            });
            for(Orderline orderline: orderlines)
            {
                 Ingredienttracing[] ingredienttracings = keycloakRestTemplate.getForObject(urlIngredienttracings+orderline.getId(), Ingredienttracing[].class);
                List<Ingredient> ingredients = new ArrayList();

                for(Ingredienttracing ingredienttracing: ingredienttracings)
                {
                    Long ingredientId = ingredienttracing.getIngredientId();
                     Ingredient ingredient = keycloakRestTemplate.getForObject(urlIngredients+ingredientId, Ingredient.class);
                    ingredients.add(ingredient);
                    orderline.setIngredients(ingredients);
                    if (ingredient.getUniqueCode().equals(id))
                    {
                        found = true;
                    }
                }
                if (found)
                {
                    filteredOrderlines.add(orderline);
                    found = false;
                }
            }
            order.setOrderlines(filteredOrderlines);
        });

        List<Order> ordersFiltered =   orders.stream().filter(o -> !o.getOrderlines().isEmpty()).collect(Collectors.toList());

        ordersFiltered.forEach(order -> {
             Customer customer = keycloakRestTemplate.getForObject(urlCustomers+order.getCustomerId(), Customer.class);
            order.setCustomerId(customer.getId());
            order.setCustomer(customer);
        });

        return ordersFiltered;
    }

    public BigDecimal subTotal(Order order)
    {
        Function<Orderline, BigDecimal> totalMapper = o -> o.getPurchasePrice().multiply(BigDecimal.valueOf(o.getQty()));
        BigDecimal result = order.getOrderlines().stream().map(totalMapper).reduce(BigDecimal.ZERO, BigDecimal::add);
        result = result.setScale(2, RoundingMode.HALF_UP);
        return result;
    }

    public BigDecimal vatTotal(Order order)
    {
        double highestVat = (order.getOrderlines().stream().mapToDouble(o -> o.getProduct().getVat()).max().orElseThrow() / 100);
        BigDecimal result = ((order.getSubTotal().subtract(order.getDiscount())).add(order.getShippingCost())).multiply(BigDecimal.valueOf(highestVat));
        result = result.setScale(2, RoundingMode.HALF_UP);
       return result;
    }

    public BigDecimal total(Order order)
    {
        BigDecimal result = order.getSubTotal().subtract(order.getDiscount()).add(order.getVatTotal()).add(order.getShippingCost());
        result = result.setScale(2, RoundingMode.HALF_UP);
        return result;
    }

}
