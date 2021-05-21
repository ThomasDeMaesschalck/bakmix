package be.bakmix.eindproject.orderline.business.repository;

import be.bakmix.eindproject.orderline.business.OrderlineEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Boot repository of Orderlines
 */
@Repository
public interface OrderlineRepository extends CrudRepository<OrderlineEntity, Long> {
}
