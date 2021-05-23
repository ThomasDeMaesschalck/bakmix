package be.bakmix.eindproject.orderline.web.rest;

import be.bakmix.eindproject.orderline.service.OrderlineService;
import be.bakmix.eindproject.orderline.service.dto.Orderline;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;

/**
 * Generate the REST API endpoints for the Orderline microservice
 */
@CrossOrigin
@RestController
@RequestMapping("/api")
@AllArgsConstructor
@Log4j2
public class OrderlineResource {

    /**
     * The service layer
     */
    @Autowired
    private final OrderlineService orderlineService;

    /**
     * Get all orderlines
     * @return All orderlines
     */
    @GetMapping("/orderlines")
    @RolesAllowed("bakmix-admin")
    public ResponseEntity<List<Orderline>> getAll() {
        List<Orderline> orderlines = orderlineService.getAll();
        log.info("Retrieved all orderlines");
        return ResponseEntity.ok(orderlines);
    }

    /**
     * Get a specific Orderline
     * @param id The id of the Orderline
     * @return The requested Orderline
     */
    @GetMapping("/orderlines/{id}")
    @RolesAllowed("bakmix-admin")
    public ResponseEntity<Orderline> getById(@PathVariable Long id){
        Orderline orderline = orderlineService.getById(id);
        if(orderline == null){
            log.error("Failed to find orderline number " + id);

            return ResponseEntity.notFound().build();
        }
        log.info("Retrieved orderline number " + id);
        return ResponseEntity.ok(orderline);
    }

    /**
     * Get all orderlines that belong to a specified Order
     * @param id The id of the Order
     * @return All orderlines for the specified Order.
     */
    @GetMapping("/orderlines/getbyorder/{id}")
    @RolesAllowed("bakmix-admin")
    public ResponseEntity<List<Orderline>> getByOrderId(@PathVariable Long id){
        List<Orderline> orderline = orderlineService.getByOrderId(id);
        if(orderline == null){
            log.error("Failed to find orderlines with order id number " + id);

            return ResponseEntity.notFound().build();
        }
        log.info("Retrieved orderlines with order id number " + id);
        return ResponseEntity.ok(orderline);
    }
}
