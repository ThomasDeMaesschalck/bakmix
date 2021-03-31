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

    public Page<Customer> getAll(Integer pageNo, Integer pageSize, String sortBy){
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.DESC, sortBy));
        Page<Customer> customers = customerRepository.findAll(paging).map(customerMapper::toDTO);
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
