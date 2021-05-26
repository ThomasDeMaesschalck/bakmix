package be.bakmix.eindproject.customers.service;

import be.bakmix.eindproject.customers.business.CustomerEntity;
import be.bakmix.eindproject.customers.business.repository.CustomerRepository;
import be.bakmix.eindproject.customers.service.dto.Customer;
import be.bakmix.eindproject.customers.service.mapper.CustomerMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CustomerServiceTest {

    @Mock
    CustomerRepository customerRepository;

    @Autowired
    CustomerMapper customerMapper;

    @InjectMocks
    CustomerService customerService;


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getAll(){
        customerService = new CustomerService(customerRepository, customerMapper);
        List<CustomerEntity> list = new ArrayList<>();
        CustomerEntity c1 = new CustomerEntity();
        c1.setId(Long.parseLong("1"));
        c1.setFirstName("Tom");
        c1.setLastName("Van Lokeren");
        list.add(c1);

        Page<CustomerEntity> pagedList = new PageImpl<>(list);
        Pageable paging = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "id"));

        given(customerRepository.findAll(paging)).willReturn(pagedList);

        Page<Customer> customerList = customerService.getAll(0, 10, "id");

        //size should be 1
        assertEquals(1, customerList.toList().size());
    }

    @Test
    public void getById(){
        customerService = new CustomerService(customerRepository, customerMapper);
        CustomerEntity cust1 = new CustomerEntity();
        cust1.setId(Long.parseLong("1"));
        cust1.setFirstName("Thomas");

        given(customerRepository.findById(Long.parseLong("1"))).willReturn(java.util.Optional.of(cust1));

        Customer customer = customerService.getById(Long.parseLong("1"));

        //first name should be Thomas
        assertEquals("Thomas", customer.getFirstName());
    }

}
