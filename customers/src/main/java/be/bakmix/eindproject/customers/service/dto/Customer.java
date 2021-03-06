package be.bakmix.eindproject.customers.service.dto;

import lombok.Data;

/**
 * DTO for the Customer object
 */
@Data
public class Customer {
    private Long id;
    private String firstName;
    private String lastName;
    private String address;
    private String zipCode;
    private String city;
    private String phone;
    private String email;
}
