package be.bakmix.eindproject.order.business.repository;

import be.bakmix.eindproject.order.business.OrderEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Spring Boot repository of the Order microservice
 */
public interface OrderRepository extends PagingAndSortingRepository<OrderEntity, Long> {
}
