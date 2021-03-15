package be.bakmix.eindproject.customers.service;


import be.bakmix.eindproject.customers.business.CustomerEntity;
import be.bakmix.eindproject.customers.business.repository.CustomerRepository;
import be.bakmix.eindproject.customers.service.dto.Customer;
import be.bakmix.eindproject.customers.service.mapper.CustomerMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@AllArgsConstructor
public class CustomerService {

    private static List<Customer> customers = new ArrayList<>();

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerMapper customerMapper;

    public List<Customer> getAll(){
        List<Customer> customers = StreamSupport
                .stream(customerRepository.findAll().spliterator(), false)
                .map(e -> customerMapper.toDTO(e))
                .collect(Collectors.toList());
        return customers;
    }

    public Customer getById(Long id){
        Optional<CustomerEntity> customerEntityOptional = customerRepository.findById(id);
        if(customerEntityOptional.isPresent()){
            return customerMapper.toDTO(customerEntityOptional.get());
        }
        return null;
    }

}
