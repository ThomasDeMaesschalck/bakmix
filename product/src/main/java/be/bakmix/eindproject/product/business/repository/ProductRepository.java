package be.bakmix.eindproject.product.business.repository;

import be.bakmix.eindproject.product.business.ProductEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Boot repository for Product microservice
 */
@Repository
public interface ProductRepository extends CrudRepository<ProductEntity, Long> {
}
