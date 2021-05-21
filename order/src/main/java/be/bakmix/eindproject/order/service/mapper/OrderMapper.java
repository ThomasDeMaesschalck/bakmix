package be.bakmix.eindproject.order.service.mapper;

import be.bakmix.eindproject.order.business.OrderEntity;
import be.bakmix.eindproject.order.service.dto.Order;
import org.mapstruct.Mapper;

/**
 * Mapstruct mapping class. From Entity to DTO and the other way around.
 */
@Mapper(componentModel = "spring")
public interface OrderMapper {
    Order toDTO(OrderEntity orderEntity);
    OrderEntity toEntity(Order order);
}
