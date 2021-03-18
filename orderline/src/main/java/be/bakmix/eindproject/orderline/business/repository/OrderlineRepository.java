package be.bakmix.eindproject.orderline.business.repository;

import be.bakmix.eindproject.orderline.business.OrderlineEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderlineRepository extends CrudRepository<OrderlineEntity, Long> {
}
