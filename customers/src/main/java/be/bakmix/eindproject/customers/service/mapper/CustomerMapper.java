package be.bakmix.eindproject.customers.service.mapper;

import be.bakmix.eindproject.customers.business.CustomerEntity;
import be.bakmix.eindproject.customers.service.dto.Customer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    Customer toDTO(CustomerEntity customerEntity);
    CustomerEntity toEntity(Customer customer);
}
