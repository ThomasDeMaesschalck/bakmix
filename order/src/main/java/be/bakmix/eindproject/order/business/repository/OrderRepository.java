package be.bakmix.eindproject.order.business.repository;

import be.bakmix.eindproject.order.business.OrderEntity;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<OrderEntity, Long> {
}
