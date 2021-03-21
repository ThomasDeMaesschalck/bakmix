package be.bakmix.eindproject.order.web.rest;

import be.bakmix.eindproject.order.service.OrderService;
import be.bakmix.eindproject.order.service.dto.Order;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<List<Order>> getAll() {
        List<Order> orders = orderService.getAll();
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
}
