package be.bakmix.eindproject.customers.web.rest;

import be.bakmix.eindproject.customers.service.CustomerService;
import be.bakmix.eindproject.customers.service.dto.Customer;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
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
    @RolesAllowed("bakmix-admin")
    public ResponseEntity<Page<Customer>> getAll(@RequestParam(defaultValue = "0") Integer pageNo,
                                    @RequestParam(defaultValue = "10") Integer pageSize,
                                    @RequestParam(defaultValue = "id") String sortBy)
    {
        Page<Customer> customers = customerService.getAll(pageNo, pageSize, sortBy);
        log.info("Retrieved all customers");
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/customers/{id}")
    @RolesAllowed("bakmix-admin")
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
