package be.bakmix.eindproject.order.business.repository;

import be.bakmix.eindproject.order.business.OrderEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface OrderRepository extends PagingAndSortingRepository<OrderEntity, Long> {
}
