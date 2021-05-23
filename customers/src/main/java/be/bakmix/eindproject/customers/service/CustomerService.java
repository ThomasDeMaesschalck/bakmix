package be.bakmix.eindproject.customers.service;


import be.bakmix.eindproject.customers.business.CustomerEntity;
import be.bakmix.eindproject.customers.business.repository.CustomerRepository;
import be.bakmix.eindproject.customers.service.dto.Customer;
import be.bakmix.eindproject.customers.service.mapper.CustomerMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * Service class for the Customer microservice.
 */
@Service
@AllArgsConstructor
public class CustomerService {

    /**
     * The Customer repository
     */
    @Autowired
    private final CustomerRepository customerRepository;

    /**
     * Customer mapper, mapping from Customer to Customer Entity and the other way around
     */
    @Autowired
    private final CustomerMapper customerMapper;

    /**
     * Retrieves a Page of customers from the database.
     * @param pageNo The page number
     * @param pageSize The size of the page
     * @param sortBy Sorting filter
     * @return Returns the requested Page of Customers
     */
    public Page<Customer> getAll(Integer pageNo, Integer pageSize, String sortBy){
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.DESC, sortBy));
        Page<Customer> customers = customerRepository.findAll(paging).map(customerMapper::toDTO);
        return customers;
    }

    /**
     * Get a specific customer from the database.
     * @param id The id of the Customer
     * @return If found the requested Customer is returned
     */
    public Customer getById(Long id){
        Optional<CustomerEntity> customerEntityOptional = customerRepository.findById(id);
        if(customerEntityOptional.isPresent()){
            return customerMapper.toDTO(customerEntityOptional.get());
        }
        return null;
    }

}
