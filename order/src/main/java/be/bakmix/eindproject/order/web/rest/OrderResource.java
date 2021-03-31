package be.bakmix.eindproject.order.web.rest;

import be.bakmix.eindproject.order.service.OrderService;
import be.bakmix.eindproject.order.service.dto.Ingredient;
import be.bakmix.eindproject.order.service.dto.Order;
import be.bakmix.eindproject.order.service.dto.Orderline;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api")
@AllArgsConstructor
@Log4j2
public class OrderResource {

    @Autowired
    private OrderService orderService;

    @GetMapping("/orders")
    public ResponseEntity<List<Order>> getAll(@RequestParam(defaultValue = "0") Boolean index) {
        List<Order> orders = orderService.getAll(index);
        log.info("Retrieved all orders");
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<Order> getById(@PathVariable Long id){
        Order order = orderService.getById(id);
        if(order == null){
            log.error("Failed to find order number " + id);

            return ResponseEntity.notFound().build();
        }
        log.info("Retrieved order number " + id);
        return ResponseEntity.ok(order);
    }

    @PutMapping("/orders/{id}")
    public ResponseEntity<Order> replaceOrder(@PathVariable Long id, @Valid @RequestBody Order orderDetails) {

        Order order = orderService.getById(id);
        order.setStatus(orderDetails.getStatus());
        orderService.save(order);
        log.info("Updated order number " + id + order);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/orders/orderlinewithlinkedingredients/{id}")
    public ResponseEntity<Orderline> getOrderlineWithLinkedIngredientsById(@PathVariable Long id){
        Orderline orderline = orderService.getOrderlineWithLinkedIngredientsById(id);
        if(orderline == null){
            log.error("Failed to find orderline number " + id);

            return ResponseEntity.notFound().build();
        }
        log.info("Retrieved orderline number " + id);
        return ResponseEntity.ok(orderline);
    }

    @GetMapping("/orders/availableingredientsfororderline/{id}")
    public ResponseEntity<List<Ingredient>> getAvailableIngredientsForOrderline(@PathVariable Long id){
        List<Ingredient> ingredients = orderService.getAvailableIngredientsForOrderline(id);
        if(ingredients == null){
            log.error("Failed to find available ingredients for orderline number " + id);

            return ResponseEntity.notFound().build();
        }
        log.info("Retrieved  available ingredients for orderline number " + id);
        return ResponseEntity.ok(ingredients);
    }

    @GetMapping("/tracing/{id}")
    public ResponseEntity<List<Order>> getAllIngredienttracedOrders(@PathVariable String id){
        List<Order> orders = orderService.getAllIngredienttracedOrders(id);
        if(orders == null){
            log.error("Failed to find orders for unique ingredient " + id);

            return ResponseEntity.notFound().build();
        }
        log.info("Retrieved  orders for unique ingredient " + id);
        return ResponseEntity.ok(orders);
    }
}
