package be.bakmix.eindproject.customers.business.repository;

import be.bakmix.eindproject.customers.business.CustomerEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends CrudRepository<CustomerEntity, Long> {
}
