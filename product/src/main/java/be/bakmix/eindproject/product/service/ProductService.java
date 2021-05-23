package be.bakmix.eindproject.product.service;


import be.bakmix.eindproject.product.business.ProductEntity;
import be.bakmix.eindproject.product.business.repository.ProductRepository;
import be.bakmix.eindproject.product.service.dto.Product;
import be.bakmix.eindproject.product.service.mapper.ProductMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Service layer of the Product microservice
 */
@Service
@AllArgsConstructor
public class ProductService {

    /**
     * The product repository
     */
    @Autowired
    private final ProductRepository productRepository;

    /**
     * The product mapping class
     */
    @Autowired
    private final ProductMapper productMapper;

    /**
     * Get a List of all products
     * @return List of products
     */
    public List<Product> getAll(){
        List<Product> products = StreamSupport
                .stream(productRepository.findAll().spliterator(), false)
                .map(e -> productMapper.toDTO(e))
                .collect(Collectors.toList());
        return products;
    }

    /**
     * Get a product by id
     * @param id The id of the Product
     * @return The requested Product
     */
    public Product getById(Long id){
        Optional<ProductEntity> productEntityOptional = productRepository.findById(id);
        if(productEntityOptional.isPresent()){
            return productMapper.toDTO(productEntityOptional.get());
        }
        return null;
    }

}
