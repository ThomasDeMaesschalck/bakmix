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

/**
 * Resource class for the Customer microservice. Creates REST endpoints.
 */
@CrossOrigin
@RestController
@RequestMapping("/api")
@AllArgsConstructor
@Log4j2
public class CustomerResource {

    /**
     * The Customer service class
     */
    @Autowired
    private CustomerService customerService;

    /**
     * This class is used by the frontend to retrieve a Page of Customers from the database.
     * @param pageNo The page number
     * @param pageSize The size of the paging
     * @param sortBy Sorting filter
     * @return Returns the requested Page of Customers
     */
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

    /**
     * Get a specific customer from the database.
     * @param id The id of the Customer
     * @return Returns the requested Customer
     */
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
