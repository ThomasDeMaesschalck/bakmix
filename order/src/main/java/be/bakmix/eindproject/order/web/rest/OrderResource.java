package be.bakmix.eindproject.order.web.rest;

import be.bakmix.eindproject.order.service.OrderService;
import be.bakmix.eindproject.order.service.dto.Ingredient;
import be.bakmix.eindproject.order.service.dto.Order;
import be.bakmix.eindproject.order.service.dto.Orderline;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.List;

/**
 * Generate the REST API endpoints for the Order microservice
 */
@CrossOrigin
@RestController
@RequestMapping("/api")
@AllArgsConstructor
@Log4j2
public class OrderResource {

    /**
     * The order service layer
     */
    @Autowired
    private final OrderService orderService;

    /**
     * Get a Page of orders
     * @param pageNo The page number
     * @param pageSize The page size
     * @param sortBy The sorting filter
     * @return The requested Page of orders
     */
    @GetMapping("/orders")
    @RolesAllowed("bakmix-admin")
    public ResponseEntity<Page<Order>> getAll(@RequestParam(defaultValue = "0") Integer pageNo,
                                              @RequestParam(defaultValue = "10") Integer pageSize,
                                              @RequestParam(defaultValue = "id") String sortBy) {
        Page<Order> orders = orderService.getAll(pageNo, pageSize, sortBy);
        log.info("Retrieved all orders");
        return ResponseEntity.ok(orders);
    }

    /**
     * Get a specific Order from the database
     * @param id The Order id
     * @return The requested Order
     */

    @GetMapping("/orders/{id}")
    @RolesAllowed("bakmix-admin")
    public ResponseEntity<Order> getById(@PathVariable Long id){
        Order order = orderService.getById(id);
        if(order == null){
            log.error("Failed to find order number " + id);

            return ResponseEntity.notFound().build();
        }
        log.info("Retrieved order number " + id);
        return ResponseEntity.ok(order);
    }

    /**
     * Adjust a persisted Order. Used to update the Order status.
     * @param id The id of the Order
     * @param orderDetails The Order details that need to be adjusted.
     * @return ResponseEntity Order
     */
    @PutMapping("/orders/{id}")
    @RolesAllowed("bakmix-admin")
    public ResponseEntity<Order> replaceOrder(@PathVariable Long id, @Valid @RequestBody Order orderDetails) {

        Order order = orderService.getById(id);
        order.setStatus(orderDetails.getStatus());
        orderService.save(order);
        log.info("Updated order number " + id + " " + order);
        return ResponseEntity.ok(order);
    }

    /**
     * Get an Orderline with all linked ingredients.
     * @param id The id of the orderline
     * @return The processed orderline
     */
    @GetMapping("/orders/orderlinewithlinkedingredients/{id}")
    @RolesAllowed("bakmix-admin")
    public ResponseEntity<Orderline> getOrderlineWithLinkedIngredientsById(@PathVariable Long id){
        Orderline orderline = orderService.getOrderlineWithLinkedIngredientsById(id);
        if(orderline == null){
            log.error("Failed to find orderline number " + id);

            return ResponseEntity.notFound().build();
        }
        log.info("Retrieved orderline number " + id);
        return ResponseEntity.ok(orderline);
    }

    /**
     * Get a List of ingredients that can be linked to an orderline
     * @param id The id of the orderline
     * @return The processed orderline
     */
    @GetMapping("/orders/availableingredientsfororderline/{id}")
    @RolesAllowed("bakmix-admin")
    public ResponseEntity<List<Ingredient>> getAvailableIngredientsForOrderline(@PathVariable Long id){
        List<Ingredient> ingredients = orderService.getAvailableIngredientsForOrderline(id);
        if(ingredients == null){
            log.error("Failed to find available ingredients for orderline number " + id);

            return ResponseEntity.notFound().build();
        }
        log.info("Retrieved  available ingredients for orderline number " + id);
        return ResponseEntity.ok(ingredients);
    }

    /**
     * Get a List of orders that contain a specific Ingredient.
     * This is used to issue a product recall, to find order(lines) that contain an unsafe ingredient.
     * @param id The uniqueCode of the Ingredient
     * @return A List of processed Order DTOs.
     */
    @GetMapping("/tracing/{id}")
    @RolesAllowed("bakmix-admin")
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
