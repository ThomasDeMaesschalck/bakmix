package be.bakmix.eindproject.customers.business;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name="Customers")
@Data
public class CustomerEntity {
    @Id
    @GeneratedValue
    private Long id;
    private String firstName;
    private String lastName;
    private String address;
    private String zipCode;
    private String city;
    private String phone;
    private String email;
}
