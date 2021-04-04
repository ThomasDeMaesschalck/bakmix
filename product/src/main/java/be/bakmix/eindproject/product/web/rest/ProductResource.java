package be.bakmix.eindproject.product.web.rest;

import be.bakmix.eindproject.product.service.ProductService;
import be.bakmix.eindproject.product.service.dto.Product;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api")
@AllArgsConstructor
@Log4j2
public class ProductResource {

    @Autowired
    private ProductService productService;

    @GetMapping("/products")
    @RolesAllowed({"bakmix-admin"})
    public ResponseEntity<List<Product>> getAll() {
        List<Product> products = productService.getAll();
        log.info("Retrieved all products");
        return ResponseEntity.ok(products);
    }

    @GetMapping("/products/{id}")
    @RolesAllowed("bakmix-admin")
    public ResponseEntity<Product> getById(@PathVariable Long id){
        Product product = productService.getById(id);
        if(product == null){
            log.error("Failed to find product number " + id);

            return ResponseEntity.notFound().build();
        }
        log.info("Retrieved product number " + id);
        return ResponseEntity.ok(product);
    }

}
