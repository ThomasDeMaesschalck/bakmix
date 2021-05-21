package be.bakmix.eindproject.customers.business.repository;

import be.bakmix.eindproject.customers.business.CustomerEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for the Customer microservice. Implemented with paging support.
 */

@Repository
public interface CustomerRepository extends PagingAndSortingRepository<CustomerEntity, Long> {
}
