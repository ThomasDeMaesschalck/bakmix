package be.bakmix.eindproject.order.service;

import be.bakmix.eindproject.order.business.OrderEntity;
import be.bakmix.eindproject.order.business.repository.OrderRepository;
import be.bakmix.eindproject.order.service.dto.*;
import be.bakmix.eindproject.order.service.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class OrderService {

    private static List<Order> orders = new ArrayList<>();

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderMapper orderMapper;

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

    public List<Order> getAll(){
        List<Order> orders = StreamSupport
                .stream(orderRepository.findAll().spliterator(), false)
                .map(e -> orderMapper.toDTO(e))
                .collect(Collectors.toList());

        for(Order order : orders){
            RestTemplate rtCustomer = new RestTemplate();
            Customer customer = rtCustomer.getForObject(urlCustomers+order.getCustomerId(), Customer.class);
            order.setCustomerId(customer.getId());
            order.setCustomer(customer);
        }


        for(Order order : orders){
            RestTemplate rtOrderlines = new RestTemplate();
            Orderline[] orderlines = rtOrderlines.getForObject(urlOrderlines+order.getId(), Orderline[].class);

            for(Orderline orderline: orderlines)
            {
                RestTemplate rtIngredienttracings = new RestTemplate();
                Ingredienttracing[] ingredienttracings = rtIngredienttracings.getForObject(urlIngredienttracings+orderline.getId(), Ingredienttracing[].class);
                List<Ingredient> ingredients = new ArrayList();

                for(Ingredienttracing ingredienttracing: ingredienttracings) 
                {
                    Long ingredientId = ingredienttracing.getIngredientId();
                    RestTemplate rtIngredients = new RestTemplate();
                    Ingredient ingredient = rtIngredients.getForObject(urlIngredients+ingredientId, Ingredient.class);
                    ingredients.add(ingredient);
                    orderline.setIngredients(ingredients);
                }
            }

            order.setOrderlines(Arrays.asList(orderlines));
        }

        return orders;
    }

    public Order getById(Long id){
        Optional<OrderEntity> optionalOrderEntity = orderRepository.findById(id);
        if(optionalOrderEntity.isPresent()){
            Order order = orderMapper.toDTO(optionalOrderEntity.get());

            RestTemplate rtCustomer = new RestTemplate();
            Customer customer = rtCustomer.getForObject(urlCustomers+order.getCustomerId(), Customer.class);
            order.setCustomer(customer);

            RestTemplate rtOrderlines = new RestTemplate();
            Orderline[] orderlines = rtOrderlines.getForObject(urlOrderlines+order.getId(), Orderline[].class);

            for(Orderline orderline: orderlines)
            {
                RestTemplate rtIngredienttracings = new RestTemplate();
                Ingredienttracing[] ingredienttracings = rtIngredienttracings.getForObject(urlIngredienttracings+orderline.getId(), Ingredienttracing[].class);
                List<Ingredient> ingredients = new ArrayList();

                for(Ingredienttracing ingredienttracing: ingredienttracings)
                {
                    Long ingredientId = ingredienttracing.getIngredientId();
                    RestTemplate rtIngredients = new RestTemplate();
                    Ingredient ingredient = rtIngredients.getForObject(urlIngredients+ingredientId, Ingredient.class);
                    ingredients.add(ingredient);
                    orderline.setIngredients(ingredients);
                }
            }

            order.setOrderlines(Arrays.asList(orderlines));
            return order;
        }
        return null;
    }

    public Orderline getOrderlineWithLinkedIngredientsById(Long id){

            RestTemplate rtOrderlines = new RestTemplate();
            Orderline orderline = rtOrderlines.getForObject(urlOrderlinesById+id, Orderline.class);

                RestTemplate rtIngredienttracings = new RestTemplate();
                Ingredienttracing[] ingredienttracings = rtIngredienttracings.getForObject(urlIngredienttracings+orderline.getId(), Ingredienttracing[].class);
                List<Ingredient> ingredients = new ArrayList();

                for(Ingredienttracing ingredienttracing: ingredienttracings)
                {
                    Long ingredientId = ingredienttracing.getIngredientId();
                    RestTemplate rtIngredients = new RestTemplate();
                    Ingredient ingredient = rtIngredients.getForObject(urlIngredients+ingredientId, Ingredient.class);
                    ingredient.setIngredienttracingId(ingredienttracing.getId());
                    ingredients.add(ingredient);
                    orderline.setIngredients(ingredients);
                }

            return orderline;
    }

    public List<Ingredient> getAvailableIngredientsForOrderline(Long id) {

        RestTemplate rtIngredients = new RestTemplate();
        Ingredient[] ingredients = rtIngredients.getForObject(urlIngredients, Ingredient[].class);

        List<Ingredient> ingredientsList = Arrays.asList(ingredients);


        RestTemplate rtIngredienttracings = new RestTemplate();
        Ingredienttracing[] ingredienttracings = rtIngredienttracings.getForObject(urlIngredienttracings + id, Ingredienttracing[].class);

        ingredientsList = ingredientsList.stream().filter(i -> i.getAvailable() == true).collect(Collectors.toList());


        for (Ingredienttracing ingredienttracing : ingredienttracings)
        {
            for (Ingredient ingredient: ingredientsList)
            {
                if (ingredienttracing.getIngredientId() == ingredient.getId())
                {
                    ingredientsList.remove(ingredient);
                    break;
                }
            }
        }

        return ingredientsList;
    }
}
