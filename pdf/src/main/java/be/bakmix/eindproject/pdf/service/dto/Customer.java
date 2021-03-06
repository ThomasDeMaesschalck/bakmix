package be.bakmix.eindproject.pdf.service.dto;

import lombok.Data;

/**
 * Customer DTO
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
