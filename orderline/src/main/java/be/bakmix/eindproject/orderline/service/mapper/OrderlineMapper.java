package be.bakmix.eindproject.orderline.service.mapper;

import be.bakmix.eindproject.orderline.business.OrderlineEntity;
import be.bakmix.eindproject.orderline.service.dto.Orderline;
import org.mapstruct.Mapper;

/**
 * Mapstruct mapping class. Maps from DTO to Entity and other way around.
 */
@Mapper(componentModel = "spring")
public interface OrderlineMapper {
    Orderline toDTO(OrderlineEntity orderlineEntity);
    OrderlineEntity toEntity(Orderline orderline);
}
