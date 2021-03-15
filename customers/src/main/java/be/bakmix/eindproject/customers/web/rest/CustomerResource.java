package be.bakmix.eindproject.customers.web.rest;

import be.bakmix.eindproject.customers.service.CustomerService;
import be.bakmix.eindproject.customers.service.dto.Customer;
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
public class CustomerResource {
    @Autowired
    private CustomerService customerService;

    @GetMapping("/customers")
    public ResponseEntity<List<Customer>> getAll() {
        List<Customer> customers = customerService.getAll();
        log.info("Retrieved all customers");
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/customers/{id}")
    public ResponseEntity<Customer> getById(@PathVariable Long id){
        Customer customer = customerService.getById(id);
        if(customer == null){
            log.error("Failed to find customer number " + id);

            return ResponseEntity.notFound().build();
        }
        log.info("Retrieved customer number " + id);
        return ResponseEntity.ok(customer);
    }
}
