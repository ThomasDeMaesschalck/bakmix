package be.bakmix.eindproject.product.service.mapper;

import be.bakmix.eindproject.product.business.ProductEntity;
import be.bakmix.eindproject.product.service.dto.Product;
import org.mapstruct.Mapper;

/**
 * Mapstruct mapping class. Map from DTO to Entity and the other way around.
 */
@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product toDTO(ProductEntity productEntity);
    ProductEntity toEntity(Product product);
}
