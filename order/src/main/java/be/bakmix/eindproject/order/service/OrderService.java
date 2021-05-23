package be.bakmix.eindproject.order.service;

import be.bakmix.eindproject.order.business.OrderEntity;
import be.bakmix.eindproject.order.business.repository.OrderRepository;
import be.bakmix.eindproject.order.service.dto.*;
import be.bakmix.eindproject.order.service.mapper.OrderMapper;
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Service layer for the Order microservice
 */
@Service
public class OrderService {

    /**
     * List of orders
     */
    private static List<Order> orders = new ArrayList<>();

    /**
     * The Order repository
     */
    @Autowired
    private OrderRepository orderRepository;

    /**
     * The Order mapper class
     */
    @Autowired
    private OrderMapper orderMapper;

    /**
     * Keycloak resttemplate. For authenticated communication via REST API with other microservices.
     */
    @Autowired
    private KeycloakRestTemplate keycloakRestTemplate;

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
     * Constructor
     * @param orderRepository The order repository
     * @param orderMapper The order mapper
     */
    public OrderService(OrderRepository orderRepository, OrderMapper orderMapper)
    {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
    }

    /**
     * Get a Page of Orders
     * For each Order, the method sets the Customer and Orderlines.
     * For each orderline, the linked ingredients are set.
     * Order status check is performed and updated if necessary
     * @param index True if request is made for displaying content on the index and order page of the application. Some logic gets bypassed: avoids loading unnecessary information to speed up processing.
     * @param pageNo The page number
     * @param pageSize The page size
     * @param sortBy Sorting filter
     * @return The requested Page of orders
     */
    public Page<Order> getAll(boolean index, Integer pageNo, Integer pageSize, String sortBy){
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.DESC, sortBy));

        Page<Order> orders = orderRepository.findAll(paging).map(orderMapper::toDTO);

        orders.forEach(order -> {
                    Customer customer = keycloakRestTemplate.getForObject(urlCustomers + order.getCustomerId(), Customer.class);
                    order.setCustomerId(customer.getId());
                    order.setCustomer(customer);
                }
        );

        if(!index) { //performance enhancement for index pages
            orders.forEach(order ->
            {
                Orderline[] orderlines = keycloakRestTemplate.getForObject(urlOrderlines + order.getId(), Orderline[].class);

                Arrays.stream(orderlines).forEach(orderline -> {
                    Ingredienttracing[] ingredienttracings = keycloakRestTemplate.getForObject(urlIngredienttracings + orderline.getId(), Ingredienttracing[].class);
                    List<Ingredient> ingredients = new ArrayList<>();

                    Arrays.stream(ingredienttracings).forEach(ingredienttracing -> {
                        Long ingredientId = ingredienttracing.getIngredientId();
                         Ingredient ingredient = keycloakRestTemplate.getForObject(urlIngredients + ingredientId, Ingredient.class);
                        ingredients.add(ingredient);
                        orderline.setIngredients(ingredients);
                    });

                });

                order.setOrderlines(Arrays.asList(orderlines));
                orderStatusCheck(order);
            });
        }
        return orders;
    }

    /**
     * Retrieve an Order from the database
     * Customer and Orderline are retrieved via REST APIs.
     * For each orderline, the linked ingredients are retrieved via REST.
     * An order status check is performed and updated if necessary.
     * The Order subtotal, vat total, and invoice total are calculated.
     * @param id The id of the Order
     * @return The requested Order
     */
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
                List<Ingredient> ingredients = new ArrayList<>();

                for(Ingredienttracing ingredienttracing: ingredienttracings)
                {
                    Long ingredientId = ingredienttracing.getIngredientId();
                     Ingredient ingredient = keycloakRestTemplate.getForObject(urlIngredients+ingredientId, Ingredient.class);
                    ingredients.add(ingredient);
                    orderline.setIngredients(ingredients);
                }
            }
            order.setOrderlines(Arrays.asList(orderlines));

            orderStatusCheck(order);

            order.setSubTotal(subTotal(order));
            order.setVatTotal(vatTotal(order));
            order.setTotal(total(order));
            return order;
        }
        return null;
    }

    /**
     * Persist an order to the database.
     * @param order The order that needs to be persisted
     */
    public void save(Order order) {
        OrderEntity orderToSave = orderMapper.toEntity(order);
        orderRepository.save(orderToSave);
    }

    /**
     * Automatically change order status.
     * This method ensures the order status is always correct, even if linked ingredients are removed.
     * An iteration is performed to check whether an Order's orderlines have linked ingredients.
     * If at least one orderline has linked ingredients and the status is 0, the status is set to 1.
     * If all orderlines have linked ingredients, the status is set to 2.
     * @param order The Order that needs a status check
     */
    public void orderStatusCheck(Order order)  //order wijzigen indien er wel of geen ingrediënten gekoppeld zijn aan orderlijnen
    {
        if (order.getStatus() != 3) {
            boolean found = false;
            boolean foundInAll = false;
            int i = 0;
            for (Orderline orderline : order.getOrderlines()) {
                if ((orderline.getIngredients() != null)) {
                    found = true;
                    i++;
                }
                if (order.getOrderlines().size() == i)
                {
                    foundInAll = true;
                }
            }
            if (!found && (order.getStatus() != 0)) {
                order.setStatus(Long.parseLong("0"));
                save(order);
            }
            if (found && !foundInAll) {
                order.setStatus(Long.parseLong("1"));
                save(order);
            }
            if (found && (order.getStatus() < 2) && foundInAll) {
                order.setStatus(Long.parseLong("2"));
                save(order);
            }
        }
    }

    /**
     * Retrieve an Orderline via REST API and set all linked Ingredients
     * @param id The Orderline that needs to be processed
     * @return The processed Orderline
     */
    public Orderline getOrderlineWithLinkedIngredientsById(Long id){

             Orderline orderline = keycloakRestTemplate.getForObject(urlOrderlinesById+id, Orderline.class);

                 Ingredienttracing[] ingredienttracings = keycloakRestTemplate.getForObject(urlIngredienttracings+orderline.getId(), Ingredienttracing[].class);
                List<Ingredient> ingredients = new ArrayList<>();

                Arrays.stream(ingredienttracings).forEach(ingredienttracing -> {
                    Long ingredientId = ingredienttracing.getIngredientId();
                     Ingredient ingredient = keycloakRestTemplate.getForObject(urlIngredients+ingredientId, Ingredient.class);
                    ingredient.setIngredienttracingId(ingredienttracing.getId());
                    ingredients.add(ingredient);
                    orderline.setIngredients(ingredients);
                });

            return orderline;
    }

    /**
     * Generate a List of available ingredients for a specific orderline.
     * The method gets a list of all ingredients and removed the ingredients that are not available.
     * Ingredients that are already linked to the Orderline are removed from the list (duplicate linking is not possible).
     * @param id The id of the Orderline that needs to be processed
     * @return A List of all ingredients that can be linked to this orderline
     */
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

    /**
     * Generate a list of orders that contain a specific ingredient.
     * This method is used to generate a List for the Mail microservice, to issue a product recall.
     * Filters are applied so only the orderlines relevant to this specific recall get returned.
     * Orderlines that do not include this ingredient are removed from the list because these products are safe to consume.
     * @param id uniqueCode of an Ingredient
     * @return A List of processed orders, contains only the unsafe orderlines.
     */
    public List<Order> getAllIngredienttracedOrders(String id){
        List<Order> orders = StreamSupport
                .stream(orderRepository.findAll().spliterator(), false)
                .map(e -> orderMapper.toDTO(e))
                .collect(Collectors.toList());

        orders.forEach(order -> {

             Orderline[] orderlines = keycloakRestTemplate.getForObject(urlOrderlines+order.getId(), Orderline[].class);
            List<Orderline> filteredOrderlines = new ArrayList<>();
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

    /**
     * Calculate the subtotal of an Order.
     * @param order The order that needs to be processed.
     * @return BigDecimal value of the order's subtotal.
     */
    public BigDecimal subTotal(Order order)
    {
        Function<Orderline, BigDecimal> totalMapper = o -> o.getPurchasePrice().multiply(BigDecimal.valueOf(o.getQty()));
        BigDecimal result = order.getOrderlines().stream().map(totalMapper).reduce(BigDecimal.ZERO, BigDecimal::add);
        result = result.setScale(2, RoundingMode.HALF_UP);
        return result;
    }

    /**
     * Calculate the total amount of VAT on this Order.
     * @param order The order that needs a VAT total calculation
     * @return A BigDecimal value of the total amount of VAT
     */
    public BigDecimal vatTotal(Order order)
    {
        double highestVat = (order.getOrderlines().stream().mapToDouble(o -> o.getProduct().getVat()).max().orElseThrow() / 100);
        BigDecimal result = ((order.getSubTotal().subtract(order.getDiscount())).add(order.getShippingCost())).multiply(BigDecimal.valueOf(highestVat));
        result = result.setScale(2, RoundingMode.HALF_UP);
       return result;
    }

    /**
     * Calculate the order total.
     * Get the subtotal, subtract the discount, add the VAT total, and add the shipping cost.
     * @param order The Order that needs processing.
     * @return The invoice total of this Order.
     */

    public BigDecimal total(Order order)
    {
        BigDecimal result = order.getSubTotal().subtract(order.getDiscount()).add(order.getVatTotal()).add(order.getShippingCost());
        result = result.setScale(2, RoundingMode.HALF_UP);
        return result;
    }

}
