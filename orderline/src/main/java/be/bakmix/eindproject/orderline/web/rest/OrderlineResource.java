package be.bakmix.eindproject.orderline.web.rest;

import be.bakmix.eindproject.orderline.service.OrderlineService;
import be.bakmix.eindproject.orderline.service.dto.Orderline;
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
public class OrderlineResource {

    @Autowired
    private OrderlineService orderlineService;

    @GetMapping("/orderlines")
    public ResponseEntity<List<Orderline>> getAll() {
        List<Orderline> orderlines = orderlineService.getAll();
        log.info("Retrieved all orderlines");
        return ResponseEntity.ok(orderlines);
    }

    @GetMapping("/orderlines/{id}")
    public ResponseEntity<Orderline> getById(@PathVariable Long id){
        Orderline orderline = orderlineService.getById(id);
        if(orderline == null){
            log.error("Failed to find orderline number " + id);

            return ResponseEntity.notFound().build();
        }
        log.info("Retrieved orderline number " + id);
        return ResponseEntity.ok(orderline);
    }
}
